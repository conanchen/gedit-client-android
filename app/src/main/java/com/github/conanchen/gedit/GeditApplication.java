package com.github.conanchen.gedit;

import android.app.Activity;
import android.app.Service;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.stetho.Stetho;
import com.github.conanchen.gedit.di.common.AppInjector;
import com.github.conanchen.gedit.repository.RepositoryFascade;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

public class GeditApplication extends MultiDexApplication implements HasActivityInjector, HasServiceInjector {
    public static final String TAG = GeditApplication.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceInjector;

    @Inject
    RepositoryFascade repositoryFascade;

    @Override
    public void onCreate() {
        super.onCreate();

//        if (BuildConfig.USE_CRASHLYTICS) {
//            Fabric.with(this, new Crashlytics());
//            new ANRWatchDog().start();
//        }
        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//            ARouter.openDebug();
            initStethoDebugBridge();
        }

        AppInjector.init(this);

        repositoryFascade.helloRepository.sayHello("Conan Chen");
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化//
//
//        DaemonEnv.initialize(this, AppServiceKeepliveTraceImpl.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
//        try {
//            startService(new Intent(this, AppServiceKeepliveTraceImpl.class));
//            startService(new Intent(this, AppServiceCommandSenderImpl.class));
//        } catch (Exception ignored) {
//        }

//        ProviderInstaller.installIfNeededAsync(this, providerInstallListener);
//        List<Provider> providers = JcaUtils.getSecurityProviders();
//        for (Provider provider : providers) {
//            List<Provider.Service> services = JcaUtils.getSortedProviderServices(provider);
//            for (Provider.Service service : services) {
//                Log.i(TAG, String.format("Provider=%s,Algorithm=%s", provider.getName(), service.getAlgorithm()));
//            }
//        }
    }

    //    private ProviderInstaller.ProviderInstallListener providerInstallListener =
//            new ProviderInstaller.ProviderInstallListener() {
//                @Override
//                public void onProviderInstalled() {
//                    // Provider installed
//                    Log.i(TAG, String.format("%s", "Provider installed"));
//                }
//
//                @Override
//                public void onProviderInstallFailed(int errorCode, Intent recoveryIntent) {
//                    Log.i(TAG, String.format("%s", "Provider installation failed errorCode=%d", errorCode));
//                    // Provider installation failed
//                }
//            };
    private void initStethoDebugBridge() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (BuildConfig.DEBUG) {
//            Stetho.initialize(
//                    Stetho.newInitializerBuilder(this)
//                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                            //.enableWebKitInspector(new CouchbaseInspectorModulesProvider.Builder(this).build())
//                            .build());
            Stetho.initializeWithDefaults(this);
        }
    }


    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public DispatchingAndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }

}

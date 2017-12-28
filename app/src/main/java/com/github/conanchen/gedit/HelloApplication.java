package com.github.conanchen.gedit;

import android.app.Activity;
import android.app.Service;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.github.conanchen.gedit.di.AppInjector;
import com.github.conanchen.gedit.repository.RepositoryFascade;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

public class HelloApplication extends MultiDexApplication implements HasActivityInjector, HasServiceInjector {
    public static final String TAG = HelloApplication.class.getSimpleName();

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
//        ARouter.init(this);
//
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

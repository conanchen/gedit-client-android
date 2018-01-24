package com.github.conanchen.gedit.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragmentActivity;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.ui.my.MySummaryFragment;
import com.github.conanchen.gedit.ui.payment.GaptureActivity;
import com.github.conanchen.gedit.ui.store.StoreListFragment;
import com.github.conanchen.gedit.util.CustomPopWindow;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseFragmentActivity implements CustomPopWindow.OnItemClickListener {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    CurrentSigninViewModel currentSigninViewModel;


    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;


    private List<Fragment> viewPagerList = new ArrayList<>();//装viewPager中的Fragment
    private MainViewPagerAdapter mainViewPagerAdapter; //ViewPager的适配器

    private CustomPopWindow popWindow;

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupViewModel();
        setupPop();

        initView();//初始化数据
    }

    private void setupViewModel() {
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
    }


    private void initView() {

        viewPagerList.add(new StoreListFragment());
        viewPagerList.add(new MySummaryFragment());

        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), viewPagerList);
        mViewPager.setAdapter(mainViewPagerAdapter);


        //默认选中首页
        mViewPager.setCurrentItem(0);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().getItem(0).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().getItem(2).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_navigation_main:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.bottom_navigation_scan:
//                        startScan();
                        ARouter.getInstance().build("/app/PointsPayActivity").navigation();
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.bottom_navigation_my:
                        mViewPager.setCurrentItem(1);
                        break;
                }

                return true;//要return true否则点击各个item就没法选中了
            }
        });
    }


    /**
     * 设置右上角弹框的内容
     */
    private void setupPop() {
        //设置右上角弹框内容
        List<String> menu = new ArrayList<>();
        menu.add("扫一扫");
        menu.add("收款");
        menu.add("录单");
        popWindow = new CustomPopWindow(this, menu);
    }


    @OnClick(R.id.right)
    public void onViewClicked() {
        popWindow.showLocation(R.id.right);
        popWindow.setOnItemClickListener(this);
    }

    @Override
    public void onClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            //扫一扫
            startScan();
        } else if (position == 1) {
            //收款界面
            ARouter.getInstance().build("/app/PayeeQRCodeActivity").navigation();
        } else {
            //录单
            ARouter.getInstance().build("/app/BuyInvestPaymentActivity").navigation();
        }
    }

    private void startScan() {
        Observable.just(true).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
            Dexter.withActivity(MainActivity.this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
//                                    ARouter.getInstance().build("/app/GaptureActivity").navigation();
                            Intent intent = new Intent(MainActivity.this, GaptureActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(MainActivity.this, "我们需要摄像头访问权限来启动扫码功能，can not open GaptureActivity ", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            MainActivity.this.showPermissionRationale(token);
                        }
                    })
                    .withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {

                        }
                    })
                    .onSameThread()
                    .check();
        });

    }

    /**
     * 回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);

                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();

                    ARouter.getInstance().build("/app/PointsPayActivity")
                            .withString("code", result)
                            .navigation();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showPermissionRationale(final PermissionToken token) {
        new AlertDialog.Builder(this).setTitle(R.string.permission_rationale_title)
                .setMessage(R.string.permission_rationale_message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.cancelPermissionRequest();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.continuePermissionRequest();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .show();
    }


}

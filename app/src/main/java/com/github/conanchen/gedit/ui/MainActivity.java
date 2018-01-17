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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragmentActivity;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.ui.my.MySummaryFragment;
import com.github.conanchen.gedit.ui.my.myinvestpayment.MyInvestPaymentsActivity;
import com.github.conanchen.gedit.ui.payment.GaptureActivity;
import com.github.conanchen.gedit.ui.payment.PayeeQRCodeActivity;
import com.github.conanchen.gedit.ui.payment.PointsPayActivity;
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

public class MainActivity extends BaseFragmentActivity implements CustomPopWindow.OnItemClickListener {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    CurrentSigninViewModel currentSigninViewModel;


    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.main_pic)
    ImageView mainPic;
    @BindView(R.id.main_text)
    TextView mainText;
    @BindView(R.id.main_frame_layout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.me_pic)
    ImageView mePic;
    @BindView(R.id.me_text)
    TextView meText;
    @BindView(R.id.me_frame_layout)
    FrameLayout meFrameLayout;

    private FragmentManager manager;
    private StoreListFragment storeListFragment;
    private MySummaryFragment mySummaryFragment;

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
        initView();
    }

    private void setupViewModel() {
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
    }


    private void initView() {

        storeListFragment = new StoreListFragment();
        mySummaryFragment = new MySummaryFragment();

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction
                .add(R.id.container, storeListFragment)
                .add(R.id.container, mySummaryFragment)
                .hide(mySummaryFragment)
                .show(storeListFragment)
                .commit();

        //默认主页
        mainText.setTextColor(getResources().getColor(R.color.blue));
        meText.setTextColor(getResources().getColor(R.color.text_color));
        mainPic.setImageResource(R.mipmap.add);
        mePic.setImageResource(R.mipmap.add);

        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.hide(mySummaryFragment)
                .show(storeListFragment)
                .commit();
    }

    @OnClick({R.id.right, R.id.main_frame_layout, R.id.me_frame_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right:
                //右上角的弹框
                popWindow.showLocation(R.id.right);
                popWindow.setOnItemClickListener(this);
                break;
            case R.id.main_frame_layout:
                //点击了主页
                mainText.setTextColor(getResources().getColor(R.color.blue));
                meText.setTextColor(getResources().getColor(R.color.text_color));
                mainPic.setImageResource(R.mipmap.add);
                mePic.setImageResource(R.mipmap.add);

                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.hide(mySummaryFragment)
                        .show(storeListFragment)
                        .commit();

                break;
            case R.id.me_frame_layout:
                //点击了我的
                mainText.setTextColor(getResources().getColor(R.color.text_color));
                meText.setTextColor(getResources().getColor(R.color.blue));
                mainPic.setImageResource(R.mipmap.add);
                mePic.setImageResource(R.mipmap.add);

                FragmentTransaction transaction = manager.beginTransaction();
                transaction
                        .hide(storeListFragment)
                        .show(mySummaryFragment)
                        .commit();
                break;
        }
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

    @Override
    public void onClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            //扫一扫
            new Thread(new Runnable() {
                @Override
                public void run() {
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
                }
            }).start();
        } else if (position == 1) {
            //收款界面
//            startActivity(new Intent(this, PointsPayActivity.class));
            startActivity(new Intent(this, PayeeQRCodeActivity.class));
        } else {
            //录单
            startActivity(new Intent(this, MyInvestPaymentsActivity.class));
        }
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
//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "我要去积分抵扣付款", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, PointsPayActivity.class));
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

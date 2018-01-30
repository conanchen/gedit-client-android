package com.github.conanchen.gedit.ui.my.mystore;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.room.store.StoreWorker;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.ui.payment.GaptureActivity;
import com.github.conanchen.utils.vo.PaymentInfo;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.github.conanchen.utils.vo.VoLoadGrpcStatus;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 我的员工列表界面
 */
@Route(path = "/app/MyStoreEmployeesActivity")
public class MyStoreEmployeesActivity extends BaseActivity {

    private Gson gson = new Gson();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    MyStoreEmployeesViewModel myStoreEmployeesViewModel;
    private CurrentSigninViewModel currentSigninViewModel;

    @BindView(R.id.back)
    AppCompatImageButton back;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout mSmartRefresh;
    private MyStoreEmployeesAdapter mAdapter;

    @Autowired
    String storeUuid;//商铺的uuid

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    private boolean isLogin = false;//是否登录
    private VoAccessToken voAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_employees);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);

        setupRecyclerView();
        setupViewModel();
        setupRefresh();
    }

    private void setupRefresh() {
        mSmartRefresh.setRefreshHeader(new ClassicsHeader(this));
        mSmartRefresh.setHeaderHeight(60);

        mSmartRefresh.setOnRefreshListener((refreshLayout) -> {
            currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
                if (isLogin) {
                    PaymentInfo paymentInfo = PaymentInfo.builder()
                            .setPayeeStoreUuid(storeUuid)
                            .setVoAccessToken(voAccessToken)
                            .build();
                    myStoreEmployeesViewModel.getAllEmployees(paymentInfo);
                }
                mSmartRefresh.finishRefresh();
            });
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyStoreEmployeesAdapter();
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyStoreEmployeesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(StoreWorker store) {
//                Toast.makeText(MyStoreEmployeesActivity.this, "员工详情", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupViewModel() {
        myStoreEmployeesViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyStoreEmployeesViewModel.class);
        myStoreEmployeesViewModel.getLiveStores().observe(this, stores -> {
            if (stores != null) {
                mAdapter.setList(stores);
            }
        });

        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (Status.Code.OK == signinResponse.getStatus().getCode()) {
                isLogin = true;
                voAccessToken = VoAccessToken.builder()
                        .setAccessToken(Strings.isNullOrEmpty(signinResponse.getAccessToken()) ? System.currentTimeMillis() + "" : signinResponse.getAccessToken())
                        .setExpiresIn(Strings.isNullOrEmpty(signinResponse.getExpiresIn()) ? System.currentTimeMillis() + "" : signinResponse.getExpiresIn())
                        .build();

                PaymentInfo paymentInfo = PaymentInfo.builder()
                        .setPayeeStoreUuid(storeUuid)
                        .setVoAccessToken(voAccessToken)
                        .build();
                myStoreEmployeesViewModel.getAllEmployees(paymentInfo);

            } else {
                isLogin = false;
            }
        });

        currentSigninViewModel.getGrpcApiStatus().observe(this, new Observer<VoLoadGrpcStatus>() {
            @Override
            public void onChanged(@Nullable VoLoadGrpcStatus voLoadGrpcStatus) {
                if (voLoadGrpcStatus != null && !Strings.isNullOrEmpty(voLoadGrpcStatus.status)) {
                    Toast.makeText(MyStoreEmployeesActivity.this, voLoadGrpcStatus.message, Toast.LENGTH_SHORT).show();
                }
            }
        });


        myStoreEmployeesViewModel.getAddWorkerLiveData().observe(this, workshipResponse -> {
            if (Status.Code.OK == workshipResponse.getStatus().getCode()) {
                Log.i("-=-=-=--------------", gson.toJson(workshipResponse));
            }
        });

    }

    @OnClick({R.id.back, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                startScan();
                break;
        }
    }

    private void startScan() {
        Observable.just(true).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
            Dexter.withActivity(MyStoreEmployeesActivity.this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
//                                    ARouter.getInstance().build("/app/GaptureActivity").navigation();
                            Intent intent = new Intent(MyStoreEmployeesActivity.this, GaptureActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(MyStoreEmployeesActivity.this, "我们需要摄像头访问权限来启动扫码功能，can not open GaptureActivity ", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            MyStoreEmployeesActivity.this.showPermissionRationale(token);
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
                    String workerUuid = Strings.isNullOrEmpty(result) ? System.currentTimeMillis() + "" : result;//员工的uuid

                    PaymentInfo paymentInfo = PaymentInfo.builder()
                            .setVoAccessToken(voAccessToken)
                            .setPayeeStoreUuid(storeUuid)
                            .setPayeeWorkerUuid(workerUuid)
                            .build();
                    myStoreEmployeesViewModel.addWorker(paymentInfo);
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

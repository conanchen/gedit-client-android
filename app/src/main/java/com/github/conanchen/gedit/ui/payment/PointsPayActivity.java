package com.github.conanchen.gedit.ui.payment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.payment.common.grpc.PaymentResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.CreatePaymentRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.GetReceiptCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.PrepareMyPaymentResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 积分付款界面
 */
@Route(path = "/app/PointsPayActivity")
public class PointsPayActivity extends BaseActivity {
    private Gson gson = new Gson();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PointsPayViewModel pointsPayViewModel;

    @BindView(R.id.store_name)
    AppCompatTextView storeName;
    @BindView(R.id.need_pay)
    AppCompatEditText needPay;
    @BindView(R.id.use_integral_desc)
    AppCompatTextView useIntegralDesc;
    @BindView(R.id.use)
    AppCompatCheckBox use;
    @BindView(R.id.no_use_integral_desc)
    AppCompatTextView noUseIntegralDesc;
    @BindView(R.id.no_use)
    AppCompatCheckBox noUse;
    @BindView(R.id.money)
    AppCompatTextView money;
    @BindView(R.id.used_integral)
    AppCompatTextView usedIntegral;
    @BindView(R.id.submit)
    AppCompatButton submit;


    @Autowired
    public String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);

        setupViewModel();
    }

    private void setupViewModel() {
        pointsPayViewModel = ViewModelProviders.of(this, viewModelFactory).get(PointsPayViewModel.class);
        pointsPayViewModel.getPaymentStoreDetailsLiveData().observe(this, new Observer<GetReceiptCodeResponse>() {
            @Override
            public void onChanged(@Nullable GetReceiptCodeResponse getReceiptCodeResponse) {
                Log.i("-=-=-=-=-=", gson.toJson(getReceiptCodeResponse));
                if (getReceiptCodeResponse != null) {
//                    string code = 1; //店员/收银员客户端使用这个代码生成收款码QRCode
//                    int64 expiresIn = 3; //code的过期时间
//                    //以下为QRCode界面可能用到的提示用信息
//                    string payeeUuid = 5; //店主uuid
//                    string payeeLogo = 7; //店主logo
//                    string payeeName = 9; //店主昵称
//                    string payeeStoreUuid = 13; //店员/收银员工作店铺的uuid
//                    string payeeStoreLogo = 15; //工作店铺的logo
//                    string payeeStoreNamee = 17; //工作店铺的名称
//                    string payeeWorkerUuid = 25; //店员/收银员uuid
//                    string payeeWorkerLogo = 27; //店员/收银员logo
//                    string payeeWorkerName = 29; //店员/收银员昵称
                    //需要  payeeReceiptCode
                    // TODO: 2018/1/22  处理界面显示

                    String payeeReceiptCode = "oooooo";//服务器返回
                    pointsPayViewModel.getPayment(payeeReceiptCode);
                }
            }
        });
        pointsPayViewModel.getPaymentStoreDetails(code);

        pointsPayViewModel.getPaymentLiveData().observe(this, new Observer<PrepareMyPaymentResponse>() {
            @Override
            public void onChanged(@Nullable PrepareMyPaymentResponse prepareMyPaymentResponse) {
                Log.i("-=-=-=-=-=getPayment()", gson.toJson(prepareMyPaymentResponse));
                if (prepareMyPaymentResponse != null) {
//                    string payeeReceiptCode = 11; //收款码
//                    string payeeUuid = 12; //收款人（其实是店主）
//                    string payeeStoreUuid = 13; //收款店铺
//                    string payeeWorkerUuid = 14; //收款员工
//
//                    int32 shouldPay = 15; //应付金额 = 实付金额 + 实付积分等价金额
//                    int32 actualPay = 16; //实付金额
//                    int32 pointsPay = 17; //实付积分代替金额
//                    int32 pointsRepay = 18; //返还积分
                    pointsPayViewModel.getCreatePayment(CreatePaymentRequest.newBuilder().build());
                }
            }
        });


        pointsPayViewModel.getCreatePaymentLiveData().observe(this, new Observer<PaymentResponse>() {
            @Override
            public void onChanged(@Nullable PaymentResponse paymentResponse) {
                Log.i("-=-=-=-=-=create()", gson.toJson(paymentResponse));
                if (paymentResponse != null) {

                }
            }
        });


    }

    @OnClick({R.id.back, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                break;
        }
    }
}

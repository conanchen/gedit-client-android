package com.github.conanchen.gedit.ui.payment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.NetworkUtils;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.PaymentChannel;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.payer.activeinapp.grpc.GetPayeeCodeResponse;
import com.github.conanchen.gedit.payer.activeinapp.grpc.PreparePayerInappPaymentResponse;
import com.github.conanchen.gedit.payment.common.grpc.PaymentResponse;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.util.pay.AliPayUtil;
import com.github.conanchen.gedit.util.pay.PayResultCallBack;
import com.github.conanchen.gedit.util.pay.WxPay;
import com.github.conanchen.utils.vo.PaymentInfo;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.common.base.Strings;
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

    private static final String TAG = PointsPayActivity.class.getSimpleName();
    private Gson gson = new Gson();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PointsPayViewModel pointsPayViewModel;
    private CurrentSigninViewModel currentSigninViewModel;


    @BindView(R.id.store_name)
    AppCompatTextView mTvStoreName;
    @BindView(R.id.need_pay)
    AppCompatEditText mEtNeedPay;
    @BindView(R.id.money)
    AppCompatTextView mTvMoney;
    @BindView(R.id.desc)
    AppCompatTextView desc;
    @BindView(R.id.submit)
    AppCompatButton mBtSubmit;
    @BindView(R.id.use_points)
    AppCompatCheckBox mCbUsePoints;
    @BindView(R.id.no_use_points)
    AppCompatCheckBox mCbNoUsePoints;
    @BindView(R.id.ali_pay)
    AppCompatTextView mTvAliPay;
    @BindView(R.id.ali_bg)
    AppCompatImageView mIvAliBg;
    @BindView(R.id.weixin)
    AppCompatTextView mTvWeixin;
    @BindView(R.id.weinxin_bg)
    AppCompatImageView mIvWeinxinBg;

    @Autowired
    public String code;

    private PaymentChannel paymentChannelSelected = PaymentChannel.ALIPAY;
    boolean isPay = false;//是否已经调起了支付

    private String payeeCode;
    private VoAccessToken voAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);

        setupViewModel();
        setUpEditText();

    }

    private void setUpEditText() {
        mEtNeedPay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Strings.isNullOrEmpty(s.toString())) {

                    PaymentInfo paymentInfo = PaymentInfo.builder()
                            .setPayeeCode(payeeCode)
                            .setVoAccessToken(voAccessToken)
                            .setShouldPay(Integer.parseInt(s.toString().trim()))
                            .setPointsPay(mCbUsePoints.isChecked())
                            .build();
                    pointsPayViewModel.getPayment(paymentInfo);
                } else {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupViewModel() {

        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (com.github.conanchen.gedit.common.grpc.Status.Code.OK.getNumber() == signinResponse.getStatus().getCode().getNumber()) {
                voAccessToken = VoAccessToken.builder()
                        .setAccessToken(Strings.isNullOrEmpty(signinResponse.getAccessToken()) ? System.currentTimeMillis() + "" : signinResponse.getAccessToken())
                        .setExpiresIn(Strings.isNullOrEmpty(signinResponse.getExpiresIn()) ? System.currentTimeMillis() + "" : signinResponse.getExpiresIn())
                        .build();

                //获取商铺信息
                PaymentInfo paymentInfo = PaymentInfo.builder()
                        .setVoAccessToken(voAccessToken)
                        .setPayeeCode(Strings.isNullOrEmpty(code) ? System.currentTimeMillis() + "" : code)
                        .build();

                pointsPayViewModel.getPaymentStoreDetails(paymentInfo);

            }
        });


        pointsPayViewModel = ViewModelProviders.of(this, viewModelFactory).get(PointsPayViewModel.class);
        pointsPayViewModel.getPaymentStoreDetailsLiveData().observe(this, new Observer<GetPayeeCodeResponse>() {

            @Override
            public void onChanged(@Nullable GetPayeeCodeResponse getReceiptCodeResponse) {
                Log.i("-=-=-=-=-=GetPayeeCode", gson.toJson(getReceiptCodeResponse));
                if (getReceiptCodeResponse != null) {
                    // TODO: 2018/1/22  处理界面显示
                    String payeeStoreName = getReceiptCodeResponse.getPayeeCode().getPayeeStoreNamee();
                    payeeCode = getReceiptCodeResponse.getPayeeCode().getPayeeCode();//获取出来用来调用处理返还积分的接口
                    mTvStoreName.setText(Strings.isNullOrEmpty(payeeStoreName) ? "商铺" : payeeStoreName);
                }
            }
        });


        pointsPayViewModel.getPaymentLiveData().observe(this, new Observer<PreparePayerInappPaymentResponse>() {
            @Override
            public void onChanged(@Nullable PreparePayerInappPaymentResponse prepareMyPaymentResponse) {
                Log.i("-=-=-=-=-=Prepare", gson.toJson(prepareMyPaymentResponse));
                if (prepareMyPaymentResponse != null) {
                    // TODO: 2018/1/23 处理返还积分的

                }
            }
        });

        pointsPayViewModel.getCreatePaymentLiveData().observe(this, new Observer<PaymentResponse>() {
            @Override
            public void onChanged(@Nullable PaymentResponse paymentResponse) {
                Log.i("-=-=-=-=-=Create", gson.toJson(paymentResponse));
                if (paymentResponse != null && isPay) {
                    isPay = false;
                    PaymentChannel paymentChannel = paymentResponse.getPayment().getPaymentChannel();
                    String returnStr = paymentResponse.getPayment().getPaymentChannelSignature();
                    if (PaymentChannel.ALIPAY.name().equalsIgnoreCase(paymentChannel.name())) {
                        aliPay(returnStr);
                    } else if (PaymentChannel.WECHAT.name().equalsIgnoreCase(paymentChannel.name())) {
                        wechatPay(returnStr);
                    }
                }
            }
        });

    }

    /**
     * 支付宝支付
     *
     * @param
     */
    private void aliPay(String returnStr) {

        //调起支付
        AliPayUtil payUtil = new AliPayUtil(PointsPayActivity.this, new PayResultCallBack() {
            @Override
            public void OnSuccess(String orderInfo) {
                //成功
                Log.i("-=-=-=", "OnSuccess");
            }

            @Override
            public void OnDealing(String orderInfo) {
                //处理中
                Log.i("-=-=-=", "OnDealing");
            }

            @Override
            public void OnFail(int code, String error_message) {
                //失败
                Log.i("-=-=-=", "OnFail");
            }
        });

        String s = "app_id=2018011701924082&biz_content=%7B%22out_trade_no%22%3A2018012717132609731%2C%22total_amount%22%3A%221%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%7B%5C%22desc%5C%22%3A%5C%22%E6%B5%8B%E8%AF%95%5C%22%7D%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22passback_params%22%3A%22%7B%5C%22tradeType%5C%22%3A200%7D%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F192.168.1.200%2Fapi%2FalipayUrl&sign_type=RSA&timestamp=2018-01-27%2017%3A13%3A26&version=1.0&sign=bNU3tf331r0xCNJv9kaFsfZMMMcoSJloXPXUXL1fqVTz5XwjAsZ6f4ia%2FVlWZOKZ4UgzcjKl%2Fhq2YEZs9HAMNl2DZjZ4lTWrMn9OeaRnHgTmD3Vs0NldCmaFXHCIgQilEVIiW796xzyWAyhNAgOdZtHCqq3yDOwbIxPHSoJS54sSZyfGw3Ibd2DLYnfIJzPmnURCxHGrGWoxmmAMF4VwDC%2BlxwPhDsFwQeUxSwXJcFJ1qDleq64J6uyus2GFRPuknGzvq1K3lPyIfEZIS0PQJAt0VuSf5ChT%2BHzXBnU0UdAoBPIJ0EL6P9dDvxLhkzFgYmm4gMu%2BGLw1IjKS7AfhTA%3D%3D";

        payUtil.doAliPay(s);

    }

    /**
     * 微信支付
     *
     * @param signStr
     */
    private void wechatPay(String signStr) {
        WxPay.init(PointsPayActivity.this);
        WxPay.getInstance().doPay(signStr, new WxPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                //成功
                Log.i("-=-=-=", "OnSuccess");
            }

            @Override
            public void onError(int error_code) {
                //失败
                Log.i("-=-=-=", "onError");
            }

            @Override
            public void onCancel() {
                //取消
                Log.i("-=-=-=", "onCancel");
            }
        });
    }


    @OnClick({R.id.back, R.id.submit, R.id.use_points_desc, R.id.use_points, R.id.no_use_points, R.id.no_use_points_desc,
            R.id.ali_pay, R.id.weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.use_points_desc:
            case R.id.use_points:
                mCbUsePoints.setChecked(true);
                mCbNoUsePoints.setChecked(false);
                break;
            case R.id.no_use_points_desc:
            case R.id.no_use_points:
                mCbNoUsePoints.setChecked(true);
                mCbUsePoints.setChecked(false);
                break;
            case R.id.ali_pay:
                mIvAliBg.setVisibility(View.VISIBLE);
                mIvWeinxinBg.setVisibility(View.GONE);
                paymentChannelSelected = PaymentChannel.ALIPAY;
                break;
            case R.id.weixin:
                mIvAliBg.setVisibility(View.GONE);
                mIvWeinxinBg.setVisibility(View.VISIBLE);
                paymentChannelSelected = PaymentChannel.WECHAT;
                break;
            case R.id.submit:

                boolean isPointsPay = true;//是否使用积分支付
                if (mCbUsePoints.isChecked()) {
                    isPointsPay = true;
                } else if (mCbNoUsePoints.isChecked()) {
                    isPointsPay = false;
                }


                PaymentInfo paymentInfo = null;
                if (paymentChannelSelected == PaymentChannel.ALIPAY) {
                    paymentInfo = PaymentInfo.builder()
                            .setVoAccessToken(voAccessToken)
                            .setActualPay(1)
                            .setPayerIp(NetworkUtils.getIPAddress(true))
                            .setPayeeCode("qazwsxedc")
                            .setShouldPay(1)
                            .setPointsPay(0)
                            .setPointsPay(isPointsPay)
                            .setPaymentChannelName("1")//支付宝
                            .build();
                } else if (paymentChannelSelected == PaymentChannel.WECHAT) {
                    paymentInfo = PaymentInfo.builder()
                            .setVoAccessToken(voAccessToken)
                            .setActualPay(1)
                            .setPayerIp(NetworkUtils.getIPAddress(true))
                            .setPayeeCode("qazwsxedc")
                            .setShouldPay(1)
                            .setPointsPay(isPointsPay)
                            .setPointsPay(0)
                            .setPaymentChannelName("2")//微信
                            .build();
                }
                isPay = true;

                Log.i("-=-=-=-", (isPointsPay ? "使用积分" : "不适用积分") + "----微信还是支付宝:" + paymentChannelSelected);
                pointsPayViewModel.createPayment(paymentInfo);

                break;
        }
    }

}

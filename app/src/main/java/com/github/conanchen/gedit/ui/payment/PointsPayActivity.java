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
import com.github.conanchen.gedit.payment.common.grpc.PaymentResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.GetReceiptCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.PrepareMyPaymentResponse;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.util.pay.AliPayUtil;
import com.github.conanchen.gedit.util.pay.PayResultCallBack;
import com.github.conanchen.utils.vo.PaymentInfo;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.grpc.Status;

/**
 * 积分付款界面
 */
@Route(path = "/app/PointsPayActivity")
public class PointsPayActivity extends BaseActivity {

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

    private int selected = 1;//选择支付  1表示支付宝， 2 表示微信
    boolean isPay = false;//是否已经调起了支付
    private boolean isLogin = false;//是否登录
    private String accessToken;
    private String expiresIn;

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
                    String payeeReceiptCode = "oooooo";//服务器返回
                    pointsPayViewModel.getPayment(payeeReceiptCode);
                } else {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupViewModel() {
        pointsPayViewModel = ViewModelProviders.of(this, viewModelFactory).get(PointsPayViewModel.class);
        pointsPayViewModel.getPaymentStoreDetailsLiveData().observe(this, new Observer<GetReceiptCodeResponse>() {
            @Override
            public void onChanged(@Nullable GetReceiptCodeResponse getReceiptCodeResponse) {
                Log.i("-=-=-=-=-=", gson.toJson(getReceiptCodeResponse));
                if (getReceiptCodeResponse != null) {
                    // TODO: 2018/1/22  处理界面显示
                    String payeeStoreName = getReceiptCodeResponse.getReceiptCode().getPayeeStoreNamee();
                    mTvStoreName.setText(Strings.isNullOrEmpty(payeeStoreName) ? "商铺" : payeeStoreName);

                }
            }
        });
        pointsPayViewModel.getPaymentStoreDetails(code);

        pointsPayViewModel.getPaymentLiveData().observe(this, new Observer<PrepareMyPaymentResponse>() {
            @Override
            public void onChanged(@Nullable PrepareMyPaymentResponse prepareMyPaymentResponse) {
                Log.i("-=-=-=-=-=getPayment()", gson.toJson(prepareMyPaymentResponse));
                if (prepareMyPaymentResponse != null) {
                    // TODO: 2018/1/23 处理返还积分的

                }
            }
        });


        pointsPayViewModel.getCreatePaymentLiveData().observe(this, new Observer<PaymentResponse>() {
            @Override
            public void onChanged(@Nullable PaymentResponse paymentResponse) {
                Log.i("-=-=-=-=-=create()", gson.toJson(paymentResponse));
//                if (paymentResponse != null && isPay) {
//                    isPay = false;
//                    String channelOrderUuid = paymentResponse.getPayment().getChannelOrderUuid();
//                    if (selected == 1) {
//                        Log.i("-=-=-=-", "支付宝");
//                        aliPay(channelOrderUuid);
//                    } else if (selected == 2) {
//                        Log.i("-=-=-=-", "微信");
////                        weXinPay(channelOrderUuid);
//                    }
//                }
            }
        });


        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (Status.Code.OK.name().compareToIgnoreCase(signinResponse.getStatus().getCode()) == 0) {
                isLogin = true;
                accessToken = signinResponse.getAccessToken();
                expiresIn = signinResponse.getExpiresIn();
            } else {
                isLogin = false;
            }
        });
    }


    /**
     * 支付宝支付
     *
     * @param channelOrderUuid 订单号
     */
    private void aliPay(String channelOrderUuid) {

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

        String orderInfo = "app_id=2017051607257813&biz_content=%7B%22out_trade_no%22%3A2017120616103183710%2C%22total_amount%22%3A%220." +
                "01%22%2C%22subject%22%3A%220.01%22%2C%22body%22%3A%22%7B%5C%22desc%5C%22%3A%5C%22%E8%B4%AD%E4%B9%B0VIP%5C%22%7D%22%2C%22product" +
                "_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22passback_params%22%3A%22%7B%5C%22unit%5C%22%3A%5C%22%E5%A4%A9%5C%22%2C%5C%22payVipDay%5C%22%3A15%2C%5C%22" +
                "managerId%5C%22%3A1%2C%5C%22tradeType%5C%22%3A100%7D%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=" +
                "http%3A%2F%2F192.168.1.200%2Fapi%2FalipayUrl&sign_type=RSA&timestamp=2017-12-06%2016%3A10%3A31&version=1.0&sign=IM9v8BlHdwV1zWu6LFRm0Zea32KryyV1d5E4CCWZh4Xp7FogFkGbnDrIDuF" +
                "2lGcnAkszDweyQrN%2FcYiJA8BotpVzZ7%2FX9s6pEOpI5W%2F5GHbZ8UyhuZIUFUx%2FUVZjuqGn8jMnFhyjuZH5ipMCuw1GX%2Bnqq6MWVQabz4it20U%2FuUc%3D";//订单号

        payUtil.doAliPay(Strings.isNullOrEmpty(channelOrderUuid) ? orderInfo : channelOrderUuid);
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
                selected = 1;
                break;
            case R.id.weixin:
                mIvAliBg.setVisibility(View.GONE);
                mIvWeinxinBg.setVisibility(View.VISIBLE);
                selected = 2;
                break;
            case R.id.submit:
                int usePoints = 1;
                if (mCbUsePoints.isChecked()) {
                    usePoints = 1;
                } else if (mCbNoUsePoints.isChecked()) {
                    usePoints = 0;
                }


                VoAccessToken voAccessToken = VoAccessToken.builder()
                        .setAccessToken(Strings.isNullOrEmpty(accessToken) ? System.currentTimeMillis() + "" : accessToken)
                        .setExpiresIn(Strings.isNullOrEmpty(expiresIn) ? System.currentTimeMillis() + "" : expiresIn)
                        .build();

                PaymentInfo paymentInfo = null;
                if (selected == 1) {
                    paymentInfo = PaymentInfo.builder()
                            .setVoAccessToken(voAccessToken)
                            .setActualPay(1)
                            .setPayerIp(NetworkUtils.getIPAddress(true))
                            .setPayeeReceiptCode("qazwsxedc")
                            .setShouldPay(21)
                            .setPointsPay(10)
                            .setPayType("1")//支付宝
                            .build();
                } else if (selected == 2) {
                    paymentInfo = PaymentInfo.builder()
                            .setVoAccessToken(voAccessToken)
                            .setActualPay(1)
                            .setPayerIp(NetworkUtils.getIPAddress(true))
                            .setPayeeReceiptCode("qazwsxedc")
                            .setShouldPay(21)
                            .setPointsPay(10)
                            .setPayType("2")//微信
                            .build();

                }


                isPay = true;
                Log.i("-=-=-=-", "使不使用积分:" + usePoints + "----微信还是支付宝:" + selected);
                pointsPayViewModel.getCreatePayment(paymentInfo);

                break;
        }
    }

}

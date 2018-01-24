package com.github.conanchen.gedit.ui.payment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.payment.inapp.grpc.GetMyPayeeCodeResponse;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.utils.vo.PaymentInfo;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.grpc.Status;

@Route(path = "/app/PayeeQRCodeActivity")
public class PayeeQRCodeActivity extends BaseActivity {

    private Gson gson = new Gson();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PayeeQRCodeViewModel payeeQRCodeViewModel;
    private CurrentSigninViewModel currentSigninViewModel;

    @BindView(R.id.code)
    AppCompatImageView mIvQRCode;

    private VoAccessToken voAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payee);
        ButterKnife.bind(this);

        setupViewModel();
    }

    private void setupViewModel() {

        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (Status.Code.OK.name().compareToIgnoreCase(signinResponse.getStatus().getCode()) == 0) {
                voAccessToken = VoAccessToken.builder()
                        .setAccessToken(Strings.isNullOrEmpty(signinResponse.getAccessToken()) ? System.currentTimeMillis() + "" : signinResponse.getAccessToken())
                        .setExpiresIn(Strings.isNullOrEmpty(signinResponse.getExpiresIn()) ? System.currentTimeMillis() + "" : signinResponse.getExpiresIn())
                        .build();

                //获取商铺信息
                PaymentInfo paymentInfo = PaymentInfo.builder()
                        .setVoAccessToken(voAccessToken)
                        .setPayeeStoreUuid("test")
                        .build();

                payeeQRCodeViewModel.getQRCode(paymentInfo);

            }
        });


        payeeQRCodeViewModel = ViewModelProviders.of(this, viewModelFactory).get(PayeeQRCodeViewModel.class);
        payeeQRCodeViewModel.getStoreQRCodeLiveData().observe(this, new Observer<GetMyPayeeCodeResponse>() {
            @Override
            public void onChanged(@Nullable GetMyPayeeCodeResponse getMyReceiptCodeResponse) {
                Log.i("-=-=-=-=--", gson.toJson(getMyReceiptCodeResponse));
                if (getMyReceiptCodeResponse != null) {

                    String code = getMyReceiptCodeResponse.getPayeeCode().getPayeeCode();
                    String logo = getMyReceiptCodeResponse.getPayeeCode().getPayeeLogo();
                    Bitmap mBitmap = CodeUtils
                            .createImage(Strings.isNullOrEmpty(code) ? "wei huo qu dao sheng cheng de payCode" : code, 400, 400,
                                    Strings.isNullOrEmpty(logo) ? null : BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                    mIvQRCode.setImageBitmap(mBitmap);
                }
            }
        });

    }


    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}

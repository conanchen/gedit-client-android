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

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.payment.inapp.grpc.GetMyReceiptCodeResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayeeQRCodeActivity extends BaseActivity {

    private Gson gson = new Gson();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PayeeQRCodeViewModel payeeQRCodeViewModel;

    @BindView(R.id.code)
    AppCompatImageView mIvQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payee);
        ButterKnife.bind(this);

        setupViewModel();
    }

    private void setupViewModel() {
        payeeQRCodeViewModel = ViewModelProviders.of(this, viewModelFactory).get(PayeeQRCodeViewModel.class);
        payeeQRCodeViewModel.getStoreQRCodeLiveData().observe(this, new Observer<GetMyReceiptCodeResponse>() {
            @Override
            public void onChanged(@Nullable GetMyReceiptCodeResponse getMyReceiptCodeResponse) {
                Log.i("-=-=-=-=--", gson.toJson(getMyReceiptCodeResponse));
                if (getMyReceiptCodeResponse != null) {
                    Log.i("-=-=-=-=--进来了", gson.toJson(getMyReceiptCodeResponse));
                    String code = getMyReceiptCodeResponse.getReceiptCode().getCode();
                    String logo = getMyReceiptCodeResponse.getReceiptCode().getPayeeLogo();
                    logo = "http://sxd-moment.oss-cn-hangzhou.aliyuncs.com/advertisement/advertisement_hdpi.png";
                    Bitmap mBitmap = CodeUtils
                            .createImage(Strings.isNullOrEmpty(code) ? "you are a beautiful girl " : code, 400, 400,
                                    Strings.isNullOrEmpty(logo) ? null : BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                    mIvQRCode.setImageBitmap(mBitmap);
                }
            }
        });
        payeeQRCodeViewModel.getQRCode("test");
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

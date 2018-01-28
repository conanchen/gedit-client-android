package com.github.conanchen.gedit.ui.my;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/app/MyQRCodeActivity")
public class MyQRCodeActivity extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CurrentSigninViewModel currentSigninViewModel;

    @BindView(R.id.image_code)
    AppCompatImageView mIvQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode);
        ButterKnife.bind(this);

        setupViewModel();

    }

    private void setupViewModel() {
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        currentSigninViewModel.getMyProfile().observe(this, userProfileResponse -> {
            if (Status.Code.OK == userProfileResponse.getStatus().getCode()) {
                String uuid = userProfileResponse.getUserProfile().getUuid();

                Bitmap mBitmap = CodeUtils
                        .createImage(uuid, 400, 400,
                                null
//                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                        );
                mIvQRCode.setImageBitmap(mBitmap);
            }
        });
    }

    @OnClick({R.id.back, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                break;
        }
    }
}

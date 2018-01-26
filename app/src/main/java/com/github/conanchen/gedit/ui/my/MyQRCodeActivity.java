package com.github.conanchen.gedit.ui.my;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/app/MyQRCodeActivity")
public class MyQRCodeActivity extends AppCompatActivity {

    @BindView(R.id.image_code)
    AppCompatImageView mIvQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode);
        ButterKnife.bind(this);

        Bitmap mBitmap = CodeUtils
                .createImage("唯一标识生成的个人二维码，可以用来添加成为商铺员工", 400, 400,
                        null
//                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                );
        mIvQRCode.setImageBitmap(mBitmap);
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

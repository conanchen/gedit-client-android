package com.github.conanchen.gedit.ui.my.mystore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.github.conanchen.gedit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的店铺的二维码
 */
public class MyStoreCodeActivity extends AppCompatActivity {

    @BindView(R.id.image_code)
    AppCompatImageView mImageViewImageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store_code);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                //保存二维码图片
                break;
        }
    }
}

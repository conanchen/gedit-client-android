package com.github.conanchen.gedit.ui.my;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 客服
 */
@Route(path = "/app/CustomerServiceActivity")
public class CustomerServiceActivity extends AppCompatActivity {

    @BindView(R.id.tel)
    AppCompatTextView tel;
    @BindView(R.id.wei_xin)
    AppCompatTextView weiXin;
    @BindView(R.id.left_back)
    AppCompatImageView leftBack;
    @BindView(R.id.title)
    AppCompatTextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        title.setText("客服");

    }

    @OnClick({R.id.left_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
        }
    }
}

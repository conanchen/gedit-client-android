package com.github.conanchen.gedit.ui.store;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 付款界面
 */
@Route(path = "/app/PaymentActivity")
public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.left_back)
    AppCompatImageView leftBack;
    @BindView(R.id.title)
    AppCompatTextView title;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
    }

    @OnClick({R.id.left_back, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.submit:
                break;
        }
    }
}

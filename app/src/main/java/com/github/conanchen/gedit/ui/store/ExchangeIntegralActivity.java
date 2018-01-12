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
 * 积分兑换界面
 */
@Route(path = "/app/ExchangeIntegralActivity")
public class ExchangeIntegralActivity extends AppCompatActivity {

    @BindView(R.id.left_back)
    AppCompatImageView leftBack;
    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.edit_text_money)
    AppCompatEditText editTextMoney;
    @BindView(R.id.max_exchange_message)
    AppCompatTextView maxExchangeMessage;
    @BindView(R.id.exchange_all)
    AppCompatTextView exchangeAll;
    @BindView(R.id.ali)
    AppCompatTextView ali;
    @BindView(R.id.ali_cb)
    AppCompatCheckBox aliCb;
    @BindView(R.id.wei_xin)
    AppCompatTextView weiXin;
    @BindView(R.id.wei_xin_icon_cb)
    AppCompatCheckBox weiXinIconCb;
    @BindView(R.id.submit)
    AppCompatButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_integral);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
    }

    @OnClick({R.id.left_back, R.id.exchange_all, R.id.ali, R.id.ali_cb, R.id.wei_xin, R.id.wei_xin_icon_cb, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.exchange_all:
                //全部提现
                break;
            case R.id.ali:
                break;
            case R.id.ali_cb:
                break;
            case R.id.wei_xin:
                break;
            case R.id.wei_xin_icon_cb:
                break;
            case R.id.submit:
                //确认
                break;
        }
    }
}

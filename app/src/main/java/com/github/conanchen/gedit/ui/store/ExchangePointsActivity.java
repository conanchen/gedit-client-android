package com.github.conanchen.gedit.ui.store;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
@Route(path = "/app/ExchangePointsActivity")
public class ExchangePointsActivity extends AppCompatActivity {

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
    @BindView(R.id.ali_cb)
    AppCompatCheckBox aliCb;
    @BindView(R.id.ali)
    ConstraintLayout ali;
    @BindView(R.id.wei_xin_cb)
    AppCompatCheckBox weiXinCb;
    @BindView(R.id.wei_xin)
    ConstraintLayout weiXin;
    @BindView(R.id.submit)
    AppCompatButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_points);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
    }

    @OnClick({R.id.left_back, R.id.exchange_all, R.id.ali_cb, R.id.ali, R.id.wei_xin_cb, R.id.wei_xin, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.exchange_all:
                //全部兑换
                break;
            case R.id.ali_cb:
                //支付宝CheckBox
                break;
            case R.id.ali:
                //支付宝父控件
                break;
            case R.id.wei_xin_cb:
                //微信CheckBox
                break;
            case R.id.wei_xin:
                //微信
                break;
            case R.id.submit:
                //确认
                break;
        }
    }
}

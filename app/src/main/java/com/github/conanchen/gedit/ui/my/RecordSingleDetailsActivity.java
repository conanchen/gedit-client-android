package com.github.conanchen.gedit.ui.my;

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
 * 录单详情界面
 */
@Route(path = "/app/RecordSingleDetailsActivity")
public class RecordSingleDetailsActivity extends AppCompatActivity {

    @BindView(R.id.left_back)
    AppCompatImageView leftBack;
    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.consumption_money)
    AppCompatEditText consumptionMoney;
    @BindView(R.id.business_discount)
    AppCompatEditText businessDiscount;
    @BindView(R.id.business_need_pay)
    AppCompatEditText businessNeedPay;
    @BindView(R.id.use_explain)
    AppCompatTextView useExplain;
    @BindView(R.id.wallet_cb)
    AppCompatCheckBox walletCb;
    @BindView(R.id.ali_cb)
    AppCompatCheckBox aliCb;
    @BindView(R.id.wei_xin_cb)
    AppCompatCheckBox weiXinCb;
    @BindView(R.id.submit)
    AppCompatButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_single_details);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        title.setText("录单详情");

    }

    @OnClick({R.id.left_back, R.id.wallet, R.id.wallet_cb, R.id.ali, R.id.ali_cb, R.id.wei_xin, R.id.wei_xin_cb, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.wallet:
                break;
            case R.id.wallet_cb:
                break;
            case R.id.ali:
                break;
            case R.id.ali_cb:
                break;
            case R.id.wei_xin:
                break;
            case R.id.wei_xin_cb:
                break;
            case R.id.submit:
                break;
        }
    }
}

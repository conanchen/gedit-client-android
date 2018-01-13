package com.github.conanchen.gedit.ui.my;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/app/IntegralActivity")
public class IntegralActivity extends AppCompatActivity {

    @BindView(R.id.left_back)
    AppCompatImageView leftBack;
    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.buy)
    AppCompatTextView buy;
    @BindView(R.id.today_add_integral)
    AppCompatTextView todayAddIntegral;
    @BindView(R.id.can_consumption_integral)
    AppCompatTextView canConsumptionIntegral;
    @BindView(R.id.can_exchange_integral)
    AppCompatTextView canExchangeIntegral;
    @BindView(R.id.to_integral_exchange_desc)
    AppCompatTextView toIntegralExchangeDesc;
    @BindView(R.id.can_consumption_integral_info_desc)
    AppCompatTextView canConsumptionIntegralInfoDesc;
    @BindView(R.id.can_exchange_integral_info_desc)
    AppCompatTextView canExchangeIntegralInfoDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

    }

    @OnClick({R.id.left_back, R.id.buy, R.id.to_integral_exchange_desc, R.id.can_consumption_integral_info_desc, R.id.can_exchange_integral_info_desc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.buy:
                //去购买积分
                break;
            case R.id.to_integral_exchange_desc:
                //跳转到积分兑换界面
                ARouter.getInstance().build("/app/ExchangeIntegralActivity").navigation();
                break;
            case R.id.can_consumption_integral_info_desc:
                //可消费积分明细
                ARouter.getInstance().build("/app/ConsumptionIntegralDetailsActivity").navigation();
                break;
            case R.id.can_exchange_integral_info_desc:
                //可兑换积分明细
                ARouter.getInstance().build("/app/ExchangeIntegralDetailsActivity").navigation();
                break;
        }
    }
}

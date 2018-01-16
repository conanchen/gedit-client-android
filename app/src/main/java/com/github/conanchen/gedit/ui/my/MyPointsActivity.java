package com.github.conanchen.gedit.ui.my;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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

@Route(path = "/app/MyPointsActivity")
public class MyPointsActivity extends AppCompatActivity {


    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.left_back)
    AppCompatImageView leftBack;
    @BindView(R.id.right_text)
    AppCompatTextView rightText;
    @BindView(R.id.today_add_integral)
    AppCompatTextView todayAddIntegral;
    @BindView(R.id.can_consumption_integral)
    AppCompatTextView canConsumptionIntegral;
    @BindView(R.id.can_exchange_integral)
    AppCompatTextView canExchangeIntegral;
    @BindView(R.id.exchange_points)
    ConstraintLayout exchangePoints;
    @BindView(R.id.can_consumption_points)
    ConstraintLayout canConsumptionPoints;
    @BindView(R.id.can_exchange_record)
    ConstraintLayout canExchangeRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.back, R.id.right, R.id.exchange_points, R.id.can_consumption_points, R.id.can_exchange_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                //购买
                break;
            case R.id.exchange_points:
                //跳转到积分兑换界面
                ARouter.getInstance().build("/app/ExchangePointsActivity").navigation();
                break;
            case R.id.can_consumption_points:
                //可消费积分明细
                ARouter.getInstance().build("/app/ConsumptionPointsDetailsActivity").navigation();
                break;
            case R.id.can_exchange_record:
                //可兑换积分明细
                ARouter.getInstance().build("/app/ExchangePointsDetailsActivity").navigation();
                break;
        }
    }

}

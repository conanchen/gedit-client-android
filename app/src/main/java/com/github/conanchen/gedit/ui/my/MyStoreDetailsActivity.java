package com.github.conanchen.gedit.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的店铺详情
 */
@Route(path = "/app/MyStoreDetailsActivity")
public class MyStoreDetailsActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.banner)
    RelativeLayout banner;
    @BindView(R.id.total_money)
    AppCompatTextView totalMoney;
    @BindView(R.id.today_money)
    AppCompatTextView todayMoney;
    @BindView(R.id.total_integral)
    AppCompatTextView totalIntegral;
    @BindView(R.id.today_integral)
    AppCompatTextView todayIntegral;
    @BindView(R.id.my_employees)
    ConstraintLayout myEmployees;
    @BindView(R.id.code)
    ConstraintLayout code;
    @BindView(R.id.address_desc)
    AppCompatTextView addressDesc;
    @BindView(R.id.address)
    ConstraintLayout address;
    @BindView(R.id.phone_desc)
    AppCompatTextView phoneDesc;
    @BindView(R.id.phone)
    ConstraintLayout phone;
    @BindView(R.id.time_desc)
    AppCompatTextView timeDesc;
    @BindView(R.id.time)
    ConstraintLayout time;
    @BindView(R.id.prompt_desc)
    AppCompatTextView promptDesc;
    @BindView(R.id.prompt)
    ConstraintLayout prompt;
    @BindView(R.id.store_introduce)
    AppCompatTextView storeIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store_details);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

    }


    @OnClick({R.id.back, R.id.my_employees, R.id.code, R.id.address, R.id.phone, R.id.time, R.id.prompt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.my_employees:
                //我的员工
                startActivity(new Intent(MyStoreDetailsActivity.this, MyEmployeesActivity.class));
                break;
            case R.id.code:
                //二维码
                break;
            case R.id.address:
                //地址
                break;
            case R.id.phone:
                //电话
                break;
            case R.id.time:
                //营业时间和关闭时间
                break;
            case R.id.prompt:
                //优惠描述
                break;
        }
    }
}

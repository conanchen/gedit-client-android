package com.github.conanchen.gedit.ui.store;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
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

    @BindView(R.id.left_back)
    AppCompatImageView leftBack;
    @BindView(R.id.title)
    AppCompatTextView title;
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
    AppCompatTextView myEmployees;
    @BindView(R.id.code)
    AppCompatTextView code;
    @BindView(R.id.address)
    AppCompatTextView address;
    @BindView(R.id.phone)
    AppCompatTextView phone;
    @BindView(R.id.time)
    AppCompatTextView time;
    @BindView(R.id.prompt)
    AppCompatTextView prompt;
    @BindView(R.id.store_introduce)
    AppCompatTextView storeIntroduce;
    @BindView(R.id.delete)
    AppCompatTextView delete;
    @BindView(R.id.modify)
    AppCompatTextView modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store_details);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
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
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
 * 商家详情
 */
@Route(path = "/app/BusinessDetailsActivity")
public class StoreDetailsActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.banner)
    RelativeLayout banner;
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
    @BindView(R.id.address_icon)
    AppCompatImageView addressIcon;
    @BindView(R.id.phone_icon)
    AppCompatImageView phoneIcon;
    @BindView(R.id.time_icon)
    AppCompatImageView timeIcon;
    @BindView(R.id.prompt_icon)
    AppCompatImageView promptIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}

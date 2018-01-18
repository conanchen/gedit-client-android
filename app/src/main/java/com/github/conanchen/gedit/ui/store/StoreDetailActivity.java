package com.github.conanchen.gedit.ui.store;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商家详情
 */
@Route(path = "/app/StoreDetailActivity")
public class StoreDetailActivity extends AppCompatActivity {
    private static String   TAG = StoreDetailActivity.class.getSimpleName();
    @Autowired
    public String uuid; //passed from ARouter

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
        ARouter.getInstance().inject(this);

        // ARouter会自动对字段进行赋值，无需主动获取
        Log.d(TAG,String.format("ARouter got param uuid=%s",uuid));

        setContentView(R.layout.activity_store_detail);
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

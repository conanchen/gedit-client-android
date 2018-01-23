package com.github.conanchen.gedit.ui.my.mystore;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
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
 * 我的店铺详情
 */
@Route(path = "/app/MyStoreActivity")
public class MyStoreActivity extends AppCompatActivity {

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
    @BindView(R.id.prompt)
    ConstraintLayout prompt;
    @BindView(R.id.store_introduce)
    AppCompatTextView storeIntroduce;


    @Autowired
    public String uuid;//从我的店铺详情传递过来

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);

    }


    @OnClick({R.id.back, R.id.banner, R.id.my_employees, R.id.code, R.id.address, R.id.phone, R.id.time, R.id.prompt,
            R.id.store_introduce})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.banner:
                //如果没有图片就去修改门店展示的图片
                ARouter.getInstance()
                        .build("/app/StoreUpdateStoreDisplayActivity")
                        .withString("uuid", uuid)
                        .navigation();
                break;
            case R.id.my_employees:
                //我的员工
                ARouter.getInstance().build("/app/MyStoreEmployeesActivity").navigation();
                break;
            case R.id.code:
                //二维码
                ARouter.getInstance().build("/app/MyStoreCodeActivity").navigation();
                break;
            case R.id.address:
                //地址
                ARouter.getInstance()
                        .build("/app/StoreUpdateActivity")
                        .withString("MODIFY_TYPE", "DETAIL_ADDRESS")
                        .withString("uuid", uuid).navigation();
                break;
            case R.id.phone:
                //电话
                ARouter.getInstance()
                        .build("/app/StoreUpdateActivity")
                        .withString("MODIFY_TYPE", "PHONE")
                        .withString("uuid", uuid)
                        .navigation();
                break;
            case R.id.time:
                //营业时间和关闭时间
                break;
            case R.id.prompt:
                //优惠描述
                ARouter.getInstance()
                        .build("/app/StoreUpdateActivity")
                        .withString("MODIFY_TYPE", "POINTS_RATE")
                        .withString("uuid", uuid)
                        .navigation();
                break;
            case R.id.store_introduce:
                //商铺描述
                ARouter.getInstance()
                        .build("/app/StoreUpdateActivity")
                        .withString("MODIFY_TYPE", "DESC")
                        .withString("uuid", uuid)
                        .navigation();
                break;
        }
    }
}

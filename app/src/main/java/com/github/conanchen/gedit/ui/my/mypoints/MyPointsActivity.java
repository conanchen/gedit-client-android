package com.github.conanchen.gedit.ui.my.mypoints;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragmentActivity;
import com.github.conanchen.gedit.room.my.accounting.Account;
import com.github.conanchen.gedit.ui.store.ExchangePointsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/app/MyPointsActivity")
public class MyPointsActivity extends BaseFragmentActivity {

    private final static String TAG = MyPointsActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    MyPointsViewModel myPointsViewModel;

    @BindView(R.id.today_added_points)
    AppCompatTextView today_added_points;


    @BindView(R.id.can_exchange_points_desc)
    AppCompatTextView can_exchange_points_desc;

    @BindView(R.id.can_consumption_points)
    AppCompatTextView can_consumption_points;


    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<Fragment> viewPagerList = new ArrayList<>();

    private MyPointsViewPagerAdapter mAdapter;//viewPager的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        ButterKnife.bind(this);

        setupViewModel();
        setViewPager();
    }

    private void setViewPager() {
        viewPagerList.add(new TodayAddedPointsFragment());
        viewPagerList.add(new CanConsumptionPointsFragment());
        viewPagerList.add(new CanExchangePointsFragment());
        mAdapter = new MyPointsViewPagerAdapter(getSupportFragmentManager(), viewPagerList);
        viewPager.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        myPointsViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyPointsViewModel.class);

        myPointsViewModel.getMyAccountListLiveData().observe(this, accounts -> {
            if (accounts != null && accounts.size() > 0) {
                for (int i = 0; i < accounts.size(); i++) {
                    Account account = accounts.get(i);
                    switch (account.type) {
                        case "CASH":
                            break;
                        case "LIVE_POINTS"://可消费积分
                            today_added_points.setText(account.currentChanges);
                            can_consumption_points.setText(account.currentBalance);
                            break;
                        case "FIXED_POINTS"://可兑换积分
                            can_exchange_points_desc.setText(account.currentBalance);
                            break;
                        default:
                            Log.e(TAG, "ERROR Unknown Type:" + account.type);
                    }
                }
            }
        });
    }

    @OnClick({R.id.back, R.id.right,
            R.id.today_added_points,R.id.today_added_points_desc ,
            R.id.can_consumption_points,R.id.can_consumption_points_desc,
            R.id.can_exchange_points,R.id.can_exchange_points_desc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                //兑换
                startActivity(new Intent(MyPointsActivity.this, ExchangePointsActivity.class));
                break;
            case R.id.today_added_points:
            case R.id.today_added_points_desc:
                //今日新增列表
                viewPager.setCurrentItem(0);
                break;
            case R.id.can_consumption_points:
            case R.id.can_consumption_points_desc:
                //可消费积分列表
                viewPager.setCurrentItem(1);
                break;
            case R.id.can_exchange_points:
            case R.id.can_exchange_points_desc:
                //可兑换积分列表
                viewPager.setCurrentItem(2);
                break;
        }
    }

}

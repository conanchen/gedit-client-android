package com.github.conanchen.gedit.ui.my.myfans;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的粉丝列表界面
 */
@Route(path = "/app/MyFansActivity")
public class MyFansActivity extends BaseActivity {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    MyFansViewModel myFansViewModel;



    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyFansAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans_list);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupViewModel();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyFansAdapter();
        recyclerView.setAdapter(mAdapter);


    }

    private void setupViewModel() {
        myFansViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyFansViewModel.class);
        myFansViewModel.getMyFanshipPagedListLiveData().observe(this, stores -> {
            if (stores != null) {
                mAdapter.setList(stores);
            }
        });
        myFansViewModel.refresh();
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

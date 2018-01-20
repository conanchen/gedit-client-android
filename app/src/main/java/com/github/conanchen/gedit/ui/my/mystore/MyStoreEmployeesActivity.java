package com.github.conanchen.gedit.ui.my.mystore;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Location;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.room.store.Store;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的员工列表界面
 */
public class MyStoreEmployeesActivity extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    MyStoreEmployeesViewModel myStoreEmployeesViewModel;

    @BindView(R.id.back)
    AppCompatImageButton back;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyStoreEmployeesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_employees);
        ButterKnife.bind(this);
        setupRecyclerView();
        setupViewModel();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyStoreEmployeesAdapter();
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyStoreEmployeesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Store store) {
                Toast.makeText(MyStoreEmployeesActivity.this, "员工详情", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupViewModel() {
        myStoreEmployeesViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyStoreEmployeesViewModel.class);
        myStoreEmployeesViewModel.getLiveStores().observe(this, stores -> {
            if (stores != null) {
                mAdapter.setList(stores);
            }
        });

        myStoreEmployeesViewModel.updateLocation(Location.newBuilder().setLat(1).setLon(2).build());
    }

    @OnClick(R.id.back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}

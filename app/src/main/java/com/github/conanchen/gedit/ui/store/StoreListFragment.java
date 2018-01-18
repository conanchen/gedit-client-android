package com.github.conanchen.gedit.ui.store;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.github.conanchen.amap.livelocation.AmapLiveLocation;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.vo.Location;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Conan Chen on 2018/1/9.
 */

public class StoreListFragment extends BaseFragment implements Injectable, StoreListAdapter.OnItemClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private StoreListViewModel storeListViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private StoreListAdapter mAdapter;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, null);
        ButterKnife.bind(this, view);

        setupRecyclerView();
        setupViewModel();
        return view;
    }


    private void setupViewModel() {
        storeListViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreListViewModel.class);
        storeListViewModel.getStorePagedListLiveData().observe(this, stores -> {
            if (stores != null) {
                mAdapter.setList(stores);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        AmapLiveLocation amapLiveLocation = AmapLiveLocation.builder().setContext(this.getContext()).build();
        Dexter.withActivity(this.getActivity())
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Snackbar.make(view, "访问位置权限打开了，测试定位,请等待HelloWorld的变化。", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        amapLiveLocation.locate().observe(StoreListFragment.this, aMapLocation -> {
                            String text = String.format("Test now:%s@(lat,lon)=(%f,%f) address=%s",
                                    DateFormat.getTimeInstance().format(new Date()),
                                    aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation.getAddress());
                            Snackbar.make(recyclerView, text, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            if (aMapLocation.getLatitude() > 0 && aMapLocation.getLongitude() > 0) {
                                storeListViewModel.updateLocation(Location.builder()
                                        .setLat(aMapLocation.getLatitude())
                                        .setLon(aMapLocation.getLongitude()).build());
                            }
                        });
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(view, "访问位置权限打开才能定位哦，打开定位权限吧。", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }).onSameThread()
                .check();

    }

    /**
     * 设置recyclerView
     */
    private void setupRecyclerView() {
        mAdapter = new StoreListAdapter();
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }


    /**
     * 列表条目的点击事件
     *
     * @param store
     */
    @Override
    public void OnItemClick(Store store) {
        startActivity(new Intent(getContext(), StoreDetailsActivity.class));
    }

}

package com.github.conanchen.gedit.ui.my.mypoints;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Location;
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/20.
 */

public class TodayAddPointsFragment extends BaseFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    TodayAddPointsFragmentViewModel todayAddPointsFragmentViewModel;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private TodayAddPointsAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_add_points, null);
        ButterKnife.bind(this, view);

        setupRecyclerView();
        setupViewModel();
        return view;
    }

    private void setupRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(manager);
        mAdapter = new TodayAddPointsAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private void setupViewModel() {

        todayAddPointsFragmentViewModel = ViewModelProviders.of(this, viewModelFactory).get(TodayAddPointsFragmentViewModel.class);
        todayAddPointsFragmentViewModel.getStorePagedListLiveData().observe(this, stores -> {
            mAdapter.setList(stores);
        });

        todayAddPointsFragmentViewModel.updateLocation(Location.newBuilder().setLon(1.0).setLat(2.0).build());
    }

}

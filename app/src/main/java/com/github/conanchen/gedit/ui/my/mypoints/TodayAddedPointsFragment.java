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
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/20.
 */

public class TodayAddedPointsFragment extends BaseFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    TodayAddedPointsFragmentViewModel todayAddedPointsFragmentViewModel;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private TodayAddedPointsAdapter mAdapter;


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
        mAdapter = new TodayAddedPointsAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private void setupViewModel() {

        todayAddedPointsFragmentViewModel = ViewModelProviders.of(this, viewModelFactory).get(TodayAddedPointsFragmentViewModel.class);
        todayAddedPointsFragmentViewModel.getTodayPostingPagedListLiveData().observe(this, postings -> {
            mAdapter.setList(postings);
        });

        todayAddedPointsFragmentViewModel.refresh();
    }

}

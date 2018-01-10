package com.github.conanchen.gedit.ui.my;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;
import com.github.conanchen.gedit.ui.store.StoreCreateActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Conan Chen on 2018/1/9.
 */

public class MyFragment extends BaseFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private MyViewModel viewModel;

    @BindView(R.id.myviewmodeltext)
    AppCompatTextView myviewmodeltext;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyViewModel.class);
        myviewmodeltext.setText(String.format("MyViewModel is injected ? viewModel=%s", viewModel));
        viewModel.setHelloName("set name");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, view);

        return view;
    }


    @OnClick(R.id.myviewmodeltext)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), StoreCreateActivity.class));
    }
}

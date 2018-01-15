package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Conan Chen on 2018/1/9.
 */

public class StoreListFragment extends BaseFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.left_back)
    AppCompatImageView leftBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private StoreListViewModel storeListViewModel;
    private StoreListAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        storeListViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreListViewModel.class);
        storeListViewModel.getLiveStores().observe(this, stores -> {
            Log.i("getLiveStores().observe", "stores是不是为空" + stores);
            if (stores != null) {
                mAdapter.setList(stores);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, null);
        ButterKnife.bind(this, view);

        title.setText("首页");
        setupRecyclerView();

        return view;
    }


    /**
     * 设置recyclerView
     */
    private void setupRecyclerView() {
        mAdapter = new StoreListAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mAdapter);

    }


    @OnClick(R.id.hellobutton)
        public void openHelloButtonClicked() {
            // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
            ARouter.getInstance().build("/app/HelloActivity").navigation();
    //        startActivity(new Intent(this.getContext(), StoreCreateActivity.class));
        }

    @OnClick(R.id.createbutton)
    public void openCreateStoreButtonClicked() {
        // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
        ARouter.getInstance().build("/app/StoreCreateActivity").navigation();
    }
    /*@OnClick({R.id.login, R.id.registe, R.id.my_store_details, R.id.my_extension_store_details, R.id.others_store_details,
            R.id.record_single_list, R.id.record_single_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                //登录界面
                ARouter.getInstance().build("/app/LoginActivity").navigation();
                break;
            case R.id.registe:
                //注册界面
                ARouter.getInstance().build("/app/RegisterActivity").navigation();
                break;
            case R.id.my_store_details:
                //我的店铺详情
                ARouter.getInstance().build("/app/MyStoreDetailsActivity").navigation();
                break;
            case R.id.my_extension_store_details:
                //代理的店铺详情
                ARouter.getInstance().build("/app/ExtensionStoreDetailsActivity").navigation();
                break;
            case R.id.others_store_details:
                //其他店铺详情
                ARouter.getInstance().build("/app/BusinessDetailsActivity").navigation();
                break;
            case R.id.record_single_list:
                //录单列表
                ARouter.getInstance().build("/app/RecordSingleListActivity").navigation();
                break;
            case R.id.record_single_details:
                //录单列表
                ARouter.getInstance().build("/app/RecordSingleDetailsActivity").navigation();
                break;
        }
    }*/
}

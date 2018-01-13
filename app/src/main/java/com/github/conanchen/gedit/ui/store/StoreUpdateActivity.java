package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.hello.grpc.store.StoreUpdateInfo;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/app/StoreUpdateActivity")
public class StoreUpdateActivity extends BaseActivity {

    private String TAG = StoreUpdateActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.name_edit)
    AppCompatEditText nameEdit;
    @BindView(R.id.address_text)
    AppCompatTextView address_text;
    private StoreUpdateViewModel storeUpdateViewModel;
    private static final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_update);
        ButterKnife.bind(this);
        setupViewModel();
    }

    private void setupViewModel() {
        storeUpdateViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreUpdateViewModel.class);
        storeUpdateViewModel.getStoreUpdateResponseLiveData()
                .observe(this, storeUpdateResponse -> {
                    String message = String.format("storeUpdateResponse=%s", gson.toJson(storeUpdateResponse));
                    Log.i(TAG, message);
                    if (storeUpdateResponse != null) {
                        address_text.setText(message);
                    }
                });
    }

    @OnClick(R.id.updatebutton)
    public void setUpdatebuttonClick() {
        String name = nameEdit.getText().toString().trim();
        //获取uuid   传入数据库查询 ?? 为什么要到数据库查询，
        String uuid = "1";
        StoreUpdateInfo storeUpdateInfo = StoreUpdateInfo.builder()
                .setName(StoreUpdateInfo.Field.NAME)
                .setValue(name)
                .setUuid(uuid)
                .build();
        storeUpdateViewModel.updateStoreWith(storeUpdateInfo);
    }
}

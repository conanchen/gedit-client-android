package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;

import javax.inject.Inject;

public class StoreUpdateAddressActivity extends BaseActivity {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    StoreUpdateAddressViewModel storeUpdateAddressViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sotre_update_address);
        setupViewModel();
    }

    private void setupViewModel() {
        storeUpdateAddressViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreUpdateAddressViewModel.class);

    }
}

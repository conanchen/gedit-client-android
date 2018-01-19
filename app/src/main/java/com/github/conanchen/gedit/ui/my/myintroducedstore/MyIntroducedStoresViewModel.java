package com.github.conanchen.gedit.ui.my.myintroducedstore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.MyIntroducedStoreRepository;
import com.github.conanchen.gedit.room.my.store.MyIntroducedStore;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyIntroducedStoresViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<Long> timeMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<MyIntroducedStore>> myIntroducedStorePagedListLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public MyIntroducedStoresViewModel(MyIntroducedStoreRepository myIntroducedStoreRepository) {
        myIntroducedStorePagedListLiveData = Transformations.switchMap(timeMutableLiveData, location -> {
            if (location == null) {
                return AbsentLiveData.create();
            } else {
                return myIntroducedStoreRepository.loadMyIntroducedStores(location);
            }
        });
    }

    @VisibleForTesting
    public void refresh() {
        timeMutableLiveData.setValue(System.currentTimeMillis());
    }

    @VisibleForTesting
    public LiveData<PagedList<MyIntroducedStore>> getMyIntroducedStorePagedListLiveData() {
        return myIntroducedStorePagedListLiveData;
    }
}

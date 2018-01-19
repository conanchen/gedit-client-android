package com.github.conanchen.gedit.ui.my.myfans;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.MyFansRepository;
import com.github.conanchen.gedit.repository.MyIntroducedStoreRepository;
import com.github.conanchen.gedit.room.my.fan.Fanship;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyFansViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<Long> timeMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<Fanship>> myFanshipPagedListLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public MyFansViewModel(MyFansRepository myIntroducedStoreRepository) {
        myFanshipPagedListLiveData = Transformations.switchMap(timeMutableLiveData, time -> {
            if (time == null) {
                return AbsentLiveData.create();
            } else {
                return myIntroducedStoreRepository.loadMyFanships(time);
            }
        });
    }

    @VisibleForTesting
    public void refresh() {
        timeMutableLiveData.setValue(System.currentTimeMillis());
    }

    @VisibleForTesting
    public LiveData<PagedList<Fanship>> getMyFanshipPagedListLiveData() {
        return myFanshipPagedListLiveData;
    }
}

package com.github.conanchen.gedit.ui.my.myfans;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.MyFansRepository;
import com.github.conanchen.gedit.room.my.fan.Fanship;
import com.github.conanchen.gedit.user.fans.grpc.FanshipResponse;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.utils.vo.MyFansBean;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyFansViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<MyFansBean> timeMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<Fanship>> myFanshipPagedListLiveData;


    @VisibleForTesting
    final MutableLiveData<MyFansBean> addFansMutableLiveData = new MutableLiveData<>();
    private final LiveData<FanshipResponse> addFansPagedListLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public MyFansViewModel(MyFansRepository myIntroducedStoreRepository) {
        myFanshipPagedListLiveData = Transformations.switchMap(timeMutableLiveData, myFansBean -> {
            if (myFansBean == null) {
                return AbsentLiveData.create();
            } else {
                return myIntroducedStoreRepository.loadMyFanships(myFansBean);
            }
        });


        addFansPagedListLiveData = Transformations.switchMap(addFansMutableLiveData, myFansBean -> {
            if (myFansBean == null) {
                return AbsentLiveData.create();
            } else {
                return myIntroducedStoreRepository.addFans(myFansBean);
            }
        });
    }

    @VisibleForTesting
    public void refresh(MyFansBean myFansBean) {
        timeMutableLiveData.setValue(myFansBean);
    }

    @VisibleForTesting
    public LiveData<PagedList<Fanship>> getMyFanshipPagedListLiveData() {
        return myFanshipPagedListLiveData;
    }

    @VisibleForTesting
    public void add(MyFansBean myFansBean) {
        addFansMutableLiveData.setValue(myFansBean);
    }

    @VisibleForTesting
    public LiveData<FanshipResponse> addPagedListLiveData() {
        return addFansPagedListLiveData;
    }
}

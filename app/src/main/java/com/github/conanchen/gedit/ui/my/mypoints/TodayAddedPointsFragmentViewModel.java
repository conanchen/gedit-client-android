package com.github.conanchen.gedit.ui.my.mypoints;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.AccountingRepository;
import com.github.conanchen.gedit.room.my.accounting.Posting;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/20.
 */

public class TodayAddedPointsFragmentViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<Long> timeMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<Posting>> todayPostingPagedListLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public TodayAddedPointsFragmentViewModel(AccountingRepository accountingRepository) {
        todayPostingPagedListLiveData = Transformations.switchMap(timeMutableLiveData, time -> {
            if (time == null) {
                return AbsentLiveData.create();
            } else {
                return accountingRepository.loadMyPointsByDate("000001",time);
            }
        });
    }

    @VisibleForTesting
    public void refresh() {
        timeMutableLiveData.setValue(System.currentTimeMillis());
    }

    @VisibleForTesting
    public LiveData<PagedList<Posting>> getTodayPostingPagedListLiveData() {
        return todayPostingPagedListLiveData;
    }

}

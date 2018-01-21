package com.github.conanchen.gedit.ui.my.mypoints;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.AccountingRepository;
import com.github.conanchen.gedit.room.my.accounting.Account;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/20.
 */

public class MyPointsViewModel extends ViewModel{
    @VisibleForTesting
    final MutableLiveData<Long> lastUpdatedMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<Account>> myAccountListLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public MyPointsViewModel(AccountingRepository accountingRepository) {
        myAccountListLiveData = Transformations.switchMap(lastUpdatedMutableLiveData, times -> {
            if (times == null) {
                return AbsentLiveData.create();
            } else {
                return accountingRepository.loadMyAccounts();
            }
        });
    }

    @VisibleForTesting
    public void loadMyAccounts(Long times) {
        lastUpdatedMutableLiveData.setValue(times);
    }

    @VisibleForTesting
    public LiveData<PagedList<Account>> getMyAccountListLiveData() {
        return myAccountListLiveData;
    }
}

package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.github.conanchen.gedit.accounting.account.grpc.ListMyAccountRequest;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.my.accounting.Account;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Conan Chen on 2018/1/8.
 */
@Singleton
public class AccountingRepository {
    private final static String TAG = AccountingRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public AccountingRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }

    private static final PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
            .setEnablePlaceholders(true)
            .setPrefetchDistance(10)
            .setPageSize(20).build();


    public LiveData<PagedList<Account>> loadMyAccounts() {
        return (new LivePagedListBuilder(roomFascade.daoAccount.listLivePagedMyAccounts(), pagedListConfig))
                .setBoundaryCallback(new PagedList.BoundaryCallback() {
                    @Override
                    public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
                        Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
                            grpcFascade.accountService.downloadMyAccounts(ListMyAccountRequest.newBuilder().setLastUpdated(System.currentTimeMillis()).build(), response -> {
                                Account myStore = Account.builder()
                                        .setUuid(response.getAccount().getUuid())
                                        .setType(response.getAccount().getType().name())
                                        .setCurrentBalance(response.getAccount().getCurrentBalance())
                                        .setCurrentChanges(response.getAccount().getCurrentChanges())
                                        .build();
                                roomFascade.daoAccount.save(myStore);
                            });
                        });
                    }
                })
                .build();
    }
}

package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.github.conanchen.gedit.accounting.account.grpc.ListMyAccountRequest;
import com.github.conanchen.gedit.accounting.posting.grpc.ListMyPostingRequest;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.my.accounting.Account;
import com.github.conanchen.gedit.room.my.accounting.Posting;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.Calendar;

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
        return (new LivePagedListBuilder<Integer, Account>(roomFascade.daoAccount.listLivePagedMyAccounts(), pagedListConfig))
                .setBoundaryCallback(new PagedList.BoundaryCallback<Account>() {
                    @Override
                    public void onItemAtEndLoaded(@NonNull Account itemAtEnd) {
                        Observable.just(true).subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(aBoolean -> {
                                    grpcFascade.accountService.downloadMyAccounts(
                                            ListMyAccountRequest.newBuilder()
                                                    .setLastUpdated(System.currentTimeMillis())
                                                    .build(),
                                            response -> {
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


    public LiveData<PagedList<Posting>> loadMyPointsByDate(String accountId, Long time) {
        // Use the Calendar class to subtract one day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Long start = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, +2);
        Long end = calendar.getTimeInMillis();

        return (new LivePagedListBuilder<Integer, Posting>(roomFascade.daoPosting.listLivePagedMyPostingByDate(accountId,start,end), pagedListConfig))
                .setBoundaryCallback(new PagedList.BoundaryCallback<Posting>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        downloadMyPointsByDate(time);
                    }

                    @Override
                    public void onItemAtEndLoaded(@NonNull Posting itemAtEnd) {
                        downloadMyPointsByDate(time);
                    }

                })
                .build();
    }

    private void downloadMyPointsByDate(Long time) {
        Observable.just(true).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    grpcFascade.postingService.downloadMyPostings(
                            ListMyPostingRequest.newBuilder()
                                    .setLastUpdated(System.currentTimeMillis())
                                    .build(),
                            response -> {
                                Posting myStore = Posting.builder()
                                        .setUuid(response.getPosting().getUuid())
                                        .setAccountUuid(response.getPosting().getAccountUuid())
                                        .setAmount(response.getPosting().getAmount())
                                        .setComment(response.getPosting().getComment())
                                        .setCreated(response.getPosting().getCreated())
                                        .build();
                                roomFascade.daoPosting.save(myStore);
                            });
                });

    }

}

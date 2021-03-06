package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.my.store.MyIntroducedStore;
import com.github.conanchen.gedit.room.my.store.MyStore;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.store.introducer.grpc.ListMyIntroducedStoreRequest;
import com.github.conanchen.gedit.store.owner.grpc.ListMyStoreRequest;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Conan Chen on 2018/1/8.
 */
@Singleton
public class MyIntroducedStoreRepository {
    private final static String TAG = MyIntroducedStoreRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public MyIntroducedStoreRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }


    public LiveData<List<Store>> createTime(Long time) {
        return roomFascade.daoStore.getLiveStores(100);
    }

    private static final PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
            .setEnablePlaceholders(true)
            .setPrefetchDistance(10)
            .setPageSize(20).build();


    public LiveData<PagedList<MyIntroducedStore>> loadMyIntroducedStores(Long times) {
        Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
            grpcFascade.myIntroducedStoreService.loadMyIntroducedStores(
                    ListMyIntroducedStoreRequest.newBuilder().build(), response -> {
                MyIntroducedStore myStore = MyIntroducedStore.builder()
                        .setStoreUuid(response.getIntroducership().getStoreUuid())
                        .setLat(response.getIntroducership().getLocation().getLat())
                        .setLon(response.getIntroducership().getLocation().getLon())
                        .setStoreLogo(response.getIntroducership().getStoreLogo())
                        .setStoreName(response.getIntroducership().getStoreName())
                        .build();
                roomFascade.daoMyIntroducedStore.save(myStore);
            });
        });

        return (new LivePagedListBuilder(roomFascade.daoMyIntroducedStore.listLivePagedMyStore(), pagedListConfig))
                .build();
    }
}

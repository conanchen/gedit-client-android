package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.github.conanchen.gedit.hello.grpc.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.store.MyStore;
import com.github.conanchen.gedit.room.store.Store;
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
public class MyWorkinStoreRepository {
    private final static String TAG = MyWorkinStoreRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public MyWorkinStoreRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
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


    public LiveData<PagedList<MyStore>> loadMyStores(Long times) {
        Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
            grpcFascade.myStoreService.loadMyStores(ListMyStoreRequest.newBuilder().build(), response -> {
                MyStore myStore = MyStore.builder()
                        .setStoreUuid(response.getOwnership().getStoreUuid())
                        .setLat(response.getOwnership().getLocation().getLat())
                        .setLon(response.getOwnership().getLocation().getLon())
                        .setStoreLogo(response.getOwnership().getStoreLogo())
                        .setStoreName(response.getOwnership().getStoreName())
                        .build();
                 roomFascade.daoMyStore.save(myStore);
            });
        });

        return (new LivePagedListBuilder(roomFascade.daoMyStore.listLivePagedMyStore(), pagedListConfig))
                .build();
    }
}

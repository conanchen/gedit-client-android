package com.github.conanchen.gedit.repository.hello;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.conanchen.gedit.hello.grpc.di.GrpcFascade;
import com.github.conanchen.gedit.hello.grpc.store.StoreCreateInfo;
import com.github.conanchen.gedit.hello.grpc.store.StoreService;
import com.github.conanchen.gedit.hello.grpc.store.StoreUpdateInfo;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.store.MyStore;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.store.owner.grpc.OwnershipResponse;
import com.github.conanchen.gedit.store.profile.grpc.CreateStoreResponse;
import com.github.conanchen.gedit.store.profile.grpc.UpdateStoreResponse;
import com.github.conanchen.gedit.store.search.grpc.SearchRequest;
import com.github.conanchen.gedit.vo.Location;
import com.github.conanchen.gedit.vo.StoreCreateResponse;
import com.github.conanchen.gedit.vo.StoreUpdateResponse;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Conan Chen on 2018/1/8.
 */
@Singleton
public class MyStoreRepository {
    private final static String TAG = MyStoreRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public MyStoreRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
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
        grpcFascade.storeService.loadMyStores(new StoreService.LoadMyStoresCallBack() {
            @Override
            public void onLoadMyStores(OwnershipResponse response) {
                Log.i("-=-=-", String.format("loadMyStores: %s", response.getStatus()));
                Observable.fromCallable(() -> {
                    if ("OK".compareToIgnoreCase(response.getStatus().getCode()) == 0) {
                        MyStore myStore = MyStore.builder()
                                .setStoreUuid(response.getOwnership().getStoreUuid())
                                .setLat(response.getOwnership().getLocation().getLat())
                                .setLon(response.getOwnership().getLocation().getLon())
                                .setStoreLogo(response.getOwnership().getStoreLogo())
                                .setStoreName(response.getOwnership().getStoreName())
                                .build();
                        return roomFascade.daoMyStore.save(myStore);
                    } else {
                        MyStore myStore = MyStore.builder()
                                .setStoreUuid("storeUuid" + System.currentTimeMillis())
                                .setLat(111.0)
                                .setLon(222.0)
                                .setStoreName("shibai")
                                .setStoreLogo("wwwwwwwww")
                                .build();
                        return roomFascade.daoMyStore.save(myStore);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long rowId) throws Exception {
                                // the uuid of the upserted record.
                                if (rowId > 0) {
                                    return;
                                } else {
                                    return;
                                }
                            }
                        });
                ;
            }
        });

        Log.i("-=-=-", "伙计走到这一步，但是没数据");
        return (new LivePagedListBuilder(roomFascade.daoMyStore.listLivePagedMyStore(), pagedListConfig))
                .build();
    }
}

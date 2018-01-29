package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.store.MyStoreService;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.my.store.MyStore;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.store.owner.grpc.OwnershipResponse;
import com.github.conanchen.utils.vo.StoreCreateInfo;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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


    public LiveData<PagedList<MyStore>> loadMyStores(StoreCreateInfo storeCreateInfo) {

        grpcFascade.myStoreService.loadMyStores(storeCreateInfo, new MyStoreService.OwnershipCallBack() {
            @Override
            public void onOwnershipResponse(OwnershipResponse response) {
                if (Status.Code.OK == response.getStatus().getCode()) {
                    Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
                        MyStore myStore = MyStore.builder()
                                .setStoreUuid(response.getOwnership().getStoreUuid())
                                .setLat(response.getOwnership().getLocation().getLat())
                                .setLon(response.getOwnership().getLocation().getLon())
                                .setStoreLogo(response.getOwnership().getStoreLogo())
                                .setStoreName(response.getOwnership().getStoreName())
                                .setLastUpdated(System.currentTimeMillis())
                                .build();
                        roomFascade.daoMyStore.save(myStore);
                    });
                }
            }

            @Override
            public void onGrpcApiError(Status status) {
                if (Status.Code.UNKNOWN == status.getCode()) {
                    Observable.just(true)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aBoolean -> {

                            });
                }
            }

            @Override
            public void onGrpcApiCompleted() {

            }
        });

        return (new LivePagedListBuilder(roomFascade.daoMyStore.listLivePagedMyStore(), pagedListConfig))
                .build();
    }

}

package com.github.conanchen.gedit.repository.hello;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.conanchen.gedit.hello.grpc.StoreService;
import com.github.conanchen.gedit.hello.grpc.di.GrpcFascade;
import com.github.conanchen.gedit.vo.Location;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.hello.Store;
import com.github.conanchen.gedit.store.profile.grpc.CreateResponse;
import com.github.conanchen.gedit.hello.grpc.StoreCreateInfo;
import com.github.conanchen.gedit.vo.StoreCreateResponse;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class StoreRepository {
    private final static String TAG = StoreRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public StoreRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }


    public LiveData<List<Store>> createTime(Long time) {
        return roomFascade.daoStore.getLiveStores(100);
    }


    public LiveData<List<Store>> loadStoresNearAt(Location location) {
        return null;
    }

    public LiveData<Store> findStore(String uuid) {
        return null;
    }

    public LiveData<StoreCreateResponse> createStore(StoreCreateInfo storeCreateInfo) {
        return new LiveData<StoreCreateResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.storeService.storeCreate(storeCreateInfo, new StoreService.StoreCallback() {
                    @Override
                    public void onStoreCreateResponse(CreateResponse createResponse) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("CreateResponse: %s", createResponse.getStatus()));
                            if ("OK".compareToIgnoreCase(createResponse.getStatus().getCode()) == 0) {
                                Store s = Store.builder()
                                        .setUuid(createResponse.getUuid())
                                        .setName(storeCreateInfo.name)
                                        .setDistrictUuid(storeCreateInfo.districtUuid)
                                        .setAddress(storeCreateInfo.address)
                                        .build();
                                return roomFascade.daoStore.save(s);
                            } else {
                                return new Long(-1);
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(@NonNull Long rowId) throws Exception {
                                        // the id of the upserted record.
                                        if (rowId > 0) {
                                            setValue(StoreCreateResponse.builder()
                                                    .setStausCode("OK")
                                                    .setStatusDetail("Create Store successfully")
                                                    .setStoreUuid(createResponse.getUuid())
                                                    .build());
                                        } else {
                                            setValue(StoreCreateResponse.builder()
                                                    .setStausCode(createResponse.getStatus().getCode())
                                                    .setStatusDetail(createResponse.getStatus().getDetails())
                                                    .setStoreUuid(createResponse.getUuid())
                                                    .build());
                                        }
                                    }
                                });
                        ;
                    }
                });
            }
        };
    }
}

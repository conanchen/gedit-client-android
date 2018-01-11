package com.github.conanchen.gedit.repository.hello;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.conanchen.gedit.hello.grpc.StoreCreateInfo;
import com.github.conanchen.gedit.hello.grpc.StoreService;
import com.github.conanchen.gedit.hello.grpc.StoreUpdateInfo;
import com.github.conanchen.gedit.hello.grpc.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.store.profile.grpc.CreateResponse;
import com.github.conanchen.gedit.store.profile.grpc.UpdateResponse;
import com.github.conanchen.gedit.vo.Location;
import com.github.conanchen.gedit.vo.StoreCreateResponse;
import com.github.conanchen.gedit.vo.StoreUpdateResponse;
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
                                        // the uuid of the upserted record.
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

    public LiveData<StoreUpdateResponse> updateStore(StoreUpdateInfo storeUpdateInfo) {
        Log.i(TAG, "enter updateStoreWith()");
        return new LiveData<StoreUpdateResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.storeService.updateStore(storeUpdateInfo, new StoreService.UpdateCallback() {
                    @Override
                    public void onUpdateResponse(UpdateResponse response) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("UpdateResponse: %s", response.getStatus()));
                            if ("OK".compareToIgnoreCase(response.getStatus().getCode()) == 0) {
                                Store s = roomFascade.daoStore.findOne(storeUpdateInfo.uuid);
                                s.address = storeUpdateInfo.detailAddress;
                                return roomFascade.daoStore.save(s);
                            } else {
                                return new Long(-1);
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(@NonNull Long rowId) throws Exception {
                                        // the uuid of the upserted record.
                                        if (rowId > 0) {
                                            setValue(StoreUpdateResponse.builder()
                                                    .setStausCode("OK")
                                                    .setStatusDetail("update Store successfully")
                                                    .setStoreUuid(response.getUuid())
                                                    .build());
                                        } else {
                                            setValue(StoreUpdateResponse.builder()
                                                    .setStausCode(response.getStatus().getCode())
                                                    .setStatusDetail(response.getStatus().getDetails())
                                                    .setStoreUuid(response.getUuid())
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

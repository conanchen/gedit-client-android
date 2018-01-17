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
import com.github.conanchen.gedit.room.store.Store;
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

    private static final PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
            .setEnablePlaceholders(true)
            .setPrefetchDistance(10)
            .setPageSize(20).build();

    public LiveData<PagedList<Store>> loadStoresNearAt(Location location) {

        //  call grpc api to refresh near stores
        Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
            grpcFascade.storeService.searchStoresNearAt(
                    SearchRequest.newBuilder().setLat(location.lat).setLon(location.lon).build(),
                    response -> {
                        //TODO 填写完整信息
                        Store s = Store.builder()
                                .setUuid(response.getUuid())
                                .setName(response.getName())
                                .setAddress(response.getDesc())
                                .build();
                        roomFascade.daoStore.save(s);
                    });
        });
        return (new LivePagedListBuilder(roomFascade.daoStore.listLivePagedStore(), pagedListConfig))
                .build();
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
                    public void onStoreCreateResponse(CreateStoreResponse createResponse) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("CreateStoreResponse: %s", createResponse.getStatus()));
                            if ("OK".compareToIgnoreCase(createResponse.getStatus().getCode()) == 0) {
                                Store s = Store.builder()
                                        .setUuid(createResponse.getUuid())
                                        .setName(storeCreateInfo.name)
                                        .setDistrictUuid(storeCreateInfo.districtUuid)
                                        .setAddress(storeCreateInfo.address)
                                        .build();
                                return roomFascade.daoStore.save(s);
                            } else {
                                Store s = Store.builder()
                                        .setUuid("uuid" + System.currentTimeMillis())
                                        .setName("name" + System.currentTimeMillis())
                                        .setDistrictUuid("districtUuid" + System.currentTimeMillis())
                                        .setAddress("address" + System.currentTimeMillis())
                                        .build();
                                Log.i(TAG, "添加假数据");
                                return roomFascade.daoStore.save(s);
//                                return new Long(-1);
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
                                                    .setStoreUuid("set jia shu ju uuid")
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
                    public void onUpdateStoreResponse(UpdateStoreResponse response) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("UpdateStoreResponse: %s", response.getStatus()));
                            if ("OK".compareToIgnoreCase(response.getStatus().getCode()) == 0) {
                                Store s = roomFascade.daoStore.findOne(storeUpdateInfo.uuid);
                                s.lastUpdated = response.getLastUpdated();
//                                s.address = storeUpdateInfo.detailAddress;
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

    /**
     * 修改头像的方法
     *
     * @param storeUpdateInfo
     * @return
     */
    public LiveData<StoreUpdateResponse> updateHeadPortrait(StoreUpdateInfo storeUpdateInfo) {
        return new LiveData<StoreUpdateResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.storeService.updateHeadPortrait(storeUpdateInfo, new StoreService.UpdateHeadPortraitCallback() {
                    @Override
                    public void onUpdateHeadPortraitResponse(UpdateStoreResponse response) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("UpdateStoreResponse: %s", response.getStatus()));
                            if ("OK".compareToIgnoreCase(response.getStatus().getCode()) == 0) {
                                Store s = roomFascade.daoStore.findOne(storeUpdateInfo.uuid);
                                if (StoreUpdateInfo.Field.DETAIL_ADDRESS.equals(storeUpdateInfo.name)) {
                                    s.address = (String) storeUpdateInfo.value;
                                } else {
                                    //TODO ....
                                }
                                s.lastUpdated = response.getLastUpdated();
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
                                                    .setStatusDetail("update head portrait Store successfully")
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

    /**
     * 修改头像的方法
     *
     * @param storeUpdateInfo
     * @return
     */
    public LiveData<StoreUpdateResponse> updateAddress(StoreUpdateInfo storeUpdateInfo) {
        return new LiveData<StoreUpdateResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.storeService.updateAddress(storeUpdateInfo, new StoreService.UpdateHeadPortraitCallback() {
                    @Override
                    public void onUpdateHeadPortraitResponse(UpdateStoreResponse response) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("UpdateStoreResponse: %s", response.getStatus()));
                            if ("OK".compareToIgnoreCase(response.getStatus().getCode()) == 0) {
                                Store s = roomFascade.daoStore.findOne(storeUpdateInfo.uuid);
                                s.address = (String) storeUpdateInfo.value;
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
                                                    .setStatusDetail("update head portrait Store successfully")
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

package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Location;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.store.StoreProfileService;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.my.store.MyStore;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.store.profile.grpc.CreateStoreResponse;
import com.github.conanchen.gedit.store.profile.grpc.GetStoreRequest;
import com.github.conanchen.gedit.store.profile.grpc.UpdateStoreResponse;
import com.github.conanchen.gedit.store.search.grpc.SearchStoreRequest;
import com.github.conanchen.utils.vo.StoreCreateInfo;
import com.github.conanchen.utils.vo.StoreUpdateInfo;
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
public class StoreProfileRepository {
    private final static String TAG = StoreProfileRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public StoreProfileRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
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
        return (new LivePagedListBuilder<Integer, Store>(roomFascade.daoStore.listLivePagedStore(), pagedListConfig))
                .setBoundaryCallback(new PagedList.BoundaryCallback<Store>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        refreshNearStores(location);
                    }
                })
                .build();
    }

    public void refreshNearStores(Location location) {
        //  call grpc api to refresh near stores
        Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
            grpcFascade.storeSearchService.searchStoresNearAt(
                    SearchStoreRequest.newBuilder()
                            .setLocation(Location.newBuilder()
                                    .setLat(location.getLat())
                                    .setLon(location.getLon())
                                    .build())
                            .build(),
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
    }

    public LiveData<Store> findStore(String uuid) {
        grpcFascade.storeProfileService.downloadStoreProfile(
                GetStoreRequest.newBuilder().setUuid(uuid).setLastUpdated(-1).build(),
                response -> {
                    Observable
                            .just(true)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(aBoolean -> {
                                if (Status.Code.OK.getNumber() == response.getStatus().getCode().getNumber()) {
                                    Store s = Store.builder()
                                            .setUuid(response.getStoreProfile().getUuid())
                                            .setName(response.getStoreProfile().getName())
                                            .setDistrictUuid(response.getStoreProfile().getDistrictUuid())
                                            .setAddress(response.getStoreProfile().getDetailAddress())
                                            .build();
                                    roomFascade.daoStore.save(s);
                                }
                            });
                });
        return roomFascade.daoStore.findLive(uuid);
    }

    public LiveData<CreateStoreResponse> createStore(StoreCreateInfo storeCreateInfo) {
        return new LiveData<CreateStoreResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.storeProfileService.storeCreate(storeCreateInfo, new StoreProfileService.StoreCreateCallback() {
                    @Override
                    public void onStoreCreateResponse(CreateStoreResponse createResponse) {
                        Observable.fromCallable(() -> {
                            if (Status.Code.OK == createResponse.getStatus().getCode()) {
                                MyStore s = MyStore.builder()
                                        .setStoreUuid(createResponse.getUuid())
                                        .setStoreName(storeCreateInfo.name)
                                        .setLat(storeCreateInfo.lat)
                                        .setLon(storeCreateInfo.lon)
                                        .setLastUpdated(System.currentTimeMillis())
                                        .build();
                                return roomFascade.daoMyStore.save(s);
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
                                            setValue(CreateStoreResponse.newBuilder()
                                                    .setName(storeCreateInfo.name)
                                                    .setUuid(createResponse.getUuid())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(Status.Code.OK)
                                                            .setDetails("Create Store successfully")
                                                            .build())
                                                    .build());
                                        } else {
                                            setValue(CreateStoreResponse.newBuilder()
                                                    .setName(storeCreateInfo.name)
                                                    .setUuid(createResponse.getUuid())
                                                    .setStatus(createResponse.getStatus())
                                                    .build());
                                        }
                                    }
                                });
                    }
                });
            }
        };
    }

    public LiveData<UpdateStoreResponse> updateStore(StoreUpdateInfo storeUpdateInfo) {
        return new LiveData<UpdateStoreResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.storeProfileService.updateStore(storeUpdateInfo, new StoreProfileService.UpdateCallback() {
                    @Override
                    public void onUpdateStoreResponse(UpdateStoreResponse response) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("UpdateStoreResponse: %s", response.getStatus()));
                            if (Status.Code.OK.getNumber() == response.getStatus().getCode().getNumber()) {
                                Store s = roomFascade.daoStore.findOne(storeUpdateInfo.uuid);
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
                                            setValue(UpdateStoreResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(Status.Code.OK)
                                                            .setDetails("update Store successfully")
                                                            .build())
                                                    .setUuid(response.getUuid())
                                                    .build());
                                        } else {
                                            setValue(UpdateStoreResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(response.getStatus().getCode())
                                                            .setDetails(response.getStatus().getDetails())
                                                            .build())
                                                    .setUuid(response.getUuid())
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
    public LiveData<UpdateStoreResponse> updateHeadPortrait(StoreUpdateInfo storeUpdateInfo) {
        return new LiveData<UpdateStoreResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.storeProfileService.updateHeadPortrait(storeUpdateInfo, new StoreProfileService.UpdateHeadPortraitCallback() {
                    @Override
                    public void onUpdateHeadPortraitResponse(UpdateStoreResponse response) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("UpdateStoreResponse: %s", response.getStatus()));
                            if (Status.Code.OK.getNumber() == response.getStatus().getCode().getNumber()) {
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
                                            setValue(UpdateStoreResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(Status.Code.OK)
                                                            .setDetails("update Store successfully")
                                                            .build())
                                                    .setUuid(response.getUuid())
                                                    .build());
                                        } else {
                                            setValue(UpdateStoreResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(response.getStatus().getCode())
                                                            .setDetails(response.getStatus().getDetails())
                                                            .build())
                                                    .setUuid(response.getUuid())
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
    public LiveData<UpdateStoreResponse> updateAddress(StoreUpdateInfo storeUpdateInfo) {
        return new LiveData<UpdateStoreResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.storeProfileService.updateAddress(storeUpdateInfo, new StoreProfileService.UpdateHeadPortraitCallback() {
                    @Override
                    public void onUpdateHeadPortraitResponse(UpdateStoreResponse response) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("UpdateStoreResponse: %s", response.getStatus()));
                            if (Status.Code.OK.getNumber() == response.getStatus().getCode().getNumber()) {
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
                                            setValue(UpdateStoreResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(Status.Code.OK)
                                                            .setDetails("update Store successfully")
                                                            .build())
                                                    .setUuid(response.getUuid())
                                                    .build());
                                        } else {
                                            setValue(UpdateStoreResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(response.getStatus().getCode())
                                                            .setDetails(response.getStatus().getDetails())
                                                            .build())
                                                    .setUuid(response.getUuid())
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

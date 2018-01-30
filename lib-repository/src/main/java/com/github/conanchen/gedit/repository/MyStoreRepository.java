package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.store.MyStoreService;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.room.kv.Value;
import com.github.conanchen.gedit.room.my.store.MyStore;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.store.owner.grpc.OwnershipResponse;
import com.github.conanchen.utils.vo.StoreCreateInfo;
import com.github.conanchen.utils.vo.VoLoadGrpcStatus;
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

    /**
     * 获取我的商铺列表
     *
     * @param storeCreateInfo
     * @return
     */
    public LiveData<PagedList<MyStore>> loadMyStores(StoreCreateInfo storeCreateInfo) {
        Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
            grpcFascade.myStoreService.loadMyStores(storeCreateInfo, new MyStoreService.OwnershipCallBack() {
                @Override
                public void onOwnershipResponse(OwnershipResponse response) {
                    if (Status.Code.OK == response.getStatus().getCode()) {
                        MyStore myStore = MyStore.builder()
                                .setStoreUuid(response.getOwnership().getStoreUuid())
                                .setLat(response.getOwnership().getLocation().getLat())
                                .setLon(response.getOwnership().getLocation().getLon())
                                .setStoreLogo(response.getOwnership().getStoreLogo())
                                .setStoreName(response.getOwnership().getStoreName())
                                .setLastUpdated(System.currentTimeMillis())
                                .build();
                        roomFascade.daoMyStore.save(myStore);
                    }
                }

                @Override
                public void onGrpcApiError(Status status) {
                    saveGrpcStatus(VoLoadGrpcStatus.builder()
                            .setStatus(status.getCode().toString())
                            .setMessage("网络不佳，请稍后重试")
                            .build());
                }

                @Override
                public void onGrpcApiCompleted() {
                    saveGrpcStatus(VoLoadGrpcStatus.builder()
                            .setStatus("COMPLETED")
                            .setMessage("暂无更多数据")
                            .build());

                }
            });
        });

        return (new LivePagedListBuilder(roomFascade.daoMyStore.listLivePagedMyStore(), pagedListConfig)).build();
    }


    private void saveGrpcStatus(VoLoadGrpcStatus voLoadGrpcStatus) {
        KeyValue keyValue = KeyValue.builder()
                .setKey(KeyValue.KEY.LOAD_GRPC_API_STATUS)
                .setValue(Value.builder()
                        .setVoLoadGrpcStatus(voLoadGrpcStatus)
                        .build())
                .build();
//        roomFascade.daoKeyValue.save(keyValue);
    }

}

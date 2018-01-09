package com.github.conanchen.gedit.repository.hello;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.github.conanchen.gedit.hello.grpc.StoreService;
import com.github.conanchen.gedit.hello.grpc.di.GrpcFascade;
import com.github.conanchen.gedit.vo.Location;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.hello.Store;
import com.github.conanchen.gedit.store.profile.grpc.CreateResponse;
import com.github.conanchen.gedit.vo.StoreCreateInfo;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

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

    public void storeCreate(Store store) {
        grpcFascade.storeService.storeCreate(store, new StoreService.StoreCallback() {
            @Override
            public void onStoreReply(CreateResponse createResponse) {
                Log.i(TAG, String.format("CreateResponse: %s", createResponse.getStatus()));
                Store s = Store.builder()
                        .setUuid(createResponse.getUuid())
                        .setStoreName(store.storeName)
                        .setAddress(store.address)
                        .build();
                roomFascade.daoStore.save(s);
            }
        });
    }

    public LiveData<List<Store>> createTime(Long time) {
        return roomFascade.daoStore.getLiveStores(100);
    }


    public LiveData<List<Store>> loadStoresNearAt(Location location) {
        return null;
    }

    public   LiveData<Store> findStore(String uuid) {
        return null;
    }

    public LiveData<Store> createStore(StoreCreateInfo storeCreateInfo) {
        return null;
    }
}

package com.github.conanchen.gedit.grpc.store;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.store.owner.grpc.ListMyStoreRequest;
import com.github.conanchen.gedit.store.owner.grpc.Ownership;
import com.github.conanchen.gedit.store.owner.grpc.OwnershipResponse;
import com.github.conanchen.gedit.store.owner.grpc.StoreOwnerApiGrpc;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class MyStoreService {
    private final static String TAG = MyStoreService.class.getSimpleName();

    public interface OwnershipCallBack {
        void onOwnershipResponse(OwnershipResponse response);

    }


    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_STORE)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }


    public void loadMyStores(ListMyStoreRequest request, OwnershipCallBack callBack) {
        ManagedChannel channel = getManagedChannel();
        StoreOwnerApiGrpc.StoreOwnerApiStub storeOwnerApiStub = StoreOwnerApiGrpc.newStub(channel);
        storeOwnerApiStub.listMyStore(request, new StreamObserver<OwnershipResponse>() {
            @Override
            public void onNext(OwnershipResponse value) {
                Log.i("-=-=-", "onNext");
                callBack.onOwnershipResponse(value);
            }

            @Override
            public void onError(Throwable t) {
                Log.i("-=-=-", "onError");
                callBack.onOwnershipResponse(OwnershipResponse.newBuilder()
                        .setOwnership(Ownership.newBuilder()
                                .setStoreUuid("uuid" + System.currentTimeMillis())
                                .setStoreName("name" + System.currentTimeMillis())
                                .build())
                        .build());
            }

            @Override
            public void onCompleted() {
                Log.i("-=-=-", "onCompleted");
                callBack.onOwnershipResponse(OwnershipResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode("onCompleted()")
                                .setDetails(String.format("onCompleted（）"))
                                .build())
                        .build());
            }
        });
    }


}

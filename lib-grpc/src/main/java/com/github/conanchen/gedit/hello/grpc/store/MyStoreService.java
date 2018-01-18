package com.github.conanchen.gedit.hello.grpc.store;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.hello.grpc.utils.JcaUtils;
import com.github.conanchen.gedit.store.owner.grpc.ListMyStoreRequest;
import com.github.conanchen.gedit.store.owner.grpc.OwnershipResponse;
import com.github.conanchen.gedit.store.owner.grpc.StoreOwnerApiGrpc;
import com.github.conanchen.gedit.store.profile.grpc.CreateStoreResponse;
import com.github.conanchen.gedit.store.profile.grpc.StoreProfileApiGrpc;
import com.github.conanchen.gedit.store.profile.grpc.UpdateStoreResponse;
import com.github.conanchen.gedit.store.search.grpc.SearchStoreRequest;
import com.github.conanchen.gedit.store.search.grpc.SearchStoreResponse;
import com.github.conanchen.gedit.store.search.grpc.StoreSearchApiGrpc;
import com.google.common.base.Strings;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class MyStoreService {
    private final static String TAG = MyStoreService.class.getSimpleName();

    public interface LoadMyStoresCallBack {
        void onLoadMyStores(OwnershipResponse response);

    }


    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }



    public void loadMyStores(MyStoreService.LoadMyStoresCallBack callBack) {
        ManagedChannel channel = getManagedChannel();
        StoreOwnerApiGrpc.StoreOwnerApiStub storeOwnerApiStub = StoreOwnerApiGrpc.newStub(channel);
        Log.i("-=-=-", "进来了没");
        storeOwnerApiStub.listMyStore(ListMyStoreRequest.newBuilder().build(), new StreamObserver<OwnershipResponse>() {
            @Override
            public void onNext(OwnershipResponse value) {
                Log.i("-=-=-", "onNext");
                callBack.onLoadMyStores(value);
            }

            @Override
            public void onError(Throwable t) {
                Log.i("-=-=-", "onError");
                callBack.onLoadMyStores(OwnershipResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode("FAILED")
                                .setDetails(String.format("API访问错误，可能网络不通！error:%s", t.getMessage()))
                                .build())
                        .build());
            }

            @Override
            public void onCompleted() {
                Log.i("-=-=-", "onCompleted");
                callBack.onLoadMyStores(OwnershipResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode("onCompleted()")
                                .setDetails(String.format("onCompleted（）"))
                                .build())
                        .build());
            }
        });
    }


}

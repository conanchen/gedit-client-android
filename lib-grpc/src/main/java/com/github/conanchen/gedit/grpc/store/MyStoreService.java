package com.github.conanchen.gedit.grpc.store;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.store.owner.grpc.ListMyStoreRequest;
import com.github.conanchen.gedit.store.owner.grpc.Ownership;
import com.github.conanchen.gedit.store.owner.grpc.OwnershipResponse;
import com.github.conanchen.gedit.store.owner.grpc.StoreOwnerApiGrpc;
import com.github.conanchen.gedit.utils.JcaUtils;
import com.github.conanchen.utils.vo.StoreCreateInfo;
import com.google.gson.Gson;

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
    private Gson gson = new Gson();

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


    public void loadMyStores(StoreCreateInfo storeCreateInfo, OwnershipCallBack callBack) {

        Log.i("-=-=-=-=-=", "列表 token:" + storeCreateInfo.voAccessToken.accessToken + "----------expiresIn:" + storeCreateInfo.voAccessToken.expiresIn);
        ManagedChannel channel = getManagedChannel();
        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(storeCreateInfo.voAccessToken.accessToken
                        , Long.valueOf(storeCreateInfo.voAccessToken.expiresIn));

        StoreOwnerApiGrpc.StoreOwnerApiStub storeOwnerApiStub = StoreOwnerApiGrpc.newStub(channel);
        storeOwnerApiStub
                .withCallCredentials(callCredentials)
                .listMyStore(ListMyStoreRequest.newBuilder()
                        .build(), new StreamObserver<OwnershipResponse>() {
                    @Override
                    public void onNext(OwnershipResponse value) {
                        Log.i("-=-=-", "onNext"+ gson.toJson(value));
                        callBack.onOwnershipResponse(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-", "onError" + gson.toJson(t));
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

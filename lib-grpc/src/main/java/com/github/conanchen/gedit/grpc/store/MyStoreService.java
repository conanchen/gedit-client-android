package com.github.conanchen.gedit.grpc.store;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.grpc.GrpcApiCallback;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.store.owner.grpc.ListMyStoreRequest;
import com.github.conanchen.gedit.store.owner.grpc.OwnershipResponse;
import com.github.conanchen.gedit.store.owner.grpc.StoreOwnerApiGrpc;
import com.github.conanchen.gedit.utils.JcaUtils;
import com.github.conanchen.utils.vo.StoreCreateInfo;
import com.google.gson.Gson;

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

    public interface OwnershipCallBack extends GrpcApiCallback {
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
                        Log.i("-=-=-==========", "onNext" + gson.toJson(value));
                        callBack.onOwnershipResponse(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-==========", "onError" + gson.toJson(t));
                        callBack.onGrpcApiError(Status.newBuilder()
                                .setCode(Status.Code.UNKNOWN)
                                .setDetails("网络不佳，请稍后重试")
                                .build());
                    }

                    @Override
                    public void onCompleted() {
                        callBack.onGrpcApiCompleted();
                        Log.i("-=-=-==========", "onCompleted");
                    }
                });
    }


}

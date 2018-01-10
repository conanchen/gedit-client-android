package com.github.conanchen.gedit.hello.grpc;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.store.profile.grpc.CreateResponse;
import com.github.conanchen.gedit.store.profile.grpc.StoreProfileApiGrpc;
import com.google.common.base.Strings;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class StoreService {
    private final static String TAG = StoreService.class.getSimpleName();

    public interface StoreCallback {
        void onStoreCreateResponse(CreateResponse response);
    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    public void storeCreate(StoreCreateInfo storeCreateInfo, StoreService.StoreCallback callback) {
        ManagedChannel channel = getManagedChannel();


        StoreProfileApiGrpc.StoreProfileApiStub storeProfileApiStub = StoreProfileApiGrpc.newStub(channel);
        storeProfileApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .create(com.github.conanchen.gedit.store.profile.grpc.CreateRequest
                                .newBuilder()
                                .setName(storeCreateInfo.name)
                                .setDetailAddress(Strings.isNullOrEmpty(storeCreateInfo.address)?"no-detail-address":storeCreateInfo.address)
                                .build(),
                        new StreamObserver<CreateResponse>() {
                            @Override
                            public void onNext(CreateResponse value) {
                                callback.onStoreCreateResponse(value);
                                Status status = value.getStatus();
                                String code = status.getCode();
                                Log.i(TAG, "staus:" + status + ",code:" + code);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e(TAG, t.getMessage());
                                callback.onStoreCreateResponse(
                                        CreateResponse.newBuilder()
                                                .setStatus(Status.newBuilder().setCode("FAILED")
                                                        .setDetails(String.format("API访问错误，可能网络不通！error:%s", t.getMessage()))
                                                        .build())
                                                .build());
                            }

                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "storeProfileApiStub.create onCompleted()");
                            }
                        });

    }
}

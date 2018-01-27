package com.github.conanchen.gedit.grpc.store;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.grpc.GrpcApiCallback;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.store.worker.grpc.GetMyCurrentWorkinStoreRequest;
import com.github.conanchen.gedit.store.worker.grpc.StoreWorkerApiGrpc;
import com.github.conanchen.gedit.store.worker.grpc.WorkshipResponse;
import com.github.conanchen.gedit.utils.JcaUtils;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.gson.Gson;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class MyWorkinStoreService {

    private Gson gson = new Gson();

    public interface WorkingStoreCallBack extends GrpcApiCallback {
        void onWorkingStoreCallBack(WorkshipResponse workshipResponse);
    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_AUTH)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 获取我工作商铺的详情
     *
     * @param voAccessToken
     * @param callBack
     */
    public void getMyCurrentWorkinStore(VoAccessToken voAccessToken, WorkingStoreCallBack callBack) {

        ManagedChannel managedChannel = getManagedChannel();
        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(voAccessToken.accessToken,
                        Long.valueOf(voAccessToken.expiresIn));

        StoreWorkerApiGrpc.StoreWorkerApiStub storeWorkerApiStub = StoreWorkerApiGrpc.newStub(managedChannel);
        storeWorkerApiStub
                .withCallCredentials(callCredentials)
                .getMyCurrentWorkinStore(GetMyCurrentWorkinStoreRequest.newBuilder()
                                .build(),
                        new StreamObserver<WorkshipResponse>() {
                            @Override
                            public void onNext(WorkshipResponse value) {
                                Log.i("=======---", gson.toJson(value));
                                callBack.onWorkingStoreCallBack(value);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i("=======---", gson.toJson(t));
                                callBack.onGrpcApiError(Status.newBuilder()
                                        .setCode(Status.Code.UNKNOWN)
                                        .setDetails("获取工作商铺详情失败")
                                        .build());
                            }

                            @Override
                            public void onCompleted() {
                                callBack.onGrpcApiCompleted();
                            }
                        });

    }


}

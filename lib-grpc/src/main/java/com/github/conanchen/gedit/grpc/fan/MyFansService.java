package com.github.conanchen.gedit.grpc.fan;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.store.owner.grpc.ListMyStoreRequest;
import com.github.conanchen.gedit.user.fans.grpc.Fanship;
import com.github.conanchen.gedit.user.fans.grpc.FanshipResponse;
import com.github.conanchen.gedit.user.fans.grpc.ListMyFanRequest;
import com.github.conanchen.gedit.user.fans.grpc.UserFansApiGrpc;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class MyFansService {
    private final static String TAG = MyFansService.class.getSimpleName();

    public interface FanshipCallBack {
        void onFanshipResponse(FanshipResponse response);

    }


    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_AUTH)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }



    public void loadMyFans(ListMyFanRequest request, FanshipCallBack callBack) {
        ManagedChannel channel = getManagedChannel();
        UserFansApiGrpc.UserFansApiStub userFansApiStub = UserFansApiGrpc.newStub(channel);
        userFansApiStub.listMyFan(request, new StreamObserver<FanshipResponse>() {
            @Override
            public void onNext(FanshipResponse value) {
                Log.i("-=-=-", "onNext");
                callBack.onFanshipResponse(value);
            }

            @Override
            public void onError(Throwable t) {
                Log.i("-=-=-", "onError");
                callBack.onFanshipResponse(FanshipResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode("FAILED")
                                .setDetails(String.format("API访问错误，可能网络不通！error:%s", t.getMessage()))
                                .build())
                        .setFanship( com.github.conanchen.gedit.user.fans.grpc.Fanship.newBuilder()
                                .setFanUuid("fanUuid"+System.currentTimeMillis())
                                .setFanName("fanname "+System.currentTimeMillis())
                                .setCreated(System.currentTimeMillis())
                                .build())
                        .build());
            }

            @Override
            public void onCompleted() {
                Log.i("-=-=-", "onCompleted");
                callBack.onFanshipResponse(FanshipResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode("onCompleted()")
                                .setDetails(String.format("onCompleted（）"))
                                .build())
                        .setFanship( com.github.conanchen.gedit.user.fans.grpc.Fanship.newBuilder()
                                .setFanUuid("fanUuid"+System.currentTimeMillis())
                                .setFanName("fanname "+System.currentTimeMillis())
                                .setCreated(System.currentTimeMillis())
                                .build())
                        .build());
            }
        });
    }


}

package com.github.conanchen.gedit.grpc.fan;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.user.fans.grpc.AddFanshipRequest;
import com.github.conanchen.gedit.user.fans.grpc.FanshipResponse;
import com.github.conanchen.gedit.user.fans.grpc.ListMyFanRequest;
import com.github.conanchen.gedit.user.fans.grpc.UserFansApiGrpc;
import com.github.conanchen.gedit.utils.JcaUtils;
import com.github.conanchen.utils.vo.MyFansBean;

import io.grpc.CallCredentials;
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

    public interface AddFansCallBack {
        void onAddFansCallBack(FanshipResponse response);

    }


    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_AUTH)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }


    public void loadMyFans(MyFansBean myFansBean, FanshipCallBack callBack) {
        ManagedChannel channel = getManagedChannel();
        CallCredentials callCredentials = JcaUtils.getCallCredentials(myFansBean.voAccessToken.accessToken,
                Long.valueOf(myFansBean.voAccessToken.expiresIn));
        UserFansApiGrpc.UserFansApiStub userFansApiStub = UserFansApiGrpc.newStub(channel);
        userFansApiStub
                .withCallCredentials(callCredentials)
                .listMyFan(ListMyFanRequest.newBuilder().build(), new StreamObserver<FanshipResponse>() {
                    @Override
                    public void onNext(FanshipResponse value) {
                        Log.i("-=-=-", "onNext");
                        callBack.onFanshipResponse(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-", "onError");
                        callBack.onFanshipResponse(FanshipResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode(Status.Code.UNKNOWN)
                                        .setDetails(String.format("API访问错误，可能网络不通！error:%s", t.getMessage()))
                                        .build())
                                .build());
                    }

                    @Override
                    public void onCompleted() {
                        Log.i("-=-=-", "onCompleted");
                    }
                });
    }

    /**
     * 添加粉丝
     *
     * @param myFansBean
     * @param callBack
     */
    public void add(MyFansBean myFansBean, AddFansCallBack callBack) {
        ManagedChannel channel = getManagedChannel();
        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(myFansBean.voAccessToken.accessToken,
                        Long.valueOf(myFansBean.voAccessToken.expiresIn));
        UserFansApiGrpc.UserFansApiStub userFansApiStub = UserFansApiGrpc.newStub(channel);
        userFansApiStub.withCallCredentials(callCredentials)
                .add(AddFanshipRequest.newBuilder()
                        .setFanUuid(myFansBean.fanUuid)
                        .build(), new StreamObserver<FanshipResponse>() {
                    @Override
                    public void onNext(FanshipResponse value) {
                        callBack.onAddFansCallBack(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        callBack.onAddFansCallBack(FanshipResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode(Status.Code.UNKNOWN)
                                        .setDetails("网络不佳，请稍后重试")
                                        .build())
                                .build());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });

    }


}

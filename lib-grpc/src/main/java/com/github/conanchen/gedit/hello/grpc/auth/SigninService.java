package com.github.conanchen.gedit.hello.grpc.auth;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.hello.grpc.store.StoreService;
import com.github.conanchen.gedit.user.auth.grpc.SigninResponse;
import com.github.conanchen.gedit.user.auth.grpc.SigninWithPasswordRequest;
import com.github.conanchen.gedit.user.auth.grpc.UserAuthApiGrpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Administrator on 2018/1/11.
 */

public class SigninService {
    private final static String TAG = StoreService.class.getSimpleName();

    public interface SigninCallback {
        void onSigninResponse(SigninResponse response);

    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    public void login(SigninInfo signinInfo, SigninService.SigninCallback callback) {
        Log.i(TAG, signinInfo.mobile + "==SigninService==" + signinInfo.password);
        ManagedChannel channel = getManagedChannel();

        UserAuthApiGrpc.UserAuthApiStub userAuthApiBlockingStub = UserAuthApiGrpc.newStub(channel);
        userAuthApiBlockingStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .signinWithPassword(SigninWithPasswordRequest.newBuilder()
                                .setMobile(signinInfo.mobile)
                                .setPassword(signinInfo.password)
                                .build(),
                        new StreamObserver<SigninResponse>() {
                            @Override
                            public void onNext(SigninResponse value) {
                                callback.onSigninResponse(value);
                                Status status = value.getStatus();
                                String code = status.getCode();
                                Log.i(TAG, "staus:" + status + ",code:" + code);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e(TAG, t.getMessage());
                                callback.onSigninResponse(
                                        SigninResponse.newBuilder()
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

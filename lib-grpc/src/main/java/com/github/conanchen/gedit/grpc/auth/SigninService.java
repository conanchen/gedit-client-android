package com.github.conanchen.gedit.grpc.auth;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.grpc.store.StoreProfileService;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.user.auth.grpc.SigninResponse;
import com.github.conanchen.gedit.user.auth.grpc.SigninWithPasswordRequest;
import com.github.conanchen.gedit.user.auth.grpc.UserAuthApiGrpc;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Administrator on 2018/1/11.
 */

public class SigninService {
    private final static String TAG = StoreProfileService.class.getSimpleName();
    private Gson gson = new Gson();

    public interface SigninCallback {
        void onSigninResponse(SigninResponse response);

    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_AUTH)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    public void login(SigninInfo signinInfo, SigninService.SigninCallback callback) {
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
                                Log.i("-=-=-=-", "value" + gson.toJson(value));
                                callback.onSigninResponse(value);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i("-=-=-=-", "Throwable" + gson.toJson(t));
                                callback.onSigninResponse(
                                        SigninResponse.newBuilder()
                                                .setStatus(Status.newBuilder()
                                                        .setCode(Status.Code.UNKNOWN)
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

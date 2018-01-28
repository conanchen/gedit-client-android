package com.github.conanchen.gedit.grpc.auth;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.grpc.GrpcApiCallback;
import com.github.conanchen.gedit.grpc.store.StoreProfileService;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.user.profile.grpc.GetMyProfileRequest;
import com.github.conanchen.gedit.user.profile.grpc.UserProfileApiGrpc;
import com.github.conanchen.gedit.user.profile.grpc.UserProfileResponse;
import com.github.conanchen.gedit.utils.JcaUtils;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.gson.Gson;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Administrator on 2018/1/26.
 */

public class MySummaryService {
    private final static String TAG = StoreProfileService.class.getSimpleName();
    private Gson gson = new Gson();


    public interface UserProfileCallBack extends GrpcApiCallback {
        void onUserProfileCallBack(UserProfileResponse userProfileResponse);
    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_AUTH)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    public void userProfile(VoAccessToken voAccessToken, UserProfileCallBack callBack) {
        ManagedChannel channel = getManagedChannel();
        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(voAccessToken.accessToken,
                        Long.valueOf(voAccessToken.expiresIn));
        UserProfileApiGrpc.UserProfileApiStub userProfileApiStub = UserProfileApiGrpc.newStub(channel);
        userProfileApiStub
                .withCallCredentials(callCredentials)
                .getMyProfile(GetMyProfileRequest.newBuilder()
                                .build(),
                        new StreamObserver<UserProfileResponse>() {
                            @Override
                            public void onNext(UserProfileResponse value) {
                                Log.i("-=-=-=-=-onNext", gson.toJson(value));
                                callBack.onUserProfileCallBack(value);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i("-=-=-=-=-onError", gson.toJson(t));
                                callBack.onGrpcApiError(Status.newBuilder()
                                        .setCode(Status.Code.UNKNOWN)
                                        .setDetails("网络不好，请稍后重试")
                                        .build());
                            }

                            @Override
                            public void onCompleted() {
                                callBack.onGrpcApiCompleted();
                            }
                        });
    }
}

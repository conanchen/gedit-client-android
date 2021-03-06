package com.github.conanchen.gedit.grpc.auth;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.grpc.GrpcApiCallback;
import com.github.conanchen.gedit.grpc.store.StoreProfileService;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.user.auth.grpc.RegisterResponse;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep1QuestionRequest;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep1QuestionResponse;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep2AnswerRequest;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep2AnswerResponse;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep3RegisterRequest;
import com.github.conanchen.gedit.user.auth.grpc.UserAuthApiGrpc;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Administrator on 2018/1/13.
 */

public class RegisterService {
    private final static String TAG = StoreProfileService.class.getSimpleName();
    private Gson gson = new Gson();

    public interface RegisterVerifyCallback extends GrpcApiCallback {
        void onRegisterVerifyResponse(SmsStep1QuestionResponse response);
    }

    public interface RegisterSmsCallback extends GrpcApiCallback {
        void onRegisterSmsCallback(SmsStep2AnswerResponse response);
    }

    public interface RegisterCallback {
        void onRegisterCallback(RegisterResponse response);
    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_AUTH)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    public void getVerify(RegisterInfo registerInfo, RegisterService.RegisterVerifyCallback callback) {
        ManagedChannel channel = getManagedChannel();

        UserAuthApiGrpc.UserAuthApiStub userAuthApiBlockingStub = UserAuthApiGrpc.newStub(channel);
        userAuthApiBlockingStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .registerSmsStep1Question(SmsStep1QuestionRequest.newBuilder()
                                .build(),
                        new StreamObserver<SmsStep1QuestionResponse>() {
                            @Override
                            public void onNext(SmsStep1QuestionResponse value) {
                                callback.onRegisterVerifyResponse(value);
                            }

                            @Override
                            public void onError(Throwable t) {
                                callback.onGrpcApiError(Status.newBuilder()
                                        .setCode(Status.Code.UNAVAILABLE)
                                        .setDetails("网络不好，请稍后重试")
                                        .build());
                            }

                            @Override
                            public void onCompleted() {
                                callback.onGrpcApiCompleted();
                                Log.i(TAG, "storeProfileApiStub.create onCompleted()");
                            }
                        });
    }

    /**
     * 获取短信验证码
     *
     * @param registerInfo
     * @param callback
     */
    public void getMsm(RegisterInfo registerInfo, RegisterService.RegisterSmsCallback callback) {
        ManagedChannel channel = getManagedChannel();
        UserAuthApiGrpc.UserAuthApiStub userAuthApiStub = UserAuthApiGrpc.newStub(channel);


        List<String> list = new ArrayList<>();
        for (int i = 0; i < registerInfo.question.size(); i++) {
            String uuid = registerInfo.question.get(i).getUuid();
            list.add(uuid);
        }

        Iterable<String> iterable = new ArrayList<String>(list);

        userAuthApiStub.registerSmsStep2Answer(SmsStep2AnswerRequest.newBuilder()
                .setMobile(registerInfo.mobile)
                .setToken(registerInfo.token)
                .addAllQuestionUuid(iterable)
                .build(), new StreamObserver<SmsStep2AnswerResponse>() {
            @Override
            public void onNext(SmsStep2AnswerResponse value) {
                callback.onRegisterSmsCallback(value);
            }

            @Override
            public void onError(Throwable t) {
                callback.onGrpcApiError(Status.newBuilder()
                        .setCode(Status.Code.UNKNOWN)
                        .setDetails("网络不好，请稍后重试")
                        .build());
            }

            @Override
            public void onCompleted() {
                callback.onGrpcApiCompleted();
            }
        });
    }

    /**
     * 注册
     *
     * @param registerInfo
     * @param callback
     */
    public void getRegister(RegisterInfo registerInfo, RegisterService.RegisterCallback callback) {
        ManagedChannel channel = getManagedChannel();
        UserAuthApiGrpc.UserAuthApiStub userAuthApiStub = UserAuthApiGrpc.newStub(channel);
        userAuthApiStub.
                registerSmsStep3Register(SmsStep3RegisterRequest.newBuilder()
                        .setMobile(registerInfo.mobile)
                        .setSmscode(registerInfo.smscode)
                        .setPassword(registerInfo.password)
                        .build(), new StreamObserver<RegisterResponse>() {
                    @Override
                    public void onNext(RegisterResponse value) {
                        Log.i("-=-=register", gson.toJson(value));
                        callback.onRegisterCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=register", t.getMessage());
                        callback.onRegisterCallback(RegisterResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode(Status.Code.UNKNOWN)
                                        .setDetails("api没有调通" + t.getMessage()))
                                .build());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

}

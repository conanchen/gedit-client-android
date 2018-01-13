package com.github.conanchen.gedit.hello.grpc.auth;

import android.util.Log;

import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.hello.grpc.store.StoreService;
import com.github.conanchen.gedit.user.auth.grpc.Question;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep1QuestionResponse;
import com.github.conanchen.gedit.user.auth.grpc.UserAuthApiGrpc;
import com.google.protobuf.Empty;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Administrator on 2018/1/13.
 */

public class RegisterService {
    private final static String TAG = StoreService.class.getSimpleName();

    public interface RegisterVerifyCallback {
        void onRegisterVerifyResponse(SmsStep1QuestionResponse response);

    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    public void getVerify(RegisterInfo registerInfo, RegisterService.RegisterVerifyCallback callback) {
        ManagedChannel channel = getManagedChannel();

        UserAuthApiGrpc.UserAuthApiStub userAuthApiBlockingStub = UserAuthApiGrpc.newStub(channel);
        userAuthApiBlockingStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .registerSmsStep1Question(Empty.newBuilder()
                                .build(),
                        new StreamObserver<SmsStep1QuestionResponse>() {
                            @Override
                            public void onNext(SmsStep1QuestionResponse value) {

                                List<Question> questionList = value.getQuestionList();
                                String token = value.getToken();
                                String questionTip = value.getQuestionTip();
                                Log.i(TAG, "token:" + token + "questionTip:" + questionTip);
                                callback.onRegisterVerifyResponse(value);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e(TAG, t.getMessage());
                                callback.onRegisterVerifyResponse(
                                        SmsStep1QuestionResponse.newBuilder()
                                                .setToken("get token is fail")
                                                .setQuestionTip("get tip is fail")
                                                .build());
                            }

                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "storeProfileApiStub.create onCompleted()");
                            }
                        });
    }
}

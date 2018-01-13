package com.github.conanchen.gedit.repository.hello;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.conanchen.gedit.hello.grpc.auth.RegisterInfo;
import com.github.conanchen.gedit.hello.grpc.auth.RegisterService;
import com.github.conanchen.gedit.hello.grpc.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep1QuestionResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/13.
 */

public class RegisterRepository {
    private final static String TAG = StoreRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public RegisterRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }


    public LiveData<RegisterInfo> getVerify(RegisterInfo registerInfo) {
        return new LiveData<RegisterInfo>() {
            @Override
            protected void onActive() {
                grpcFascade.registerService.getVerify(registerInfo, new RegisterService.RegisterVerifyCallback() {
                    @Override
                    public void onRegisterVerifyResponse(SmsStep1QuestionResponse response) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("SmsStep1QuestionResponse: %s", response.getToken()));
                            RegisterInfo register = RegisterInfo.builder()
                                    .setToken(response.getToken())
                                    .setQuestionTip(response.getQuestionTip())
                                    .setQuestion(response.getQuestionList())
                                    .build();
                            return register;
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<RegisterInfo>() {
                                    @Override
                                    public void accept(@NonNull RegisterInfo registerInfo) throws Exception {
                                        // the uuid of the upserted record.
                                        if (registerInfo != null) {
                                            Log.i(TAG, gson.toJson(registerInfo));
                                            setValue(RegisterInfo.builder()
                                                    .setToken(response.getToken())
                                                    .setQuestionTip(response.getQuestionTip())
                                                    .build());
                                        } else {
                                            setValue(RegisterInfo.builder()
                                                    .setToken(response.getToken())
                                                    .setQuestionTip(response.getQuestionTip())
                                                    .build());
                                        }

                                    }
                                });
                        ;
                    }
                });
            }
        };
    }


}

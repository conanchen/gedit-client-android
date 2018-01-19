package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.conanchen.gedit.grpc.auth.RegisterInfo;
import com.github.conanchen.gedit.grpc.auth.RegisterService;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.user.auth.grpc.RegisterResponse;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep1QuestionResponse;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep2AnswerResponse;
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

    /**
     * 获取注册时需要的图片验证
     *
     * @param registerInfo
     * @return
     */
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

    /**
     * 获取短信验证码
     *
     * @param registerInfo
     * @return
     */
    public LiveData<SmsStep2AnswerResponse> getSms(RegisterInfo registerInfo) {
        return new LiveData<SmsStep2AnswerResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.registerService.getMsm(registerInfo, new RegisterService.RegisterSmsCallback() {
                    @Override
                    public void onRegisterSmsCallback(SmsStep2AnswerResponse response) {
                        Observable.fromCallable(() -> {
                            // TODO: 2018/1/18  数据需要处理
                            SmsStep2AnswerResponse smsStep2AnswerResponse = SmsStep2AnswerResponse.newBuilder()
                                    .setStatus(response.getStatus())
                                    .build();
                            return smsStep2AnswerResponse;
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<SmsStep2AnswerResponse>() {
                                    @Override
                                    public void accept(@NonNull SmsStep2AnswerResponse smsStep2AnswerResponse) throws Exception {
                                        // the uuid of the upserted record.
                                        if (registerInfo != null) {
                                            Log.i(TAG, gson.toJson(registerInfo));
                                            setValue(SmsStep2AnswerResponse.newBuilder()
                                                    .setStatus(response.getStatus())
                                                    .build());

                                        } else {
                                            setValue(SmsStep2AnswerResponse.newBuilder()
                                                    .setStatus(response.getStatus())
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

    /**
     * 注册
     *
     * @param registerInfo
     * @return
     */
    public LiveData<RegisterResponse> getRegister(RegisterInfo registerInfo) {
        return new LiveData<RegisterResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.registerService.getRegister(registerInfo, new RegisterService.RegisterCallback() {
                    @Override
                    public void onRegisterCallback(RegisterResponse response) {
                        Observable.fromCallable(() -> {
                            // TODO: 2018/1/18  数据需要处理
                            RegisterResponse signinResponse = RegisterResponse.newBuilder()
                                    .setStatus(response.getStatus())
                                    .build();
                            return signinResponse;
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<RegisterResponse>() {
                                    @Override
                                    public void accept(@NonNull RegisterResponse signinResponse) throws Exception {
                                        // the uuid of the upserted record.
                                        if (registerInfo != null) {
                                            Log.i(TAG, gson.toJson(registerInfo));
                                            setValue(RegisterResponse.newBuilder()
                                                    .setStatus(response.getStatus())
                                                    .build());

                                        } else {
                                            setValue(RegisterResponse.newBuilder()
                                                    .setStatus(response.getStatus())
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

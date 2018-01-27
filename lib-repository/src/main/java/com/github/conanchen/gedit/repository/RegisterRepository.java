package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.auth.RegisterInfo;
import com.github.conanchen.gedit.grpc.auth.RegisterService;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.user.auth.grpc.RegisterResponse;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep1QuestionResponse;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep2AnswerResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/13.
 */

public class RegisterRepository {
    private final static String TAG = StoreProfileRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;
    private RepositoryFascade repositoryFascade;

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
                        RegisterInfo register = RegisterInfo.builder()
                                .setToken(response.getToken())
                                .setQuestionTip(response.getQuestionTip())
                                .setQuestion(response.getQuestionList())
                                .build();
                        setValue(register);
                    }

                    @Override
                    public void onGrpcApiError(Status status) {
                        RegisterInfo register = RegisterInfo.builder()
                                .setStatus(status)
                                .build();
                        setValue(register);
                    }

                    @Override
                    public void onGrpcApiCompleted() {

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
                        SmsStep2AnswerResponse smsStep2AnswerResponse = SmsStep2AnswerResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode(response.getStatus().getCode())
                                        .setDetails(response.getStatus().getDetails())
                                        .build())
                                .build();
                        setValue(smsStep2AnswerResponse);
                    }

                    @Override
                    public void onGrpcApiError(Status status) {

                    }

                    @Override
                    public void onGrpcApiCompleted() {

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
                grpcFascade.registerService.getRegister(registerInfo, (registerResponse) -> {
                    setValue(registerResponse);

                });
            }
        };
    }

}

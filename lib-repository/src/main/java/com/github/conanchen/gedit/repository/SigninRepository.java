package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.auth.SigninInfo;
import com.github.conanchen.gedit.grpc.auth.SigninService;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.room.kv.Value;
import com.github.conanchen.gedit.user.auth.grpc.SigninResponse;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/11.
 */

@Singleton
public class SigninRepository {
    private final static String TAG = StoreProfileRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public SigninRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }


    public LiveData<SigninResponse> login(SigninInfo signinInfo) {
        return new LiveData<SigninResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.signinService.login(signinInfo, new SigninService.SigninCallback() {
                    @Override
                    public void onSigninResponse(com.github.conanchen.gedit.user.auth.grpc.SigninResponse response) {
                        Observable.fromCallable(() -> {
                            Log.i("-=-=-", gson.toJson(response));

                            if (Status.Code.OK.getNumber() == response.getStatus().getCode().getNumber()) {
                                //登录成功
                                VoAccessToken voAccessToken = VoAccessToken.builder()
                                        .setAccessToken(response.getAccessToken())
                                        .setExpiresIn(response.getExpiresIn())
                                        .build();

                                Value value = Value.builder()
                                        .setVoAccessToken(voAccessToken)
                                        .build();

                                KeyValue keyValue = KeyValue.builder()
                                        .setKey(KeyValue.KEY.USER_CURRENT_ACCESSTOKEN)
                                        .setValue(value)
                                        .build();

                                //这里是做测试，保存WorkingStore的payeeStoreUuid
                                roomFascade.daoKeyValue.save(KeyValue.builder()
                                        .setKey(KeyValue.KEY.USER_CURRENT_WORKING_STORE)
                                        .setValue(Value.builder()
                                                .setPayeeStoreUuid("123456")
                                                .build())
                                        .build());
                                return roomFascade.daoKeyValue.save(keyValue);
                            } else {
                                return new Long(-1);
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(@NonNull Long rowId) throws Exception {
                                        // the uuid of the upserted record.
                                        if (rowId > 0) {
                                            setValue(SigninResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(Status.Code.OK)
                                                            .setDetails("Signin successful")
                                                            .build())
                                                    .setAccessToken(response.getAccessToken())
                                                    .setExpiresIn(response.getExpiresIn())
                                                    .build());
                                        } else {
                                            setValue(SigninResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(response.getStatus().getCode())
                                                            .setDetails(response.getStatus().getDetails())
                                                            .build())
                                                    .setAccessToken(response.getAccessToken())
                                                    .setExpiresIn(response.getExpiresIn())
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

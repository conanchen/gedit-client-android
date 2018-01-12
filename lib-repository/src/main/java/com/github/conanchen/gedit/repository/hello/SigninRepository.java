package com.github.conanchen.gedit.repository.hello;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.conanchen.gedit.hello.grpc.auth.SigninInfo;
import com.github.conanchen.gedit.hello.grpc.auth.SigninService;
import com.github.conanchen.gedit.hello.grpc.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.room.kv.Value;
import com.github.conanchen.gedit.room.kv.VoAccessToken;
import com.github.conanchen.gedit.vo.SigninResponse;
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
    private final static String TAG = StoreRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public SigninRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }


    public LiveData<SigninResponse> login(SigninInfo signinInfo) {
        Log.i(TAG, signinInfo.mobile + "==SigninRepository==" + signinInfo.password);
        return new LiveData<SigninResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.signinService.login(signinInfo, new SigninService.SigninCallback() {
                    @Override
                    public void onSigninResponse(com.github.conanchen.gedit.user.auth.grpc.SigninResponse response) {
                        Observable.fromCallable(() -> {
                            Log.i(TAG, String.format("UpdateResponse: %s", response.getStatus()));
                            if ("OK".compareToIgnoreCase(response.getStatus().getCode()) == 0) {
                                //登录成功
                                VoAccessToken voAccessToken = VoAccessToken.builder()
                                        .setAccessToken(response.getAccessToken())
                                        .setExpiresIn(response.getExpiresIn())
                                        .build();

                                Value value = Value.builder()
                                        .setVoAccessToken(voAccessToken)
                                        .build();

                                KeyValue keyValue = KeyValue.builder()
                                        .setKey(response.getAccessToken())
                                        .setValue(value)
                                        .build();
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
                                            setValue(SigninResponse.builder()
                                                    .setAccessToken("OK")
                                                    .setExpiresIn("user login Successful")
                                                    .build());
                                        } else {
                                            setValue(SigninResponse.builder()
                                                    .setAccessToken(response.getStatus().getCode())
                                                    .setExpiresIn(response.getStatus().getDetails())
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

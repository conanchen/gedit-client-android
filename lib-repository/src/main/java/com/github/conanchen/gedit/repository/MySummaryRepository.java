package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.auth.MySummaryService;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.room.kv.Value;
import com.github.conanchen.gedit.user.profile.grpc.UserProfileResponse;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.github.conanchen.utils.vo.VoUserProfile;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/26.
 */
@Singleton
public class MySummaryRepository {
    private final static String TAG = StoreProfileRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public MySummaryRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }

    /**
     * 获取用户个人资料，比如：昵称，电话，logo等。。
     *
     * @param voAccessToken
     * @return
     */
    public LiveData<UserProfileResponse> userProfile(VoAccessToken voAccessToken) {
        return new LiveData<UserProfileResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.mySummaryService.userProfile(voAccessToken, new MySummaryService.UserProfileCallBack() {
                    @Override
                    public void onUserProfileCallBack(UserProfileResponse userProfileResponse) {
                        Observable.fromCallable(() -> {
                            Log.i("-=-=-", gson.toJson(userProfileResponse));
                            if (Status.Code.OK == userProfileResponse.getStatus().getCode()) {
                                KeyValue keyValue = KeyValue.builder()
                                        .setKey(KeyValue.KEY.USER_CURRENT_USER_PROFILE)
                                        .setValue(Value.builder()
                                                .setVoUserProfile(VoUserProfile.builder()
                                                        .setUuid(userProfileResponse.getUserProfile().getUuid())
                                                        .setName(userProfileResponse.getUserProfile().getName())
                                                        .setMobile(userProfileResponse.getUserProfile().getMobile())
                                                        .setLogo(userProfileResponse.getUserProfile().getLogo())
                                                        .setDistrictId(userProfileResponse.getUserProfile().getDistrictId())
                                                        .setDesc(userProfileResponse.getUserProfile().getDesc())
                                                        .build())
                                                .build())
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
                                            setValue(UserProfileResponse.newBuilder()
                                                    .setUserProfile(userProfileResponse.getUserProfile())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(Status.Code.OK)
                                                            .setDetails("save userProfile successful")
                                                            .build())
                                                    .build());
                                        } else {
                                            setValue(UserProfileResponse.newBuilder()
                                                    .setUserProfile(userProfileResponse.getUserProfile())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(userProfileResponse.getStatus().getCode())
                                                            .setDetails(userProfileResponse.getStatus().getDetails())
                                                            .build())
                                                    .build());
                                        }
                                    }
                                });
                        ;
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

}

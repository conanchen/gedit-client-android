package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.repository.RepositoryFascade;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.user.profile.grpc.UserProfile;
import com.github.conanchen.gedit.user.profile.grpc.UserProfileResponse;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.github.conanchen.utils.vo.VoLoadGrpcStatus;
import com.github.conanchen.utils.vo.VoUserProfile;
import com.github.conanchen.utils.vo.VoWorkingStore;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/11.
 */

public class CurrentSigninViewModel extends ViewModel {
    public static final String TAG = CurrentSigninViewModel.class.getSimpleName();
    private final static Gson gson = new Gson();

    private final RepositoryFascade repositoryFascade;

    @SuppressWarnings("unchecked")
    @Inject
    public CurrentSigninViewModel(RepositoryFascade repositoryFascade) {
        this.repositoryFascade = repositoryFascade;
    }

    /**
     * 判断用户时候登录  查看本地数据库是否有token
     *
     * @return
     */
    public LiveData<com.github.conanchen.gedit.user.auth.grpc.SigninResponse> getCurrentSigninResponse() {
        return new LiveData<com.github.conanchen.gedit.user.auth.grpc.SigninResponse>() {
            @Override
            protected void onActive() {
                repositoryFascade.keyValueRepository
                        .findMaybe(KeyValue.KEY.USER_CURRENT_ACCESSTOKEN)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .map(keyValue -> keyValue.value.voAccessToken)
                        .subscribe(new MaybeObserver<VoAccessToken>() {
                            boolean hasValue = false;

                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.i(TAG, "onSubscribe");
                            }

                            @Override
                            public void onSuccess(VoAccessToken voAccessToken) {
                                //found accesstoken
                                Log.i(TAG, String.format("onSuccess voAccessToken=%s", gson.toJson(voAccessToken)));
                                com.github.conanchen.gedit.user.auth.grpc.SigninResponse signinResponse = com.github.conanchen.gedit.user.auth.grpc.SigninResponse.newBuilder()
                                        .setStatus(com.github.conanchen.gedit.common.grpc.Status.newBuilder()
                                                .setCode(com.github.conanchen.gedit.common.grpc.Status.Code.OK)
                                                .setDetails("already logon!")
                                                .build())
                                        .setAccessToken(voAccessToken.accessToken)
                                        .setExpiresIn(voAccessToken.expiresIn)
                                        .build();
                                postValue(signinResponse);
                            }

                            @Override
                            public void onError(Throwable e) {
                                //access database error
                                Log.i(TAG, String.format("onError Throwable=%s", e.getMessage()));
                                com.github.conanchen.gedit.user.auth.grpc.SigninResponse signinResponse = com.github.conanchen.gedit.user.auth.grpc.SigninResponse.newBuilder()
                                        .setStatus(com.github.conanchen.gedit.common.grpc.Status.newBuilder()
                                                .setCode(com.github.conanchen.gedit.common.grpc.Status.Code.UNAUTHENTICATED)
                                                .setDetails("error,not logon!")
                                                .build())
                                        .build();
                                postValue(signinResponse);
                            }

                            @Override
                            public void onComplete() {
                                //not found accesstoken
                                Log.i(TAG, String.format("onComplete hasValue=%b", hasValue));
                                com.github.conanchen.gedit.user.auth.grpc.SigninResponse signinResponse = com.github.conanchen.gedit.user.auth.grpc.SigninResponse.newBuilder()
                                        .setStatus(com.github.conanchen.gedit.common.grpc.Status.newBuilder()
                                                .setCode(com.github.conanchen.gedit.common.grpc.Status.Code.UNAUTHENTICATED)
                                                .setDetails("complete,not logon!")
                                                .build())
                                        .build();

                                postValue(signinResponse);

                            }
                        });
            }

        };
    }

    /**
     * 查询WorkingStore的profile
     *
     * @return
     */
    public LiveData<VoWorkingStore> getCurrentWorkingStore() {
        return new LiveData<VoWorkingStore>() {
            @Override
            protected void onActive() {
                repositoryFascade.keyValueRepository
                        .findMaybe(KeyValue.KEY.USER_CURRENT_WORKING_STORE)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .map(keyValue -> keyValue.value.voWorkingStore)
                        .subscribe(new MaybeObserver<VoWorkingStore>() {

                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.i(TAG, "onSubscribe");
                            }

                            @Override
                            public void onSuccess(VoWorkingStore voWorkingStore) {
                                //found payeeStoreUuid
                                Log.i(TAG, "onSuccess  Working Store ");
                                postValue(voWorkingStore);
                            }

                            @Override
                            public void onError(Throwable e) {
                                //access database error
                                Log.i(TAG, "onError  Working Store ");
                                VoWorkingStore voWorkingStore = VoWorkingStore.builder()
                                        .build();
                                postValue(voWorkingStore);
                            }

                            @Override
                            public void onComplete() {
                                //not found payeeStoreUuid
                                Log.i(TAG, "onComplete  Working Store ");

                            }
                        });
            }

        };
    }

    /**
     * 获取我的个人资料
     *
     * @return
     */
    public LiveData<UserProfileResponse> getMyProfile() {
        return new LiveData<UserProfileResponse>() {
            @Override
            protected void onActive() {
                repositoryFascade.keyValueRepository
                        .findMaybe(KeyValue.KEY.USER_CURRENT_USER_PROFILE)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .map(keyValue -> keyValue.value.voUserProfile)
                        .subscribe(new MaybeObserver<VoUserProfile>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.i(TAG, "onSubscribe");
                            }

                            @Override
                            public void onSuccess(VoUserProfile voUserProfile) {
                                Log.i(TAG, "onSuccess  userProfile ");
                                UserProfileResponse userProfileResponse = UserProfileResponse.newBuilder()
                                        .setStatus(Status.newBuilder()
                                                .setCode(Status.Code.OK)
                                                .setDetails("get userprofile successful")
                                                .build())
                                        .setUserProfile(UserProfile.newBuilder()
                                                .setUuid(voUserProfile.uuid)
                                                .setUsername(voUserProfile.name)
                                                .setMobile(voUserProfile.mobile)
                                                .setLogo(voUserProfile.logo)
                                                .setDesc(voUserProfile.desc)
                                                .setDistrictUuid(voUserProfile.districtId))
                                        .build();
                                postValue(userProfileResponse);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i(TAG, String.format("onError Throwable=%s", e.getMessage()));
                                UserProfileResponse userProfileResponse = UserProfileResponse.newBuilder()
                                        .setStatus(Status.newBuilder()
                                                .setCode(Status.Code.UNKNOWN)
                                                .setDetails("error,not get profile!")
                                                .build())
                                        .build();
                                postValue(userProfileResponse);
                            }

                            @Override
                            public void onComplete() {
                                UserProfileResponse userProfileResponse = UserProfileResponse.newBuilder()
                                        .setStatus(com.github.conanchen.gedit.common.grpc.Status.newBuilder()
                                                .setCode(Status.Code.UNKNOWN)
                                                .setDetails("error,not get profile!")
                                                .build())
                                        .build();
                                postValue(userProfileResponse);
                            }
                        });

            }
        };
    }

    /**
     * 从数据库获取联网的状态   联网时走的GrpcApiError方法
     *
     * @return
     */
    public LiveData<VoLoadGrpcStatus> getGrpcApiStatus() {
        return new LiveData<VoLoadGrpcStatus>() {
            @Override
            protected void onActive() {
                repositoryFascade.keyValueRepository
                        .findMaybe(KeyValue.KEY.LOAD_GRPC_API_STATUS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .map(keyValue -> keyValue.value.voLoadGrpcStatus)
                        .subscribe(new MaybeObserver<VoLoadGrpcStatus>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(VoLoadGrpcStatus voLoadGrpcStatus) {
                                //获取成功之后就直接把值返还出去
                                postValue(voLoadGrpcStatus);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }
        };
    }
}

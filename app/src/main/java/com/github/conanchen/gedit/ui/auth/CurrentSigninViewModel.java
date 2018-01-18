package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.github.conanchen.gedit.repository.RepositoryFascade;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.grpc.Status;
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
                                                .setCode(Status.Code.OK.name())
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
                                                .setCode(Status.Code.UNAUTHENTICATED.name())
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
                                                .setCode(Status.Code.UNAUTHENTICATED.name())
                                                .setDetails("complete,not logon!")
                                                .build())
                                        .build();

                                postValue(signinResponse);

                            }
                        });
            }

        };
    }
}

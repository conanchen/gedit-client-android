package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.github.conanchen.gedit.GeditApplication;
import com.github.conanchen.gedit.hello.grpc.auth.SigninInfo;
import com.github.conanchen.gedit.repository.RepositoryFascade;
import com.github.conanchen.gedit.repository.hello.SigninRepository;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.room.kv.VoAccessToken;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.gedit.vo.SigninResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.grpc.Status;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/11.
 */

public class SigninViewModel extends ViewModel {
    public static final String TAG = SigninViewModel.class.getSimpleName();
    private final static Gson gson = new Gson();

    @VisibleForTesting
    final MutableLiveData<SigninInfo> loginInfoMutableLiveData = new MutableLiveData<>();
    private final LiveData<SigninResponse> signinResponseLiveData;
    private final RepositoryFascade repositoryFascade;

    @SuppressWarnings("unchecked")
    @Inject
    public SigninViewModel(RepositoryFascade repositoryFascade) {
        this.repositoryFascade = repositoryFascade;
        signinResponseLiveData = Transformations.switchMap(loginInfoMutableLiveData, signinInfo -> {
            if (signinInfo == null) {
                return AbsentLiveData.create();
            } else {
                return repositoryFascade.signinRepository.login(signinInfo);
            }
        });

    }

    @VisibleForTesting
    public LiveData<SigninResponse> getSigninResponseLiveData() {
        return signinResponseLiveData;
    }


    public void login(SigninInfo loginInfo) {
        loginInfoMutableLiveData.setValue(loginInfo);
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
                                                .setCode(io.grpc.Status.Code.OK.name())
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

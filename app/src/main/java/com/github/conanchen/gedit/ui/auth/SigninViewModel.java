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

}

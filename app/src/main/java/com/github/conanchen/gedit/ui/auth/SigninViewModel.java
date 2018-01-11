package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.hello.grpc.auth.SigninInfo;
import com.github.conanchen.gedit.repository.hello.SigninRepository;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.gedit.vo.SigninResponse;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/11.
 */

public class SigninViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<SigninInfo> loginInfoMutableLiveData = new MutableLiveData<>();
    private final LiveData<SigninResponse> loginLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public SigninViewModel(SigninRepository signinRepository) {
        loginLiveData = Transformations.switchMap(loginInfoMutableLiveData, signinInfo -> {
            if (signinInfo == null) {
                return AbsentLiveData.create();
            } else {
                return signinRepository.login(signinInfo);
            }
        });

    }

    @VisibleForTesting
    public LiveData<SigninResponse> getSigninResponseLiveData() {
        return loginLiveData;
    }


    public void login(SigninInfo loginInfo) {
        loginInfoMutableLiveData.setValue(loginInfo);
    }


}

package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.hello.grpc.auth.RegisterInfo;
import com.github.conanchen.gedit.hello.grpc.auth.SigninInfo;
import com.github.conanchen.gedit.repository.RepositoryFascade;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/13.
 */

public class RegisterViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<RegisterInfo> registerInfoMutableLiveData = new MutableLiveData<>();
    private final LiveData<RegisterInfo> registerResponseLiveData;
    private final RepositoryFascade repositoryFascade;

    @SuppressWarnings("unchecked")
    @Inject
    public RegisterViewModel(RepositoryFascade repositoryFascade) {
        this.repositoryFascade = repositoryFascade;
        registerResponseLiveData = Transformations.switchMap(registerInfoMutableLiveData, registerInfo -> {
            if (registerInfo == null) {
                return AbsentLiveData.create();
            } else {
                return repositoryFascade.registerRepository.getVerify(registerInfo);
            }
        });

    }

    @VisibleForTesting
    public LiveData<RegisterInfo> getRegisterResponseLiveData() {
        return registerResponseLiveData;
    }


    public void getVerify(RegisterInfo registerInfo) {
        registerInfoMutableLiveData.setValue(registerInfo);
    }
}

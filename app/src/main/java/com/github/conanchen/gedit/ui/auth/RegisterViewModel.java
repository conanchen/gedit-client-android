package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.grpc.auth.RegisterInfo;
import com.github.conanchen.gedit.repository.RepositoryFascade;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.room.kv.Value;
import com.github.conanchen.gedit.user.auth.grpc.RegisterResponse;
import com.github.conanchen.gedit.user.auth.grpc.SmsStep2AnswerResponse;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.utils.vo.VoAccessToken;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/13.
 */

public class RegisterViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<RegisterInfo> registerInfoMutableLiveData = new MutableLiveData<>();
    private final LiveData<RegisterInfo> registerResponseLiveData;

    @VisibleForTesting
    final MutableLiveData<RegisterInfo> loadMsmInfoLiveData = new MutableLiveData<>();
    private final LiveData<SmsStep2AnswerResponse> loadMsmInfo;

    @VisibleForTesting
    final MutableLiveData<RegisterInfo> registerLiveData = new MutableLiveData<>();
    private final LiveData<RegisterResponse> register;

    private RepositoryFascade repositoryFascade;

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

        loadMsmInfo = Transformations.switchMap(loadMsmInfoLiveData, registerInfo -> {
            if (registerInfo == null) {
                return AbsentLiveData.create();
            } else {
                return repositoryFascade.registerRepository.getSms(registerInfo);
            }
        });

        register = Transformations.switchMap(registerLiveData, registerInfo -> {
            if (registerInfo == null) {
                return AbsentLiveData.create();
            } else {
                return repositoryFascade.registerRepository.getRegister(registerInfo);
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


    @VisibleForTesting
    public LiveData<SmsStep2AnswerResponse> getRegisterSmsLiveData() {
        return loadMsmInfo;
    }


    public void getSms(RegisterInfo registerInfo) {
        loadMsmInfoLiveData.setValue(registerInfo);
    }


    @VisibleForTesting
    public LiveData<RegisterResponse> getRegisterLiveData() {
        return register;
    }


    public void getSRegister(RegisterInfo registerInfo) {
        registerLiveData.setValue(registerInfo);
    }


    public void saveRegisterOKAccessToken(RegisterResponse registerResponse) {
        Observable.just(true)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    repositoryFascade.keyValueRepository.saveToken(KeyValue.builder()
                            .setKey(KeyValue.KEY.USER_CURRENT_ACCESSTOKEN)
                            .setValue(Value.builder()
                                    .setVoAccessToken(VoAccessToken.builder()
                                            .setAccessToken(registerResponse.getAccessToken())
                                            .setExpiresIn(registerResponse.getExpiresIn())
                                            .build())
                                    .build())
                            .build());
                });
    }
}

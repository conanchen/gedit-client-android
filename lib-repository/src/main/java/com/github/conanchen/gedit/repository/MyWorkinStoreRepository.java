package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.store.MyWorkinStoreService;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.room.kv.Value;
import com.github.conanchen.gedit.store.worker.grpc.WorkshipResponse;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.github.conanchen.utils.vo.VoWorkingStore;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Conan Chen on 2018/1/8.
 */
@Singleton
public class MyWorkinStoreRepository {
    private final static String TAG = MyWorkinStoreRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public MyWorkinStoreRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }


    /**
     * 获取工作商铺的详情
     * @param voAccessToken
     * @return
     */
    public LiveData<WorkshipResponse> getMyCurrentWorkinStore(VoAccessToken voAccessToken) {
        return new LiveData<WorkshipResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.myWorkinStoreService.getMyCurrentWorkinStore(voAccessToken, new MyWorkinStoreService.WorkingStoreCallBack() {
                    @Override
                    public void onWorkingStoreCallBack(WorkshipResponse workshipResponse) {
                        Observable.fromCallable(() -> {
                            if (Status.Code.OK == workshipResponse.getStatus().getCode()) {
                                KeyValue keyValue = KeyValue.builder()
                                        .setKey(KeyValue.KEY.USER_CURRENT_WORKING_STORE)
                                        .setValue(Value.builder()
                                                .setVoWorkingStore(VoWorkingStore.builder()
                                                        .setStoreUuid(workshipResponse.getOwnership().getStoreUuid())
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
                                    public void accept(Long id) throws Exception {
                                        setValue(WorkshipResponse.newBuilder()
                                                .setOwnership(workshipResponse.getOwnership())
                                                .setStatus(workshipResponse.getStatus())
                                                .build());
                                    }
                                });
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

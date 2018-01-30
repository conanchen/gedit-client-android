package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.store.StoreWorkerService;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.room.kv.Value;
import com.github.conanchen.gedit.room.store.StoreWorker;
import com.github.conanchen.gedit.store.worker.grpc.WorkshipResponse;
import com.github.conanchen.utils.vo.PaymentInfo;
import com.github.conanchen.utils.vo.VoLoadGrpcStatus;
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
public class StoreWorkerRepository {
    private final static String TAG = StoreWorkerRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public StoreWorkerRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }


    private static final PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
            .setEnablePlaceholders(true)
            .setPrefetchDistance(10)
            .setPageSize(20).build();


    public LiveData<PagedList<StoreWorker>> loadAllEmployees(PaymentInfo paymentInfo) {
        Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
            grpcFascade.storeWorkerService.loadAllEmployees(paymentInfo, new StoreWorkerService.ListByWorkerCallBack() {
                @Override
                public void onListByWorkerCallBack(WorkshipResponse response) {
                    if (Status.Code.OK == response.getStatus().getCode()) {
                        StoreWorker storeWorker = StoreWorker.builder()
                                .setStoreUuid(response.getOwnership().getStoreUuid())
                                .setStoreLogo(response.getOwnership().getStoreLogo())
                                .setStoreName(response.getOwnership().getStoreName())
                                .build();
                        roomFascade.daoStoreWorker.save(storeWorker);
                    }
                }

                @Override
                public void onGrpcApiError(Status status) {
                    saveGrpcStatus(VoLoadGrpcStatus.builder()
                            .setStatus(status.getCode().toString())
                            .setMessage("网络不佳，请稍后重试")
                            .build());
                }

                @Override
                public void onGrpcApiCompleted() {
                    saveGrpcStatus(VoLoadGrpcStatus.builder()
                            .setStatus("COMPLETED")
                            .setMessage("暂无更多数据")
                            .build());
                }
            });
        });

        return (new LivePagedListBuilder(roomFascade.daoStoreWorker.listLivePagedStore(), pagedListConfig))
                .build();
    }

    private void saveGrpcStatus(VoLoadGrpcStatus voLoadGrpcStatus) {
        KeyValue keyValue = KeyValue.builder()
                .setKey(KeyValue.KEY.LOAD_GRPC_API_STATUS)
                .setValue(Value.builder()
                        .setVoLoadGrpcStatus(voLoadGrpcStatus)
                        .build())
                .build();
//        roomFascade.daoKeyValue.save(keyValue);
    }

    /**
     * 添加员工
     *
     * @return
     */
    public LiveData<WorkshipResponse> addWorker(PaymentInfo paymentInfo) {
        return new LiveData<WorkshipResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.storeWorkerService.addWorker(paymentInfo, new StoreWorkerService.AddWorkerCallBack() {
                    @Override
                    public void onAddWorkerCallBack(WorkshipResponse response) {
                        Observable.fromCallable(() -> {
                            if (Status.Code.OK == response.getStatus().getCode()) {
                                StoreWorker storeWorker = StoreWorker.builder()
                                        .setUuid(response.getOwnership().getUuid())
                                        .setStoreUuid(response.getOwnership().getStoreUuid())
                                        .setCreated(response.getOwnership().getCreated())
                                        .setStoreLogo(response.getOwnership().getStoreLogo())
                                        .setUserLogo(response.getOwnership().getUserLogo())
                                        .setUserName(response.getOwnership().getUserName())
                                        .setStoreName(response.getOwnership().getStoreName())
                                        .setLat(response.getOwnership().getLocation().getLat())
                                        .setLon(response.getOwnership().getLocation().getLon())
                                        .setUserUuid(response.getOwnership().getUserUuid())
                                        .setLastUpdated(response.getOwnership().getLastUpdated())
                                        .build();

                                return roomFascade.daoStoreWorker.save(storeWorker);
                            } else {
                                return new Long(-1);
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(@NonNull Long rowId) throws Exception {
                                        setValue(WorkshipResponse.newBuilder()
                                                .setOwnership(response.getOwnership())
                                                .setStatus(Status.newBuilder()
                                                        .setCode(response.getStatus().getCode())
                                                        .setDetails(response.getStatus().getDetails())
                                                        .build())
                                                .build());
                                    }
                                });
                        ;
                    }
                });
            }
        };

    }


}

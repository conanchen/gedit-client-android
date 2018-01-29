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
        return (new LivePagedListBuilder(roomFascade.daoStoreWorker.listLivePagedStore(), pagedListConfig))
                .setBoundaryCallback(new PagedList.BoundaryCallback() {
                    @Override
                    public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
                        grpcFascade.storeWorkerService.loadAllEmployees(paymentInfo, new StoreWorkerService.ListByWorkerCallBack() {
                            @Override
                            public void onListByWorkerCallBack(WorkshipResponse response) {
                                Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
                                    if (Status.Code.OK == response.getStatus().getCode()) {
                                        StoreWorker storeWorker = StoreWorker.builder()
                                                .setStoreUuid(response.getOwnership().getStoreUuid())
                                                .setStoreLogo(response.getOwnership().getStoreLogo())
                                                .setStoreName(response.getOwnership().getStoreName())
                                                .build();
                                        roomFascade.daoStoreWorker.save(storeWorker);
                                    }
                                });
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
                    }
                })
                .build();
    }

    private void saveGrpcStatus(VoLoadGrpcStatus voLoadGrpcStatus) {
        Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
            KeyValue keyValue = KeyValue.builder()
                    .setKey(KeyValue.KEY.LOAD_GRPC_API_STATUS)
                    .setValue(Value.builder()
                            .setVoLoadGrpcStatus(voLoadGrpcStatus)
                            .build())
                    .build();
            roomFascade.daoKeyValue.save(keyValue);
        });

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
                            if (Status.Code.OK.getNumber() == response.getStatus().getCode().getNumber()) {
                                return response;
                            } else {
                                WorkshipResponse workshipResponse = WorkshipResponse.newBuilder()
                                        .setStatus(Status.newBuilder()
                                                .setCode(response.getStatus().getCode())
                                                .setDetails(response.getStatus().getDetails())
                                                .build())
                                        .build();
                                return workshipResponse;
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<WorkshipResponse>() {
                                    @Override
                                    public void accept(@NonNull WorkshipResponse workshipResponse) throws Exception {
                                        if (Status.Code.OK.getNumber() == workshipResponse.getStatus().getCode().getNumber()) {
                                            setValue(WorkshipResponse.newBuilder()
                                                    .setOwnership(workshipResponse.getOwnership())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(Status.Code.UNKNOWN)
                                                            .setDetails("add worker successfully")
                                                            .build())
                                                    .build());
                                        } else {
                                            setValue(WorkshipResponse.newBuilder()
                                                    .setOwnership(workshipResponse.getOwnership())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(workshipResponse.getStatus().getCode())
                                                            .setDetails(workshipResponse.getStatus().getDetails())
                                                            .build())
                                                    .build());
                                        }
                                    }
                                });
                        ;
                    }
                });
            }
        };

    }


}

package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.store.StoreWorkerService;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.store.StoreWorker;
import com.github.conanchen.gedit.store.worker.grpc.ListWorkshipByWorkerRequest;
import com.github.conanchen.gedit.store.worker.grpc.WorkshipResponse;
import com.github.conanchen.utils.vo.PaymentInfo;
import com.github.conanchen.utils.vo.StoreUpdateInfo;
import com.github.conanchen.utils.vo.VoAccessToken;
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
            grpcFascade.storeWorkerService.loadAllEmployees(paymentInfo, response -> {
                if (Status.Code.OK == response.getStatus().getCode()) {
                    StoreWorker storeWorker = StoreWorker.builder()
                            .setStoreUuid(response.getOwnership().getStoreUuid())
                            .setStoreLogo(response.getOwnership().getStoreLogo())
                            .setStoreName(response.getOwnership().getStoreName())
                            .build();
                    roomFascade.daoStoreWorker.save(storeWorker);
                }
            });
        });
        return (new LivePagedListBuilder(roomFascade.daoStoreWorker.listLivePagedStore(), pagedListConfig)).build();
    }


    /**
     * 添加员工
     *
     * @return
     */
    public LiveData<WorkshipResponse> addWorker(StoreUpdateInfo storeUpdateInfo) {
        return new LiveData<WorkshipResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.storeWorkerService.addWorker(storeUpdateInfo, new StoreWorkerService.AddWorkerCallBack() {
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

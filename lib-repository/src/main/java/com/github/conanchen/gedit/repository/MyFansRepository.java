package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.fan.MyFansService;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.my.fan.Fanship;
import com.github.conanchen.gedit.user.fans.grpc.FanshipResponse;
import com.github.conanchen.gedit.user.fans.grpc.ListMyFanRequest;
import com.github.conanchen.utils.vo.MyFansBean;
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
public class MyFansRepository {
    private final static String TAG = MyFansRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public MyFansRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }


    private static final PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
            .setEnablePlaceholders(true)
            .setPrefetchDistance(10)
            .setPageSize(20).build();


    public LiveData<PagedList<Fanship>> loadMyFanships(MyFansBean myFansBean) {
        Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe((mBoolean) -> {
            grpcFascade.myFansService.loadMyFans(myFansBean, new MyFansService.FanshipCallBack() {
                @Override
                public void onFanshipResponse(FanshipResponse response) {
                    if (Status.Code.OK == response.getStatus().getCode()) {
                        Fanship fanship = Fanship.builder()
                                .setFanName(response.getFanship().getFanName())
                                .setFanUuid(response.getFanship().getFanUuid())
                                .setCreated(response.getFanship().getCreated())
                                .build();
                        roomFascade.daoFanship.save(fanship);
                    }
                }
            });
        });

        return new LivePagedListBuilder(roomFascade.daoFanship.listLivePagedFanship(), pagedListConfig).build();
    }


    public LiveData<FanshipResponse> addFans(MyFansBean myFansBean) {
        return new LiveData<FanshipResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.myFansService.add(myFansBean, new MyFansService.AddFansCallBack() {
                    @Override
                    public void onAddFansCallBack(FanshipResponse response) {
                        Observable.fromCallable(() -> {
                            if (Status.Code.OK == response.getStatus().getCode()) {
                                Fanship fanship = Fanship.builder()
                                        .setFanName(response.getFanship().getFanName())
                                        .setFanUuid(response.getFanship().getFanUuid())
                                        .setCreated(response.getFanship().getCreated())
                                        .build();

                                return roomFascade.daoFanship.save(fanship);
                            } else {
                                return new Long(-1);
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(@NonNull Long rowId) throws Exception {
                                        setValue(FanshipResponse.newBuilder()
                                                .setFanship(response.getFanship())
                                                .setStatus(response.getStatus())
                                                .build());
                                    }
                                });
                    }
                });
            }
        };
    }
}

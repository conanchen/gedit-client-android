package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.my.fan.Fanship;
import com.github.conanchen.gedit.user.fans.grpc.ListMyFanRequest;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
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


    public LiveData<PagedList<Fanship>> loadMyFanships(Long times) {
        Observable.just(true).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(aBoolean -> {
            grpcFascade.myFansService.loadMyFans(
                    ListMyFanRequest.newBuilder().build(), response -> {
                        Fanship myFanship = Fanship.builder()
                                .setFanUuid(response.getFanship().getFanUuid())
                                .setFanName(response.getFanship().getFanName())
                                .setCreated(response.getFanship().getCreated())
                                .build();
                        roomFascade.daoFanship.save(myFanship);
                    });
        });

        return (new LivePagedListBuilder(roomFascade.daoFanship.listLivePagedFanship(), pagedListConfig))
                .build();
    }
}

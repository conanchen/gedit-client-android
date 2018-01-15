package com.github.conanchen.gedit.repository.hello;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import com.github.conanchen.gedit.hello.grpc.HelloReply;
import com.github.conanchen.gedit.hello.grpc.HelloService;
import com.github.conanchen.gedit.hello.grpc.di.GrpcFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.hello.Hello;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by conanchen on 2017/12/20.
 */

@Singleton
public class HelloRepository {

    private final static String TAG = HelloRepository.class.getSimpleName();

    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;

    @Inject
    public HelloRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }

    public void sayHello(String s) {
        grpcFascade.helloService.sayHello(s, new HelloService.HelloCallback() {
            @Override
            public void onHelloReply(HelloReply helloReply) {
                Observable.fromCallable(() -> {
                    Log.i(TAG, String.format("HelloReply: %s", helloReply.getMessage()));
                    if (io.grpc.Status.Code.OK.name().compareToIgnoreCase(helloReply.getStatus().getCode()) == 0) {
                        Log.i(TAG, String.format("HelloReply: %s", gson.toJson(helloReply)));
                        Hello hello = Hello.builder()
                                .setUuid(Strings.isNullOrEmpty(helloReply.getUuid()) ? "1" : helloReply.getUuid())
                                .setMessage(Strings.isNullOrEmpty(helloReply.getMessage()) ? String.format("%s", "hello is null") : helloReply.getMessage())
                                .setCreated(helloReply.getCreated())
                                .setLastUpdated(helloReply.getLastUpdated())
                                .build();
                        return roomFascade.daoHello.save(hello);
                    } else {
                        Hello hello = Hello.builder()
                                .setUuid(UUID.randomUUID().toString())
                                .setMessage(String.format("Hello 有错：%s:%s", helloReply.getStatus().getCode(), helloReply.getStatus().getDetails()))
                                .setCreated(System.currentTimeMillis())
                                .setLastUpdated(System.currentTimeMillis())
                                .build();
                        return roomFascade.daoHello.save(hello);
                    }

                }).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Log.i(TAG, String.format("save rowId=%d", aLong));
                            }
                        });
            }
        });
    }

    private static final PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
            .setEnablePlaceholders(true)
            .setPrefetchDistance(10)
            .setPageSize(20).build();


    public LiveData<PagedList<Hello>> loadHellos(Long time) {
        Log.i("-=-=-=-", "hello 查询");
        return (new LivePagedListBuilder(roomFascade.daoHello.listLivePagedHellos(), pagedListConfig))
                .build();
    }
}

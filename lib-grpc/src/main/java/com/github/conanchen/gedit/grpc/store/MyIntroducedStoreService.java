package com.github.conanchen.gedit.grpc.store;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Location;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.store.introducer.grpc.Introducership;
import com.github.conanchen.gedit.store.introducer.grpc.IntroducershipResponse;
import com.github.conanchen.gedit.store.introducer.grpc.ListMyIntroducedStoreRequest;
import com.github.conanchen.gedit.store.introducer.grpc.StoreIntroducerApiGrpc;
import com.github.conanchen.gedit.store.owner.grpc.OwnershipResponse;

import java.util.UUID;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class MyIntroducedStoreService {
    private final static String TAG = MyIntroducedStoreService.class.getSimpleName();

    public interface OwnershipCallBack {
        void onIntroducershipResponse(IntroducershipResponse response);

    }


    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_AUTH)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }



    public void loadMyIntroducedStores(ListMyIntroducedStoreRequest request, OwnershipCallBack callBack) {
        ManagedChannel channel = getManagedChannel();
        StoreIntroducerApiGrpc.StoreIntroducerApiStub storeIntroducerApiStub = StoreIntroducerApiGrpc.newStub(channel);
        storeIntroducerApiStub.listMyIntroducedStore(request, new StreamObserver<IntroducershipResponse>() {
            @Override
            public void onNext(IntroducershipResponse value) {
                callBack.onIntroducershipResponse(value);
            }

            @Override
            public void onError(Throwable t) {
                Log.i("-=-=-", "onError");
                callBack.onIntroducershipResponse(IntroducershipResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode("FAILED")
                                .setDetails(String.format("API访问错误，可能网络不通！error:%s", t.getMessage()))
                                .build())
                        .setIntroducership(Introducership.newBuilder()
                                .setStoreUuid(UUID.randomUUID().toString())
                                .setLocation(Location.newBuilder().setLat(234).setLon(232).build())
                                .setStoreLogo("logo url"+System.currentTimeMillis())
                                .setStoreName("strore name "+ System.currentTimeMillis())
                                .build())
                        .build());
            }

            @Override
            public void onCompleted() {
                Log.i("-=-=-", "onCompleted");
                callBack.onIntroducershipResponse(IntroducershipResponse.newBuilder()
                        .setStatus(Status.newBuilder().setCode("onCompleted()")
                                .setDetails(String.format("onCompleted（）"))
                                .build())
                        .setIntroducership(Introducership.newBuilder()
                                .setStoreUuid(UUID.randomUUID().toString())
                                .setLocation(Location.newBuilder().setLat(234).setLon(232).build())
                                .setStoreLogo("logo url"+System.currentTimeMillis())
                                .setStoreName("strore name "+ System.currentTimeMillis())
                                .build())
                        .build());
            }
        });
    }


}

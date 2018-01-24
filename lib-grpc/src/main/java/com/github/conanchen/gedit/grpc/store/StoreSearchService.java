package com.github.conanchen.gedit.grpc.store;

import android.util.Log;

import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.store.search.grpc.SearchStoreRequest;
import com.github.conanchen.gedit.store.search.grpc.SearchStoreResponse;
import com.github.conanchen.gedit.store.search.grpc.StoreSearchApiGrpc;
import com.google.gson.Gson;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Conan Chen on 2018/1/8.
 */

@Singleton
public class StoreSearchService {
    private final static String TAG = StoreSearchService.class.getSimpleName();
    private Gson gson = new Gson();

    public interface StoreSearchCallback {
        void onStoreSearchResponse(SearchStoreResponse response);

    }


    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_STORESEARCH)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }


    public void searchStoresNearAt(SearchStoreRequest searchRequest, StoreSearchCallback callback) {
        ManagedChannel channel = getManagedChannel();
        StoreSearchApiGrpc.StoreSearchApiStub storeSearchApiStub = StoreSearchApiGrpc.newStub(channel);
        storeSearchApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .search(searchRequest, new StreamObserver<SearchStoreResponse>() {
                    @Override
                    public void onNext(SearchStoreResponse value) {
                        callback.onStoreSearchResponse(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        callback.onStoreSearchResponse(SearchStoreResponse.newBuilder()
                                .setUuid(UUID.randomUUID().toString())
                                .setName(String.format("StoreName%d", System.currentTimeMillis()))
                                .setDesc(String.format("StoreDesc%d", System.currentTimeMillis()))
                                .build());
                    }

                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "storeSearchApiStub.search onCompleted()");

                    }
                });
    }


}

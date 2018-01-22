package com.github.conanchen.gedit.grpc.store;

import android.support.annotation.Nullable;
import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Location;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.store.profile.grpc.CreateStoreResponse;
import com.github.conanchen.gedit.store.profile.grpc.GetStoreRequest;
import com.github.conanchen.gedit.store.profile.grpc.StoreProfileApiGrpc;
import com.github.conanchen.gedit.store.profile.grpc.StoreProfileResponse;
import com.github.conanchen.gedit.store.profile.grpc.UpdateStoreRequest;
import com.github.conanchen.gedit.store.profile.grpc.UpdateStoreResponse;
import com.github.conanchen.gedit.store.search.grpc.SearchStoreRequest;
import com.github.conanchen.gedit.store.search.grpc.SearchStoreResponse;
import com.github.conanchen.gedit.store.search.grpc.StoreSearchApiGrpc;
import com.github.conanchen.gedit.utils.JcaUtils;
import com.github.conanchen.utils.vo.StoreCreateInfo;
import com.github.conanchen.utils.vo.StoreUpdateInfo;
import com.google.gson.Gson;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Conan Chen on 2018/1/8.
 */

@Singleton
public class StoreService {
    private final static String TAG = StoreService.class.getSimpleName();
    private Gson gson = new Gson();

    public interface StoreSearchCallback {
        void onStoreSearchResponse(SearchStoreResponse response);

    }

    public interface StoreGetCallback {
        void onStoreGetResponse(StoreProfileResponse response);

    }

    public interface StoreCreateCallback {
        void onStoreCreateResponse(CreateStoreResponse response);

    }

    public interface UpdateCallback {
        void onUpdateStoreResponse(UpdateStoreResponse response);
    }

    public interface UpdateHeadPortraitCallback {
        void onUpdateHeadPortraitResponse(UpdateStoreResponse response);
    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_AUTH)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    public void downloadStoreProfile(GetStoreRequest request, StoreGetCallback callback) {
        ManagedChannel channel = getManagedChannel();
        StoreProfileApiGrpc.StoreProfileApiStub storeProfileApiStub = StoreProfileApiGrpc.newStub(channel);
        storeProfileApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .get(request, new StreamObserver<StoreProfileResponse>() {
                    @Override
                    public void onNext(StoreProfileResponse value) {
                        callback.onStoreGetResponse(value);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });

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
                        callback.onStoreSearchResponse(SearchStoreResponse.newBuilder().setUuid(UUID.randomUUID().toString())
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

    public void storeCreate(StoreCreateInfo storeCreateInfo, StoreCreateCallback callback) {
        ManagedChannel channel = getManagedChannel();

        StoreProfileApiGrpc.StoreProfileApiStub storeProfileApiStub = StoreProfileApiGrpc.newStub(channel);
        storeProfileApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .create(com.github.conanchen.gedit.store.profile.grpc.CreateStoreRequest
                                .newBuilder()
                                .setName(storeCreateInfo.name)
                                .setLocation(Location.newBuilder().setLon(storeCreateInfo.lon).setLat(storeCreateInfo.lat).build())
                                .setDistrictUuid(storeCreateInfo.districtUuid)
                                .setDetailAddress(storeCreateInfo.detailAddress)
                                .build(),
                        new StreamObserver<CreateStoreResponse>() {
                            @Override
                            public void onNext(CreateStoreResponse value) {
                                Log.i("-=-=-=---", "onNext  ====  value:" + gson.toJson(value));
                                callback.onStoreCreateResponse(value);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i("-=-=-=---", "onError  ====  value:" + gson.toJson(t));
                                callback.onStoreCreateResponse(
                                        CreateStoreResponse.newBuilder()
                                                .setStatus(Status.newBuilder().setCode("FAILED")
                                                        .setDetails(String.format("API访问错误，可能网络不通！error:%s", t.getMessage()))
                                                        .build())
                                                .build());
                            }

                            @Override
                            public void onCompleted() {
                                Log.i("-=-=-=---", "onCompleted");
                            }
                        });

    }


    public void updateStore(StoreUpdateInfo storeUpdateInfo, StoreService.UpdateCallback callback) {

        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(storeUpdateInfo.voAccessToken.accessToken,
                        Long.valueOf(storeUpdateInfo.voAccessToken.expiresIn));

        ManagedChannel channel = getManagedChannel();

        StoreProfileApiGrpc.StoreProfileApiStub storeProfileApiStub = StoreProfileApiGrpc.newStub(channel);
        storeProfileApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .withCallCredentials(callCredentials)
                .update(getUpdateStoreRequest(storeUpdateInfo),
                        new StreamObserver<UpdateStoreResponse>() {
                            @Override
                            public void onNext(UpdateStoreResponse value) {
                                Log.i(TAG, "enter onNext()");
                                callback.onUpdateStoreResponse(value);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i(TAG, "enter onError()");
                                callback.onUpdateStoreResponse(
                                        UpdateStoreResponse.newBuilder()
                                                .setStatus(Status.newBuilder().setCode("FAILED")
                                                        .setDetails(String.format("API访问错误，可能网络不通！error:%s", t.getMessage()))
                                                        .build())
                                                .build());
                            }

                            @Override
                            public void onCompleted() {

                            }
                        });

    }

    @Nullable
    private UpdateStoreRequest getUpdateStoreRequest(StoreUpdateInfo storeUpdateInfo) {
        UpdateStoreRequest updateStoreRequest = null;
        switch (storeUpdateInfo.name) {
            case PHONE:
                updateStoreRequest = UpdateStoreRequest.newBuilder()
                        .setUuid(storeUpdateInfo.uuid)
                        .setName((String) storeUpdateInfo.value)
                        .build();
                break;
            case DESC:
                updateStoreRequest = UpdateStoreRequest.newBuilder()
                        .setUuid(storeUpdateInfo.uuid)
                        .setDesc((String) storeUpdateInfo.value)
                        .build();
                break;
            case DETAIL_ADDRESS:
                updateStoreRequest = UpdateStoreRequest.newBuilder()
                        .setUuid(storeUpdateInfo.uuid)
                        .setDetailAddress((String) storeUpdateInfo.value)
                        .build();
                break;
        }
        return updateStoreRequest;
    }


    /**
     * 修改头像
     *
     * @param storeUpdateInfo 修改内容封装的对象
     * @param callback
     */
    public void updateHeadPortrait(StoreUpdateInfo storeUpdateInfo, StoreService.UpdateHeadPortraitCallback callback) {
        ManagedChannel channel = getManagedChannel();

        StoreProfileApiGrpc.StoreProfileApiStub storeProfileApiStub = StoreProfileApiGrpc.newStub(channel);
        storeProfileApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .update(com.github.conanchen.gedit.store.profile.grpc.UpdateStoreRequest.newBuilder()
                                .setUuid(storeUpdateInfo.uuid)
                                .build(),
                        new StreamObserver<UpdateStoreResponse>() {
                            @Override
                            public void onNext(UpdateStoreResponse value) {
                                Log.i(TAG, "enter onNext()");
                                callback.onUpdateHeadPortraitResponse(value);
                                Status status = value.getStatus();
                                String code = status.getCode();
                                Log.i(TAG, "staus:" + status + ",code:" + code);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i(TAG, "enter onError()");
                                Log.e(TAG, t.getMessage());
                                callback.onUpdateHeadPortraitResponse(
                                        UpdateStoreResponse.newBuilder()
                                                .setStatus(Status.newBuilder().setCode("FAILED")
                                                        .setDetails(String.format("API访问错误，可能网络不通！error:%s", t.getMessage()))
                                                        .build())
                                                .build());
                            }

                            @Override
                            public void onCompleted() {

                            }
                        });

    }


    /**
     * 修改地区
     *
     * @param storeUpdateInfo
     * @param callback
     */
    public void updateAddress(StoreUpdateInfo storeUpdateInfo, StoreService.UpdateHeadPortraitCallback callback) {
        ManagedChannel channel = getManagedChannel();

        StoreProfileApiGrpc.StoreProfileApiStub storeProfileApiStub = StoreProfileApiGrpc.newStub(channel);
        storeProfileApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .update(com.github.conanchen.gedit.store.profile.grpc.UpdateStoreRequest.newBuilder()
                                .setUuid(storeUpdateInfo.uuid)
                                .build(),
                        new StreamObserver<UpdateStoreResponse>() {
                            @Override
                            public void onNext(UpdateStoreResponse value) {
                                Log.i(TAG, "enter onNext()");
                                callback.onUpdateHeadPortraitResponse(value);
                                Status status = value.getStatus();
                                String code = status.getCode();
                                Log.i(TAG, "staus:" + status + ",code:" + code);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i(TAG, "enter onError()");
                                Log.e(TAG, t.getMessage());
                                callback.onUpdateHeadPortraitResponse(
                                        UpdateStoreResponse.newBuilder()
                                                .setStatus(Status.newBuilder().setCode("FAILED")
                                                        .setDetails(String.format("API访问错误，可能网络不通！error:%s", t.getMessage()))
                                                        .build())
                                                .build());
                            }

                            @Override
                            public void onCompleted() {

                            }
                        });

    }
}

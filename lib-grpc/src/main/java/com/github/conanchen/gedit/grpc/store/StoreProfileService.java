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
import com.github.conanchen.gedit.utils.JcaUtils;
import com.github.conanchen.utils.vo.StoreCreateInfo;
import com.github.conanchen.utils.vo.StoreUpdateInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
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
public class StoreProfileService {
    private final static String TAG = StoreProfileService.class.getSimpleName();
    private Gson gson = new Gson();


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
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_STORE)
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


    public void storeCreate(StoreCreateInfo storeCreateInfo, StoreCreateCallback callback) {
        ManagedChannel channel = getManagedChannel();
        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(storeCreateInfo.voAccessToken.accessToken
                        , Long.valueOf(storeCreateInfo.voAccessToken.expiresIn));

        StoreProfileApiGrpc.StoreProfileApiStub storeProfileApiStub = StoreProfileApiGrpc.newStub(channel);
        storeProfileApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .withCallCredentials(callCredentials)
                .create(com.github.conanchen.gedit.store.profile.grpc.CreateStoreRequest
                                .newBuilder()
                                .setName(storeCreateInfo.name)
                                .setLocation(Location.newBuilder().setLon(storeCreateInfo.lon).setLat(storeCreateInfo.lat).build())
                                .setIntroducerMobile(storeCreateInfo.mobile)
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
                                                .setStatus(Status.newBuilder()
                                                        .setCode(Status.Code.UNKNOWN)
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


    public void updateStore(StoreUpdateInfo storeUpdateInfo, StoreProfileService.UpdateCallback callback) {

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
                                                .setStatus(Status.newBuilder()
                                                        .setCode(Status.Code.UNKNOWN)
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
                //修改电话   门店电话可以有很多个
                Iterable<String> listTel = new ArrayList<String>((List<String>) storeUpdateInfo.value);
                updateStoreRequest = UpdateStoreRequest.newBuilder()
                        .setUuid(storeUpdateInfo.uuid)
//                        .setTels(ListTel.newBuilder().addAllUrls(listTel).build())
                        .build();
                break;
            case DESC:
                //修改描述
                updateStoreRequest = UpdateStoreRequest.newBuilder()
                        .setUuid(storeUpdateInfo.uuid)
                        .setDesc((String) storeUpdateInfo.value)
                        .build();
                break;
            case DETAIL_ADDRESS:
                //修改详细地址
                updateStoreRequest = UpdateStoreRequest.newBuilder()
                        .setUuid(storeUpdateInfo.uuid)
                        .setDetailAddress((String) storeUpdateInfo.value)
                        .build();
                break;
            case POINTS_RATE:
                //消费可得百分之几的积分
                int pointsRate = 0;
                try {
                    pointsRate = Integer.parseInt((String) storeUpdateInfo.value);
                } catch (NumberFormatException e) {
                    pointsRate = 0;
                }

                updateStoreRequest = UpdateStoreRequest.newBuilder()
                        .setUuid(storeUpdateInfo.uuid)
                        .setPointsRate(pointsRate)
                        .build();
                break;
            case LIST_URL:
                //门店展示
                Iterable<String> listUrl = new ArrayList<String>((List<String>) storeUpdateInfo.value);
                updateStoreRequest = UpdateStoreRequest.newBuilder()
                        .setUuid(storeUpdateInfo.uuid)
//                        .setImages(ListURL.newBuilder().addAllUrls(listUrl).build())
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
    public void updateHeadPortrait(StoreUpdateInfo storeUpdateInfo, StoreProfileService.UpdateHeadPortraitCallback callback) {
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
                                String code = status.getCode().name();
                                Log.i(TAG, "staus:" + status + ",code:" + code);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i(TAG, "enter onError()");
                                Log.e(TAG, t.getMessage());
                                callback.onUpdateHeadPortraitResponse(
                                        UpdateStoreResponse.newBuilder()
                                                .setStatus(Status.newBuilder().setCode(Status.Code.UNKNOWN)
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
    public void updateAddress(StoreUpdateInfo storeUpdateInfo, StoreProfileService.UpdateHeadPortraitCallback callback) {
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
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i(TAG, "enter onError()");
                                Log.e(TAG, t.getMessage());
                                callback.onUpdateHeadPortraitResponse(
                                        UpdateStoreResponse.newBuilder()
                                                .setStatus(Status.newBuilder().setCode(Status.Code.UNKNOWN)
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

package com.github.conanchen.gedit.grpc.store;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.store.worker.grpc.AddWorkershipRequest;
import com.github.conanchen.gedit.store.worker.grpc.ListWorkshipByWorkerRequest;
import com.github.conanchen.gedit.store.worker.grpc.StoreWorkerApiGrpc;
import com.github.conanchen.gedit.store.worker.grpc.WorkshipResponse;
import com.github.conanchen.gedit.utils.JcaUtils;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class StoreWorkerService {
    private final static String TAG = StoreWorkerService.class.getSimpleName();
    private Gson gson = new Gson();

    public interface ListByWorkerCallBack {
        void onListByWorkerCallBack(WorkshipResponse response);
    }

    public interface AddWorkerCallBack {
        void onAddWorkerCallBack(WorkshipResponse response);

    }


    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_AUTH)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }


    public void loadAllEmployees(VoAccessToken voAccessToken, ListWorkshipByWorkerRequest request, ListByWorkerCallBack callBack) {
        ManagedChannel channel = getManagedChannel();
        StoreWorkerApiGrpc.StoreWorkerApiStub storeWorkerApiStub = StoreWorkerApiGrpc.newStub(channel);

        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(voAccessToken.accessToken,
                        Long.valueOf(voAccessToken.expiresIn));

        storeWorkerApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .withCallCredentials(callCredentials)
                .listByWorker(request, new StreamObserver<WorkshipResponse>() {
                    @Override
                    public void onNext(WorkshipResponse value) {
                        Log.i("-=-=-", "onNext==========" + gson.toJson(value));
                        callBack.onListByWorkerCallBack(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-", "onError==========" + gson.toJson(t));
                        callBack.onListByWorkerCallBack(WorkshipResponse.newBuilder()
                                .setStatus(Status.newBuilder().setCode("FAILED")
                                        .setDetails(String.format("API访问错误，可能网络不通！error:%s", t.getMessage()))
                                        .build())
                                .build());
                    }

                    @Override
                    public void onCompleted() {
                        Log.i("-=-=-", "onCompleted");
                    }
                });
    }

    /**
     * 添加员工
     *
     * @param workerUuid
     * @param callBack
     */
    public void addWorker(String workerUuid, AddWorkerCallBack callBack) {
        ManagedChannel channel = getManagedChannel();
        StoreWorkerApiGrpc.StoreWorkerApiStub storeWorkerApiStub = StoreWorkerApiGrpc.newStub(channel);
        storeWorkerApiStub
                .add(AddWorkershipRequest.newBuilder().setWorkerUuid(workerUuid).build(), new StreamObserver<WorkshipResponse>() {
                    @Override
                    public void onNext(WorkshipResponse value) {
                        Log.i("-=-=-", "onNext==========" + gson.toJson(value));
                        callBack.onAddWorkerCallBack(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-", "onError==========" + gson.toJson(t));
                        callBack.onAddWorkerCallBack(WorkshipResponse.newBuilder()
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

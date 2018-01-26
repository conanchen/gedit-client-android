package com.github.conanchen.gedit.grpc.accounting;

import android.util.Log;

import com.github.conanchen.gedit.accounting.posting.grpc.AccountingPostingApiGrpc;
import com.github.conanchen.gedit.accounting.posting.grpc.ListMyPostingRequest;
import com.github.conanchen.gedit.accounting.posting.grpc.Posting;
import com.github.conanchen.gedit.accounting.posting.grpc.PostingResponse;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.hello.grpc.HelloGrpc;
import com.google.gson.Gson;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.inject.Singleton;

import io.grpc.ManagedChannel;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by conanchen on 2017/12/21.
 */

@Singleton
public class PostingService {

    private final static String TAG = PostingService.class.getSimpleName();

    public interface PostingCallback {
        void onPostingResponse(PostingResponse postingResponse);
    }

    private static final Gson gson = new Gson();

    private static final Logger logger = Logger.getLogger(PostingService.class.getName());
    final HealthCheckRequest healthCheckRequest = HealthCheckRequest
            .newBuilder()
            .setService(HelloGrpc.getServiceDescriptor().getName())
            .build();

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_ACCOUNTING)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }


    public void downloadMyPostings(ListMyPostingRequest request, PostingCallback callback) {
        ManagedChannel channel = getManagedChannel();
        AccountingPostingApiGrpc.AccountingPostingApiStub postingApiStub = AccountingPostingApiGrpc.newStub(channel);
        postingApiStub.withDeadlineAfter(60, TimeUnit.SECONDS)
                .listMyPosting(
                        request, new StreamObserver<PostingResponse>() {
                            @Override
                            public void onNext(PostingResponse value) {
                                callback.onPostingResponse(value);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i(TAG, String.format("postingApiStub.listMyPosting() onError %s", t.getMessage()));
                                callback.onPostingResponse(PostingResponse.newBuilder()
                                        .setStatus(Status.newBuilder()
                                                .setCode(Status.Code.UNKNOWN)
                                                .setDetails("假数据")
                                                .build())
                                        .setPosting(Posting.newBuilder()
                                                .setUuid(UUID.randomUUID().toString())
                                                .setAccountUuid("000001")
                                                .setAmount(236)
                                                .setComment("假积分")
                                                .setCreated(System.currentTimeMillis())
                                                .build())
                                        .build());
                            }

                            @Override
                            public void onCompleted() {
                                Log.i(TAG, String.format("postingApiStub.listMyPosting() onCompleted"));
                            }
                        }
                );

    }


}

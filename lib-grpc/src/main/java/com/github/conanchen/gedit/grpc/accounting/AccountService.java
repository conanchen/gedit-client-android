package com.github.conanchen.gedit.grpc.accounting;

import android.util.Log;

import com.github.conanchen.gedit.accounting.account.grpc.AccountResponse;
import com.github.conanchen.gedit.accounting.account.grpc.AccountingAccountApiGrpc;
import com.github.conanchen.gedit.accounting.account.grpc.ListMyAccountRequest;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.hello.grpc.HelloGrpc;
import com.google.gson.Gson;

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
public class AccountService {

    private final static String TAG = AccountService.class.getSimpleName();

    public interface AccountCallback {
        void onAccountResponse(AccountResponse helloReply);
    }

    private static final Gson gson = new Gson();

    private static final Logger logger = Logger.getLogger(AccountService.class.getName());
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


    public void downloadMyAccounts(ListMyAccountRequest request, AccountCallback callback) {
        ManagedChannel channel = getManagedChannel();
        AccountingAccountApiGrpc.AccountingAccountApiStub accountApiStub = AccountingAccountApiGrpc.newStub(channel);
        accountApiStub.withDeadlineAfter(60, TimeUnit.SECONDS)
                .listMyAccount(
                        request, new StreamObserver<AccountResponse>() {
                            @Override
                            public void onNext(AccountResponse value) {
                                callback.onAccountResponse(value);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.i(TAG, String.format("helloStub.listOldHello() onError %s", t.getMessage()));

                            }

                            @Override
                            public void onCompleted() {
                                Log.i(TAG, String.format("helloStub.listOldHello() onCompleted"));
                            }
                        }
                );

    }


}

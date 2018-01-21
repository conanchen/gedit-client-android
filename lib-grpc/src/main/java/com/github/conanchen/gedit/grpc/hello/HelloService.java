package com.github.conanchen.gedit.grpc.hello;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.hello.grpc.HelloGrpc;
import com.github.conanchen.gedit.hello.grpc.HelloReply;
import com.github.conanchen.gedit.hello.grpc.HelloRequest;
import com.github.conanchen.gedit.hello.grpc.ListHelloRequest;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.inject.Singleton;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by conanchen on 2017/12/21.
 */

@Singleton
public class HelloService {

    private final static String TAG = HelloService.class.getSimpleName();

    public interface HelloCallback {
        void onHelloReply(HelloReply helloReply);
    }

    private static final Gson gson = new Gson();

    private static final Logger logger = Logger.getLogger(HelloService.class.getName());
    final HealthCheckRequest healthCheckRequest = HealthCheckRequest
            .newBuilder()
            .setService(HelloGrpc.getServiceDescriptor().getName())
            .build();

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_HELLO)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    public void sayHello(String name, HelloCallback callback) {
        ManagedChannel channel = getManagedChannel();

        ConnectivityState connectivityState = channel.getState(true);
        System.out.println(String.format("sayHello connectivityState = [%s]", gson.toJson(connectivityState)));

        HealthGrpc.HealthStub healthStub = HealthGrpc.newStub(channel);
        HelloGrpc.HelloStub helloStub = HelloGrpc.newStub(channel);

        helloStub.withDeadlineAfter(60, TimeUnit.SECONDS)
                .sayHello(HelloRequest.newBuilder().setName(name).build(), new StreamObserver<HelloReply>() {
                    @Override
                    public void onNext(HelloReply helloReply) {

                        System.out.println(String.format("sayHello got helloReply %s:%s gson=[%s]", helloReply.getUuid(), helloReply.getMessage(), gson.toJson(helloReply)));
                        callback.onHelloReply(helloReply);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i(TAG, String.format("helloStub.sayHello() onError %s", t.getMessage()));
                        callback.onHelloReply(HelloReply.newBuilder().setStatus(Status.newBuilder()
                                .setCode(io.grpc.Status.Code.UNAVAILABLE.name())
                                .setDetails("Hello API 错误：可能网络不通或服务器错误")
                                .build()).build());
                    }

                    @Override
                    public void onCompleted() {
                        Log.i(TAG, String.format("helloStub.sayHello() onCompleted"));
                    }
                });

        healthStub.withDeadlineAfter(60, TimeUnit.SECONDS).check(healthCheckRequest,
                new StreamObserver<HealthCheckResponse>() {
                    @Override
                    public void onNext(HealthCheckResponse value) {

                        if (value.getStatus() == HealthCheckResponse.ServingStatus.SERVING) {
                            System.out.println(String.format("sayHello healthStub.check onNext YES! ServingStatus.SERVING name = [%s]", name));
                        } else {
                            System.out.println(String.format("sayHello healthStub.check onNext NOT! ServingStatus.SERVING name = [%s]", name));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println(String.format("sayHello healthStub.check onError grpc service check health\n%s", t.getMessage()));
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println(String.format("sayHello healthStub.check onCompleted grpc service check health\n%s", ""));
                    }
                });

    }

    public void downloadOldHellos(ListHelloRequest request, HelloCallback callback) {
        ManagedChannel channel = getManagedChannel();
        HelloGrpc.HelloStub helloStub = HelloGrpc.newStub(channel);
        helloStub.withDeadlineAfter(60, TimeUnit.SECONDS)
                .listOldHello(
                        request, new StreamObserver<HelloReply>() {
                            @Override
                            public void onNext(HelloReply value) {
                                callback.onHelloReply(value);
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

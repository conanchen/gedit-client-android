package com.github.conanchen.gedit.grpc.payment;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.PaymentChannel;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.grpc.GrpcApiCallback;
import com.github.conanchen.gedit.grpc.store.MyIntroducedStoreService;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.payer.activeinapp.grpc.CancelPayerInappPaymentRequest;
import com.github.conanchen.gedit.payer.activeinapp.grpc.CreatePayerInappPaymentRequest;
import com.github.conanchen.gedit.payer.activeinapp.grpc.GetMyPayeeCodeRequest;
import com.github.conanchen.gedit.payer.activeinapp.grpc.GetMyPayeeCodeResponse;
import com.github.conanchen.gedit.payer.activeinapp.grpc.GetPayeeCodeRequest;
import com.github.conanchen.gedit.payer.activeinapp.grpc.GetPayeeCodeResponse;
import com.github.conanchen.gedit.payer.activeinapp.grpc.PayerActiveInappApiGrpc;
import com.github.conanchen.gedit.payer.activeinapp.grpc.PreparePayerInappPaymentRequest;
import com.github.conanchen.gedit.payer.activeinapp.grpc.PreparePayerInappPaymentResponse;
import com.github.conanchen.gedit.payment.common.grpc.PaymentResponse;
import com.github.conanchen.gedit.utils.JcaUtils;
import com.github.conanchen.utils.vo.PaymentInfo;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by hutao on 2018/1/22.
 */

public class PaymentService {
    private final static String TAG = MyIntroducedStoreService.class.getSimpleName();
    private Gson gson = new Gson();

    public interface GetQRCodeUrlCallback extends GrpcApiCallback {
        void onGetQRCodeUrlCallback(GetMyPayeeCodeResponse response);
    }

    public interface GetPayeeStoreDetailsCallback {
        void onGetPayeeStoreDetailsCallback(GetPayeeCodeResponse response);
    }

    public interface GetPaymentCallback {
        void onGetPaymentCallback(PreparePayerInappPaymentResponse response);
    }

    public interface CreatePaymentCallback {
        void onCreatePaymentCallback(PaymentResponse response);
    }

    public interface CancelPaymentCallback {
        void onCancelPaymentCallback(PaymentResponse response);
    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_PAYMENT)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * getMyPayeeCode
     *
     * @param callback
     */
    public void getQRCode(PaymentInfo paymentInfo, GetQRCodeUrlCallback callback) {

        ManagedChannel channel = getManagedChannel();

        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(paymentInfo.voAccessToken.accessToken,
                        Long.valueOf(paymentInfo.voAccessToken.expiresIn));

        PayerActiveInappApiGrpc.PayerActiveInappApiStub payerActiveInappApiStub = PayerActiveInappApiGrpc.newStub(channel);
        payerActiveInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .withCallCredentials(callCredentials)
                .getMyPayeeCode(GetMyPayeeCodeRequest.newBuilder()
                        .setPayeeStoreUuid(paymentInfo.payeeStoreUuid)
                        .build(), new StreamObserver<GetMyPayeeCodeResponse>() {
                    @Override
                    public void onNext(GetMyPayeeCodeResponse value) {
                        Log.i("-=-=-=-=--", "进了onNext()方法" + gson.toJson(value));
                        callback.onGetQRCodeUrlCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--", "onError()方法" + gson.toJson(t));
                        callback.onGrpcApiError(Status.newBuilder()
                                .setCode(Status.Code.UNKNOWN)
                                .setDetails("网络不佳，请稍后重试")
                                .build());
                    }

                    @Override
                    public void onCompleted() {
                        callback.onGrpcApiCompleted();
                    }
                });
    }


    /**
     * getPayeeCode
     *
     * @param callback
     */
    public void getPaymentStoreDetails(PaymentInfo paymentInfo, GetPayeeStoreDetailsCallback callback) {

        ManagedChannel channel = getManagedChannel();

        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(paymentInfo.voAccessToken.accessToken,
                        Long.valueOf(paymentInfo.voAccessToken.expiresIn));

        PayerActiveInappApiGrpc.PayerActiveInappApiStub payerActiveInappApiStub = PayerActiveInappApiGrpc.newStub(channel);
        payerActiveInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .withCallCredentials(callCredentials)
                .getPayeeCode(GetPayeeCodeRequest.newBuilder()
                        .setPayeeCode(paymentInfo.payeeCode)
                        .build(), new StreamObserver<GetPayeeCodeResponse>() {
                    @Override
                    public void onNext(GetPayeeCodeResponse value) {
                        Log.i("-=-=-=-=--getPayeeCode", "进了onNext()方法" + gson.toJson(value));
                        callback.onGetPayeeStoreDetailsCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--getPayeeCode", "onError()方法" + t.getMessage());
                        callback.onGetPayeeStoreDetailsCallback(GetPayeeCodeResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode(Status.Code.UNKNOWN)
                                        .setDetails("enter onError() method"))
                                .build());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    /**
     * prepare
     *
     * @param callback
     */
    public void getPayment(PaymentInfo paymentInfo, GetPaymentCallback callback) {

        ManagedChannel channel = getManagedChannel();

        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(paymentInfo.voAccessToken.accessToken,
                        Long.valueOf(paymentInfo.voAccessToken.expiresIn));

        PayerActiveInappApiGrpc.PayerActiveInappApiStub payerActiveInappApiStub = PayerActiveInappApiGrpc.newStub(channel);
        payerActiveInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .withCallCredentials(callCredentials)
                .prepare(PreparePayerInappPaymentRequest.newBuilder()
                        .setPayeeCode(paymentInfo.payeeCode)
                        .setShouldPay(paymentInfo.shouldPay)
                        .build(), new StreamObserver<PreparePayerInappPaymentResponse>() {
                    @Override
                    public void onNext(PreparePayerInappPaymentResponse value) {
                        Log.i("-=-=-=-=--prepare", "进了onNext()方法" + gson.toJson(value));
                        callback.onGetPaymentCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--prepare", "onError()方法" + t.getMessage());
                        callback.onGetPaymentCallback(PreparePayerInappPaymentResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode(Status.Code.UNKNOWN)
                                        .setDetails("enter onError() method"))
                                .build());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    /**
     * create
     *
     * @param callback
     */
    public void createPayment(PaymentInfo paymentInfo, CreatePaymentCallback callback) {

        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(paymentInfo.voAccessToken.accessToken,
                        Long.valueOf(paymentInfo.voAccessToken.expiresIn));

        CreatePayerInappPaymentRequest createPaymentRequest = null;
        if ("1".equals(paymentInfo.paymentChannelName)) {
            createPaymentRequest = CreatePayerInappPaymentRequest.newBuilder()
                    .setActualPay(paymentInfo.actualPay)
                    .setPaymentChannel(PaymentChannel.ALIPAY)
                    .setPointsPay(paymentInfo.pointsPay)
                    .setShouldPay(paymentInfo.shouldPay)
                    .setPayeeCode(paymentInfo.payeeCode)
                    .setPayerIp(paymentInfo.payerIp)
                    .build();
        } else if ("2".equals(paymentInfo.paymentChannelName)) {
            createPaymentRequest = CreatePayerInappPaymentRequest.newBuilder()
                    .setActualPay(paymentInfo.actualPay)
                    .setPaymentChannel(PaymentChannel.WECHAT)
                    .setPointsPay(paymentInfo.pointsPay)
                    .setShouldPay(paymentInfo.shouldPay)
                    .setPayeeCode(paymentInfo.payeeCode)
                    .setPayerIp(paymentInfo.payerIp)
                    .build();
        }

        ManagedChannel channel = getManagedChannel();
        PayerActiveInappApiGrpc.PayerActiveInappApiStub payerActiveInappApiStub = PayerActiveInappApiGrpc.newStub(channel);
        payerActiveInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .withCallCredentials(callCredentials)
                .create(createPaymentRequest, new StreamObserver<PaymentResponse>() {
                    @Override
                    public void onNext(PaymentResponse value) {
                        Log.i("-=-=-=-=--create", "进了onNext()方法" + gson.toJson(value));
                        callback.onCreatePaymentCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--create", "onError()方法" + gson.toJson(t));
                        callback.onCreatePaymentCallback(PaymentResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode(Status.Code.UNKNOWN)
                                        .setDetails("enter onError() method"))
                                .build());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }


    public void cancel(PaymentInfo paymentInfo, CancelPaymentCallback callback) {
        ManagedChannel channel = getManagedChannel();
        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(paymentInfo.voAccessToken.accessToken,
                        Long.valueOf(paymentInfo.voAccessToken.expiresIn));
        PayerActiveInappApiGrpc.PayerActiveInappApiStub payerActiveInappApiStub = PayerActiveInappApiGrpc.newStub(channel);
        payerActiveInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .withCallCredentials(callCredentials)
                .cancel(CancelPayerInappPaymentRequest.newBuilder()
                        .setPaymentUuid(paymentInfo.paymentUuid)
                        .build(), new StreamObserver<PaymentResponse>() {
                    @Override
                    public void onNext(PaymentResponse value) {
                        Log.i("-=-=-=-=--cancel", "进了onNext()方法" + gson.toJson(value));
                        callback.onCancelPaymentCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--cancel", "onError()方法" + gson.toJson(t));
                        callback.onCancelPaymentCallback(PaymentResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode(Status.Code.UNKNOWN)
                                        .setDetails("enter onError() method"))
                                .build());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });

    }

}

package com.github.conanchen.gedit.room.my.payment;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import com.google.common.base.Strings;

/**
 * Created by hutao on 2018/1/17.
 */
@Entity(indices = {
        @Index(value = {"uuid"}),
        @Index(value = {"created"})
})
public class Payment {

    @PrimaryKey
    @NonNull
    public String uuid; //支付单编号

    public String payerUuid; //支付人
    public String payeeUuid; //收款人
    public String payeeStoreUuid;
    public String payeeWorkerUuid;
    //create following info when return from PaymentChannel支付宝、微信等返回时
    public int shouldPay; //应付金额 = 实付金额 + 实付积分等价金额
    public int actualPay; //实付金额
    public int pointsPay; //实付积分代替金额
    public int points; //返还积分
    public String status;
//    enum Status {
//        NEW = 0;
//        INPROGRESS = 50;
//        FAILED = 51;
//        OK = 52;
//    }

    public String paymentChannel; //支付通道如支付宝、微信、云闪付
    public String channelOrderUuid; //预付单编号，可能由支付通道返回的或平台产生的

    public String signature;//微信或支付宝预定单签名，使用该签名可以直接调起支付.

    public long created;

    public Payment() {
    }

    private Payment(String uuid, String payerUuid, String payeeUuid, String payeeStoreUuid, String payeeWorkerUuid, int shouldPay, int actualPay, int pointsPay, int points, String status, String paymentChannel, String channelOrderUuid, String signature, long created) {
        this.uuid = uuid;
        this.payerUuid = payerUuid;
        this.payeeUuid = payeeUuid;
        this.payeeStoreUuid = payeeStoreUuid;
        this.payeeWorkerUuid = payeeWorkerUuid;
        this.shouldPay = shouldPay;
        this.actualPay = actualPay;
        this.pointsPay = pointsPay;
        this.points = points;
        this.status = status;
        this.paymentChannel = paymentChannel;
        this.channelOrderUuid = channelOrderUuid;
        this.signature = signature;
        this.created = created;
    }

    public static DiffCallback<Payment> DIFF_CALLBACK = new DiffCallback<Payment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Payment oldItem, @NonNull Payment newItem) {
            return oldItem.uuid == newItem.uuid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Payment oldItem, @NonNull Payment newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Payment myStore = (Payment) obj;

        return myStore.uuid == this.uuid;
    }

    public static Payment.Builder builder() {
        return new Payment.Builder();
    }

    public static final class Builder {
        private String uuid; //支付单编号
        private String payerUuid; //支付人
        private String payeeUuid; //收款人
        private String payeeStoreUuid;
        private String payeeWorkerUuid;
        private int shouldPay; //应付金额 = 实付金额 + 实付积分等价金额
        private int actualPay; //实付金额
        private int pointsPay; //实付积分代替金额
        private int points; //返还积分
        private String status;
        private String paymentChannel; //支付通道如支付宝、微信、云闪付
        private String channelOrderUuid; //预付单编号，可能由支付通道返回的或平台产生的
        private String signature;//微信或支付宝预定单签名，使用该签名可以直接调起支付.
        private long created;

        public Builder() {
        }

        public Payment build() {
            String missing = "";
            if (Strings.isNullOrEmpty(uuid)) {
                missing += " uuid ";
            }
            if (Strings.isNullOrEmpty(payerUuid)) {
                missing += " payerUuid";
            }
            if (Strings.isNullOrEmpty(payeeUuid)) {
                missing += " payeeUuid";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new Payment(uuid, payerUuid, payeeUuid, payeeStoreUuid, payeeWorkerUuid,
                    shouldPay, actualPay, pointsPay, points, status, paymentChannel,
                    channelOrderUuid, signature, created);
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setPayerUuid(String payerUuid) {
            this.payerUuid = payerUuid;
            return this;
        }

        public Builder setPayeeUuid(String payeeUuid) {
            this.payeeUuid = payeeUuid;
            return this;
        }

        public Builder setPayeeStoreUuid(String payeeStoreUuid) {
            this.payeeStoreUuid = payeeStoreUuid;
            return this;
        }

        public Builder setPayeeWorkerUuid(String payeeWorkerUuid) {
            this.payeeWorkerUuid = payeeWorkerUuid;
            return this;
        }

        public Builder setShouldPay(int shouldPay) {
            this.shouldPay = shouldPay;
            return this;
        }

        public Builder setActualPay(int actualPay) {
            this.actualPay = actualPay;
            return this;
        }

        public Builder setPointsPay(int pointsPay) {
            this.pointsPay = pointsPay;
            return this;
        }

        public Builder setPoints(int points) {
            this.points = points;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setPaymentChannel(String paymentChannel) {
            this.paymentChannel = paymentChannel;
            return this;
        }

        public Builder setChannelOrderUuid(String channelOrderUuid) {
            this.channelOrderUuid = channelOrderUuid;
            return this;
        }

        public Builder setSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public Builder setCreated(long created) {
            this.created = created;
            return this;
        }
    }


}

package com.github.conanchen.utils.vo;

/**
 * Created by conanchen on 2018/1/9.
 */

public class PaymentInfo {
    public VoAccessToken voAccessToken;
    public String payeeStoreUuid; //店员/收银员工作店铺的uuid
    public String payeeWorkerUuid; //店员/收银员uuid
    public String payeeCode; //店员、收银员的收款码
    public int shouldPay; //应付金额 = 实付金额 + 实付积分等价金额
    public int actualPay; //实付金额
    public int pointsPay; //实付积分代替金额
    public String payerIp; //付款人客户端的实际ip地址
    public String paymentChannelName;//支付类型
    public boolean isPointsPay;//是否使用积分抵扣金额

    private PaymentInfo(VoAccessToken voAccessToken, String payeeStoreUuid, String payeeWorkerUuid, String payeeCode,
                        int shouldPay, int actualPay, int pointsPay, String payerIp, String paymentChannelName, boolean isPointsPay) {
        this.voAccessToken = voAccessToken;
        this.payeeStoreUuid = payeeStoreUuid;
        this.payeeWorkerUuid = payeeWorkerUuid;
        this.payeeCode = payeeCode;
        this.shouldPay = shouldPay;
        this.actualPay = actualPay;
        this.pointsPay = pointsPay;
        this.payerIp = payerIp;
        this.paymentChannelName = paymentChannelName;
        this.isPointsPay = isPointsPay;
    }


    public static PaymentInfo.Builder builder() {
        return new PaymentInfo.Builder();
    }

    public static final class Builder {
        private VoAccessToken voAccessToken;
        private String payeeStoreUuid; //店员/收银员工作店铺的uuid
        private String payeeWorkerUuid; //店员/收银员uuid
        private String payeeCode; //店员、收银员的收款码
        private int shouldPay; //应付金额 = 实付金额 + 实付积分等价金额
        private int actualPay; //实付金额
        private int pointsPay; //实付积分代替金额
        private String payerIp; //付款人客户端的实际ip地址
        private String paymentChannelName;//支付类型
        private boolean isPointsPay;//是否使用积分抵扣金额

        Builder() {
        }

        public PaymentInfo build() {
            return new PaymentInfo(voAccessToken, payeeStoreUuid, payeeWorkerUuid, payeeCode,
                    shouldPay, actualPay, pointsPay, payerIp, paymentChannelName, isPointsPay);
        }

        public Builder setVoAccessToken(VoAccessToken voAccessToken) {
            this.voAccessToken = voAccessToken;
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

        public Builder setPayeeCode(String payeeCode) {
            this.payeeCode = payeeCode;
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

        public Builder setPayerIp(String payerIp) {
            this.payerIp = payerIp;
            return this;
        }

        public Builder setPaymentChannelName(String paymentChannelName) {
            this.paymentChannelName = paymentChannelName;
            return this;
        }

        public Builder setPointsPay(boolean pointsPay) {
            isPointsPay = pointsPay;
            return this;
        }
    }
}

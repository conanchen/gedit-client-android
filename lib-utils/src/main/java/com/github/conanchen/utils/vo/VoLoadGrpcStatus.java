package com.github.conanchen.utils.vo;

/**
 * Created by Administrator on 2018/1/29.
 */

public class VoLoadGrpcStatus {
    public String status;
    public String message;

    public VoLoadGrpcStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static VoLoadGrpcStatus.Builder builder() {
        return new VoLoadGrpcStatus.Builder();
    }

    public static final class Builder {
        private String status;
        private String message;

        Builder() {
        }

        public VoLoadGrpcStatus build() {
            return new VoLoadGrpcStatus(status, message);
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
    }
}

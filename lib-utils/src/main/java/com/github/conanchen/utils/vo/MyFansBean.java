package com.github.conanchen.utils.vo;

/**
 * Created by Administrator on 2018/1/30.
 */

public class MyFansBean {
    public VoAccessToken voAccessToken;
    public String fanUuid;

    public MyFansBean(VoAccessToken voAccessToken, String fanUuid) {
        this.voAccessToken = voAccessToken;
        this.fanUuid = fanUuid;
    }

    public static MyFansBean.Builder builder() {
        return new MyFansBean.Builder();
    }

    public static final class Builder {
        private VoAccessToken voAccessToken;
        private String fanUuid;

        Builder() {
        }

        public MyFansBean build() {
            return new MyFansBean(voAccessToken, fanUuid);
        }

        public Builder setVoAccessToken(VoAccessToken voAccessToken) {
            this.voAccessToken = voAccessToken;
            return this;
        }

        public Builder setFanUuid(String fanUuid) {
            this.fanUuid = fanUuid;
            return this;
        }
    }
}

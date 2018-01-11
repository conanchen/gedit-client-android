package com.github.conanchen.gedit.hello.grpc.auth;

/**
 * Created by Administrator on 2018/1/11.
 */

public class SigninInfo {
    public String mobile;
    public String password;

    public SigninInfo(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public static SigninInfo.Builder builder() {
        return new SigninInfo.Builder();
    }

    public static final class Builder {
        private String mobile;
        private String password;

        Builder() {
        }

        public SigninInfo build() {
            return new SigninInfo(mobile, password);
        }

        public Builder setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}

package com.github.conanchen.gedit.vo;

/**
 * Created by Administrator on 2018/1/11.
 */

public class SigninResponse {
    public String accessToken;
    public String expiresIn;

    public SigninResponse(String accessToken, String expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public static SigninResponse.Builder builder() {
        return new SigninResponse.Builder();
    }

    public static final class Builder {
        private String accessToken;
        private String expiresIn;

        public Builder() {
        }

        public SigninResponse build() {

            return new SigninResponse(accessToken, expiresIn);
        }

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setExpiresIn(String expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }
    }
}

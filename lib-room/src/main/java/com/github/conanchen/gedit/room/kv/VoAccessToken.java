package com.github.conanchen.gedit.room.kv;

public class VoAccessToken {
    public String accessToken;
    public String expiresIn;

    public VoAccessToken() {
    }


    private VoAccessToken(String accessToken, String expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String accessToken;
        private String expiresIn;

        Builder() {
        }

        public VoAccessToken build() {
            String missing = "";
            if (accessToken == null) {
                missing += " accessToken";
            }
            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new VoAccessToken(accessToken, expiresIn);
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

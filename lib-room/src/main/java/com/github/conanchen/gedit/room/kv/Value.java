package com.github.conanchen.gedit.room.kv;

import com.github.conanchen.utils.vo.VoAccessToken;

/**
 * Created by admin on 2017/7/20.
 */

public class Value {

    public VoAccessToken voAccessToken;

    public Value() {
    }

    private Value(
            VoAccessToken voAccessToken
    ) {
        this.voAccessToken = voAccessToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private VoAccessToken voAccessToken;

        Builder() {
        }

        public Value build() {
            String missing = "";

            if (voAccessToken == null) {
                missing += " voAccessToken|voWordSortType one must be set";
            }
            return new Value(voAccessToken);
        }

        public Builder setVoAccessToken(VoAccessToken voAccessToken) {
            this.voAccessToken = voAccessToken;
            return this;
        }
    }
}

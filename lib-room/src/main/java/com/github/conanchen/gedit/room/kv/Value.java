package com.github.conanchen.gedit.room.kv;

import com.github.conanchen.utils.vo.VoAccessToken;

/**
 * Created by admin on 2017/7/20.
 */

public class Value {

    public VoAccessToken voAccessToken;

    public String payeeStoreUuid;

    public Value() {
    }

    private Value(
            VoAccessToken voAccessToken,
            String payeeStoreUuid
    ) {
        this.voAccessToken = voAccessToken;
        this.payeeStoreUuid = payeeStoreUuid;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private VoAccessToken voAccessToken;
        private String payeeStoreUuid;

        Builder() {
        }

        public Value build() {
            String missing = "";

            if (voAccessToken == null) {
                missing += " voAccessToken|voWordSortType one must be set";
            }
            return new Value(voAccessToken, payeeStoreUuid);
        }

        public Builder setVoAccessToken(VoAccessToken voAccessToken) {
            this.voAccessToken = voAccessToken;
            return this;
        }

        public Builder setPayeeStoreUuid(String payeeStoreUuid) {
            this.payeeStoreUuid = payeeStoreUuid;
            return this;
        }
    }
}

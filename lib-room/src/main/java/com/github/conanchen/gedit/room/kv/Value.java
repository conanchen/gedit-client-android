package com.github.conanchen.gedit.room.kv;

import com.github.conanchen.utils.vo.VoAccessToken;
import com.github.conanchen.utils.vo.VoUserProfile;

/**
 * Created by admin on 2017/7/20.
 */

public class Value {

    public VoAccessToken voAccessToken;

    public String payeeStoreUuid;

    public VoUserProfile voUserProfile;

    public Value() {
    }

    private Value(VoAccessToken voAccessToken,
                  String payeeStoreUuid,
                  VoUserProfile voUserProfile) {
        this.voAccessToken = voAccessToken;
        this.payeeStoreUuid = payeeStoreUuid;
        this.voUserProfile = voUserProfile;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private VoAccessToken voAccessToken;
        private String payeeStoreUuid;
        private VoUserProfile voUserProfile;

        Builder() {
        }

        public Value build() {
            String missing = "";

            if (voAccessToken == null) {
                missing += " voAccessToken|voWordSortType one must be set";
            }
            return new Value(voAccessToken, payeeStoreUuid,voUserProfile);
        }

        public Builder setVoAccessToken(VoAccessToken voAccessToken) {
            this.voAccessToken = voAccessToken;
            return this;
        }

        public Builder setPayeeStoreUuid(String payeeStoreUuid) {
            this.payeeStoreUuid = payeeStoreUuid;
            return this;
        }

        public Builder setVoUserProfile(VoUserProfile voUserProfile) {
            this.voUserProfile = voUserProfile;
            return this;
        }
    }
}

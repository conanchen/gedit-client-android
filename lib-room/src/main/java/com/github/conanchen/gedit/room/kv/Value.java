package com.github.conanchen.gedit.room.kv;

import com.github.conanchen.utils.vo.VoAccessToken;
import com.github.conanchen.utils.vo.VoLoadGrpcStatus;
import com.github.conanchen.utils.vo.VoUserProfile;
import com.github.conanchen.utils.vo.VoWorkingStore;

/**
 * Created by admin on 2017/7/20.
 */

public class Value {

    public VoAccessToken voAccessToken;

    public VoWorkingStore voWorkingStore;

    public VoUserProfile voUserProfile;

    public VoLoadGrpcStatus voLoadGrpcStatus;

    public Value() {
    }

    private Value(VoAccessToken voAccessToken,
                  VoWorkingStore voWorkingStore,
                  VoUserProfile voUserProfile,
                  VoLoadGrpcStatus voLoadGrpcStatus) {
        this.voAccessToken = voAccessToken;
        this.voWorkingStore = voWorkingStore;
        this.voUserProfile = voUserProfile;
        this.voLoadGrpcStatus = voLoadGrpcStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private VoAccessToken voAccessToken;
        private VoWorkingStore voWorkingStore;
        private VoUserProfile voUserProfile;
        private VoLoadGrpcStatus voLoadGrpcStatus;

        Builder() {
        }

        public Value build() {
            String missing = "";

            if (voAccessToken == null) {
                missing += " voAccessToken|voWordSortType one must be set";
            }
            return new Value(voAccessToken, voWorkingStore, voUserProfile,voLoadGrpcStatus);
        }

        public Builder setVoAccessToken(VoAccessToken voAccessToken) {
            this.voAccessToken = voAccessToken;
            return this;
        }

        public Builder setVoWorkingStore(VoWorkingStore voWorkingStore) {
            this.voWorkingStore = voWorkingStore;
            return this;
        }

        public Builder setVoUserProfile(VoUserProfile voUserProfile) {
            this.voUserProfile = voUserProfile;
            return this;
        }

        public Builder setVoLoadGrpcStatus(VoLoadGrpcStatus voLoadGrpcStatus) {
            this.voLoadGrpcStatus = voLoadGrpcStatus;
            return this;
        }
    }
}

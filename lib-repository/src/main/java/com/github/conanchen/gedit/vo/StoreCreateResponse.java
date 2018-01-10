package com.github.conanchen.gedit.vo;

import com.google.common.base.Strings;

/**
 * Created by conanchen on 2018/1/10.
 */

public class StoreCreateResponse {
    public String stausCode;
    public String statusDetail;
    public String storeUuid;

    private StoreCreateResponse(String stausCode, String statusDetail, String storeUuid) {
        this.stausCode = stausCode;
        this.statusDetail = statusDetail;
        this.storeUuid = storeUuid;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String stausCode;
        private String statusDetail;
        private String storeUuid;

        Builder() {
        }

        public StoreCreateResponse build() {
            String missing = "";
            if (Strings.isNullOrEmpty(stausCode)) {
                missing += " stausCode";
            }


            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new StoreCreateResponse(stausCode,   statusDetail,   storeUuid);
        }

        public Builder setStausCode(String stausCode) {
            this.stausCode = stausCode;
            return this;
        }

        public Builder setStatusDetail(String statusDetail) {
            this.statusDetail = statusDetail;
            return this;
        }

        public Builder setStoreUuid(String storeUuid) {
            this.storeUuid = storeUuid;
            return this;
        }
    }


}

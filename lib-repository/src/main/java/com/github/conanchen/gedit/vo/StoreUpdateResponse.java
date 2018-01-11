package com.github.conanchen.gedit.vo;

import com.google.common.base.Strings;

/**
 * Created by Administrator on 2018/1/10.
 */

public class StoreUpdateResponse {

    public String stausCode;
    public String statusDetail;
    public String storeUuid;

    private StoreUpdateResponse(String stausCode, String statusDetail, String storeUuid) {
        this.stausCode = stausCode;
        this.statusDetail = statusDetail;
        this.storeUuid = storeUuid;
    }

    public static StoreUpdateResponse.Builder builder() {
        return new StoreUpdateResponse.Builder();
    }

    public static final class Builder {
        private String stausCode;
        private String statusDetail;
        private String storeUuid;

        Builder() {
        }

        public StoreUpdateResponse build() {
            String missing = "";
            if (Strings.isNullOrEmpty(stausCode)) {
                missing += " stausCode";
            }


            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new StoreUpdateResponse(stausCode, statusDetail, storeUuid);
        }

        public StoreUpdateResponse.Builder setStausCode(String stausCode) {
            this.stausCode = stausCode;
            return this;
        }

        public StoreUpdateResponse.Builder setStatusDetail(String statusDetail) {
            this.statusDetail = statusDetail;
            return this;
        }

        public StoreUpdateResponse.Builder setStoreUuid(String storeUuid) {
            this.storeUuid = storeUuid;
            return this;
        }
    }
}
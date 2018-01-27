package com.github.conanchen.utils.vo;

/**
 * Created by Administrator on 2018/1/27.
 */

public class VoWorkingStore {
    public String uuid;//相当于主键
    public String userUuid;
    public String storeUuid;
    public String userName;
    public String userLogo;
    public String storeName;
    public String storeLogo;
    public double lat;
    public double lon;

    VoWorkingStore() {

    }

    public VoWorkingStore(String uuid, String userUuid, String storeUuid, String userName, String userLogo, String storeName,
                          String storeLogo, double lat, double lon) {
        this.uuid = uuid;
        this.userUuid = userUuid;
        this.storeUuid = storeUuid;
        this.userName = userName;
        this.userLogo = userLogo;
        this.storeName = storeName;
        this.storeLogo = storeLogo;
        this.lat = lat;
        this.lon = lon;
    }

    public static VoWorkingStore.Builder builder() {
        return new VoWorkingStore.Builder();
    }

    public static final class Builder {
        private String uuid;//相当于主键
        private String userUuid;
        private String storeUuid;
        private String userName;
        private String userLogo;
        private String storeName;
        private String storeLogo;
        private double lat;
        private double lon;

        Builder() {
        }

        public VoWorkingStore build() {
            return new VoWorkingStore(uuid, userUuid, storeUuid, userName, userLogo, storeName, storeLogo, lat, lon);
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setUserUuid(String userUuid) {
            this.userUuid = userUuid;
            return this;
        }

        public Builder setStoreUuid(String storeUuid) {
            this.storeUuid = storeUuid;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setUserLogo(String userLogo) {
            this.userLogo = userLogo;
            return this;
        }

        public Builder setStoreName(String storeName) {
            this.storeName = storeName;
            return this;
        }

        public Builder setStoreLogo(String storeLogo) {
            this.storeLogo = storeLogo;
            return this;
        }

        public Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLon(double lon) {
            this.lon = lon;
            return this;
        }
    }
}

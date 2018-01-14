package com.github.conanchen.gedit.hello.grpc.store;

import com.github.conanchen.gedit.room.kv.VoAccessToken;
import com.google.common.base.Strings;

/**
 * Created by conanchen on 2018/1/9.
 */

public class StoreCreateInfo {
    public VoAccessToken voAccessToken;
    public String name;
    public double lat;
    public double lon;
    public String districtUuid;
    public String address;
    public String introducerPhone;

    public StoreCreateInfo(VoAccessToken voAccessToken, String name, double lat, double lon, String districtUuid, String address, String introducerPhone) {
        this.voAccessToken = voAccessToken;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.districtUuid = districtUuid;
        this.address = address;
        this.introducerPhone = introducerPhone;
    }

    public static StoreCreateInfo.Builder builder() {
        return new StoreCreateInfo.Builder();
    }

    public static final class Builder {
        private VoAccessToken voAccessToken;
        private String name;
        private double lat;
        private double lon;
        private String districtUuid;
        private String address;
        private String introducerPhone;

        Builder() {
        }

        public StoreCreateInfo build() {
            String missing = "";
            if (voAccessToken ==null) {
                missing += " voAccessToken ";
            }
            if (Strings.isNullOrEmpty(name)) {
                missing += " name ";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new StoreCreateInfo(voAccessToken, name, lat, lon, districtUuid, address, introducerPhone);
        }

        public Builder setVoAccessToken(VoAccessToken voAccessToken) {
            this.voAccessToken = voAccessToken;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
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

        public Builder setDistrictUuid(String districtUuid) {
            this.districtUuid = districtUuid;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setIntroducerPhone(String introducerPhone) {
            this.introducerPhone = introducerPhone;
            return this;
        }
    }

}

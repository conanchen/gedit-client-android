package com.github.conanchen.gedit.hello.grpc;

import com.google.common.base.Strings;

/**
 * Created by conanchen on 2018/1/9.
 */

public class StoreCreateInfo {
    public String name;
    public double lat;
    public double lon;
    public String districtUuid;
    public String address;

    private StoreCreateInfo(String name, double lat, double lon, String districtUuid, String address) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.districtUuid = districtUuid;
        this.address = address;
    }

    public static StoreCreateInfo.Builder builder() {
        return new StoreCreateInfo.Builder();
    }

    public static final class Builder {
        private String name;
        private double lat;
        private double lon;
        private String districtUuid;
        private String address;

        Builder() {
        }

        public StoreCreateInfo build() {
            String missing = "";
            if (Strings.isNullOrEmpty(name) ) {
                missing += " name ";
            }


            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new StoreCreateInfo(  name,   lat,   lon,   districtUuid,   address);
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
    }

}

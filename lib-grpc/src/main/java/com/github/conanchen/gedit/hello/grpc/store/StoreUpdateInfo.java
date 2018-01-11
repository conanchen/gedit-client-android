package com.github.conanchen.gedit.hello.grpc.store;

import android.location.Location;

import com.google.common.base.Strings;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */

public class StoreUpdateInfo {
    public String name;
    public String logo;
    public String type;
    public String desc;
    public String districtUuid;
    public String detailAddress;
    public String uuid;
    public Location location;
    public int pointsRate;
    public List<String> images;

    public StoreUpdateInfo(String name, String logo, String type, String desc, String districtUuid, String detailAddress, String uuid, Location location, int pointsRate, List<String> images) {
        this.name = name;
        this.logo = logo;
        this.type = type;
        this.desc = desc;
        this.districtUuid = districtUuid;
        this.detailAddress = detailAddress;
        this.uuid = uuid;
        this.location = location;
        this.pointsRate = pointsRate;
        this.images = images;
    }

    public static StoreUpdateInfo.Builder builder() {
        return new StoreUpdateInfo.Builder();
    }


    public static final class Builder {
        private String name;
        private String logo;
        private String type;
        private String desc;
        private String districtUuid;
        private String detailAddress;
        private String uuid;
        private Location location;
        private int pointsRate;
        private List<String> images;

        Builder() {
        }

        public StoreUpdateInfo build() {
            String missing = "";
            if (Strings.isNullOrEmpty(name)) {
                missing += " name ";
            }


            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new StoreUpdateInfo(name, logo, type, desc, districtUuid, detailAddress, uuid, location, pointsRate, images);
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLogo(String logo) {
            this.logo = logo;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setDesc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder setDistrictUuid(String districtUuid) {
            this.districtUuid = districtUuid;
            return this;
        }

        public Builder setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
            return this;
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder setPointsRate(int pointsRate) {
            this.pointsRate = pointsRate;
            return this;
        }

        public Builder setImages(List<String> images) {
            this.images = images;
            return this;
        }

    }
}

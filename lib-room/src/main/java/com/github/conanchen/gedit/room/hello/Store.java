package com.github.conanchen.gedit.room.hello;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.common.base.Strings;

/**
 * Created by Conan Chen on 2018/1/8.
 */
@Entity(indices = {
        @Index(value = {"uuid", "address"}),
        @Index(value = {"address"}),
        @Index(value = {"uuid"})
})
public class Store {
    @PrimaryKey
    @NonNull
    public String uuid;
    public String name;
    public Double lat;
    public Double lon;
    public String districtUuid;
    public String address;
    public long created;
    public long lastUpdated;

    public Store() {
    }

    private Store(@NonNull String uuid, String name, Double lat, Double lon, String districtUuid, String address, long created, long lastUpdated) {
        this.uuid = uuid;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.districtUuid = districtUuid;
        this.address = address;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public static Store.Builder builder() {
        return new Store.Builder();
    }

    public static final class Builder {
        private String uuid;
        private String name;
        public Double lat;
        public Double lon;
        private String districtUuid;
        private String address;
        private long created;
        private long lastUpdated;

        Builder() {
        }

        public Store build() {
            String missing = "";
            if (Strings.isNullOrEmpty(uuid)) {
                missing += " uuid ";
            }
            if (Strings.isNullOrEmpty(name)) {
                missing += " name";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new Store(uuid, name, lat,lon,districtUuid,address, created, lastUpdated);
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLat(Double lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLon(Double lon) {
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

        public Builder setCreated(long created) {
            this.created = created;
            return this;
        }

        public Builder setLastUpdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }
    }
}

package com.github.conanchen.gedit.room.my.fan;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import com.google.common.base.Strings;

/**
 * Created by hutao on 2018/1/17.
 */
@Entity(indices = {
        @Index(value = {"fanUuid"}),
        @Index(value = {"lastUpdated"})
})
public class Fanship {
    @PrimaryKey
    @NonNull
    public String fanUuid;
    public String fanName;
    public String fanLogo;
    public Double lat;
    public Double lon;
    public long created;
    public long lastUpdated;

    public Fanship() {
    }

    private Fanship(@NonNull String fanUuid, String fanName, String fanLogo, Double lat, Double lon, long created, long lastUpdated) {
        this.fanUuid = fanUuid;
        this.fanName = fanName;
        this.fanLogo = fanLogo;
        this.lat = lat;
        this.lon = lon;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public static DiffCallback<Fanship> DIFF_CALLBACK = new DiffCallback<Fanship>() {
        @Override
        public boolean areItemsTheSame(@NonNull Fanship oldItem, @NonNull Fanship newItem) {
            return oldItem.fanUuid == newItem.fanUuid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Fanship oldItem, @NonNull Fanship newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Fanship myStore = (Fanship) obj;

        return myStore.fanUuid == this.fanUuid;
    }

    public static Fanship.Builder builder() {
        return new Fanship.Builder();
    }

    public static final class Builder {
        private String fanUuid;
        private String fanName;
        private String fanLogo;
        private long created;
        private long lastUpdated;
        private Double lat;
        private Double lon;

        public Builder() {
        }

        public Fanship build() {
            String missing = "";
            if (Strings.isNullOrEmpty(fanUuid)) {
                missing += " fanUuid ";
            }
            if (Strings.isNullOrEmpty(fanName)) {
                missing += " fanName";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new Fanship(fanUuid, fanName, fanLogo, lat, lon, created, lastUpdated);
        }

        public Builder setFanUuid(String fanUuid) {
            this.fanUuid = fanUuid;
            return this;
        }

        public Builder setFanName(String fanName) {
            this.fanName = fanName;
            return this;
        }

        public Builder setFanLogo(String fanLogo) {
            this.fanLogo = fanLogo;
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

        public Builder setLat(Double lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLon(Double lon) {
            this.lon = lon;
            return this;
        }
    }


}

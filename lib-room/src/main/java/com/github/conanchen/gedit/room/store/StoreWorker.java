package com.github.conanchen.gedit.room.store;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

/**
 * Created by Conan Chen on 2018/1/8.
 */
@Entity(indices = {
        @Index(value = {"lastUpdated"})
})
public class StoreWorker {
    @PrimaryKey
    @NonNull
    public String uuid;
    public String userUuid;
    public String storeUuid;
    public String userName;
    public String userLogo;
    public String storeName;
    public String storeLogo;
    public Double lat;
    public Double lon;
    public long created;
    public long lastUpdated;

    public StoreWorker() {
    }

    private StoreWorker(@NonNull String uuid, String userUuid, String storeUuid, String userName, String userLogo, String storeName,
                        String storeLogo, Double lat,Double lon, long created, long lastUpdated) {
        this.uuid = uuid;
        this.userUuid = userUuid;
        this.storeUuid = storeUuid;
        this.userName = userName;
        this.userLogo = userLogo;
        this.storeName = storeName;
        this.storeLogo = storeLogo;
        this.created = created;
        this.lastUpdated = lastUpdated;
        this.lat = lat;
        this.lon = lon;
    }

    public static DiffCallback<StoreWorker> DIFF_CALLBACK = new DiffCallback<StoreWorker>() {
        @Override
        public boolean areItemsTheSame(@NonNull StoreWorker oldItem, @NonNull StoreWorker newItem) {
            return oldItem.uuid == newItem.uuid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull StoreWorker oldItem, @NonNull StoreWorker newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        StoreWorker user = (StoreWorker) obj;

        return user.uuid == this.uuid;
    }


    public static StoreWorker.Builder builder() {
        return new StoreWorker.Builder();
    }

    public static final class Builder {

        private String uuid;
        private String userUuid;
        private String storeUuid;
        private String userName;
        private String userLogo;
        private String storeName;
        private String storeLogo;
        private long created;
        private long lastUpdated;
        private Double lat;
        private Double lon;

        Builder() {
        }

        public StoreWorker build() {

            return new StoreWorker(uuid, userUuid, storeUuid, userName, userLogo, storeName, storeLogo,lat,lon,
                    created, lastUpdated);
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

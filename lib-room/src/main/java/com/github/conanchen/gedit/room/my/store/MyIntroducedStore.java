package com.github.conanchen.gedit.room.my.store;

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
        @Index(value = {"storeUuid"}),
        @Index(value = {"lastUpdated"})
})
public class MyIntroducedStore {
    @PrimaryKey
    @NonNull
    public String storeUuid;
    public String storeName;
    public String storeLogo;
    public Double lat;
    public Double lon;
    public long created;
    public long lastUpdated;


    public MyIntroducedStore() {
    }

    private MyIntroducedStore(@NonNull String storeUuid, String storeName, String storeLogo, Double lat, Double lon, long created, long lastUpdated) {
        this.storeUuid = storeUuid;
        this.storeName = storeName;
        this.storeLogo = storeLogo;
        this.lat = lat;
        this.lon = lon;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public static DiffCallback<MyIntroducedStore> DIFF_CALLBACK = new DiffCallback<MyIntroducedStore>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyIntroducedStore oldItem, @NonNull MyIntroducedStore newItem) {
            return oldItem.storeUuid == newItem.storeUuid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyIntroducedStore oldItem, @NonNull MyIntroducedStore newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        MyIntroducedStore myStore = (MyIntroducedStore) obj;

        return myStore.storeUuid == this.storeUuid;
    }

    public static MyIntroducedStore.Builder builder() {
        return new MyIntroducedStore.Builder();
    }

    public static final class Builder {
        private String storeUuid;
        private String storeName;
        private String storeLogo;
        private long created;
        private long lastUpdated;
        private Double lat;
        private Double lon;

        public Builder() {
        }

        public MyIntroducedStore build() {
            String missing = "";
            if (Strings.isNullOrEmpty(storeUuid)) {
                missing += " storeUuid ";
            }
            if (Strings.isNullOrEmpty(storeName)) {
                missing += " storeName";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new MyIntroducedStore(storeUuid, storeName, storeLogo, lat, lon, created, lastUpdated);
        }

        public Builder setStoreUuid(String storeUuid) {
            this.storeUuid = storeUuid;
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

package com.github.conanchen.gedit.room.hello;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import com.google.common.base.Strings;

/**
 * Created by conanchen on 2017/12/20.
 */

@Entity(indices = {
        @Index(value = {"created"}),
        @Index(value = {"uuid", "created"})
})
public class Hello {
    @PrimaryKey
    @NonNull
    public String uuid;
    public String message;
    public long created;
    public long lastUpdated;

    public Hello() {
    }

    private Hello(@NonNull String uuid, String message, long created, long lastUpdated) {
        this.uuid = uuid;
        this.message = message;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public static DiffCallback<Hello> DIFF_CALLBACK = new DiffCallback<Hello>() {
        @Override
        public boolean areItemsTheSame(@NonNull Hello oldItem, @NonNull Hello newItem) {
            return oldItem.uuid == newItem.uuid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Hello oldItem, @NonNull Hello newItem) {
            return oldItem.equals(newItem);
        }
    };


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Hello user = (Hello) obj;

        return user.uuid == this.uuid;
    }
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String uuid;
        private String message;
        private long created;
        private long lastUpdated;

        Builder() {
        }

        public Hello build() {
            String missing = "";
            if (Strings.isNullOrEmpty(uuid)) {
                missing += " uuid ";
            }
            if (Strings.isNullOrEmpty(message)) {
                missing += " message";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new Hello(uuid, message, created, lastUpdated);
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
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
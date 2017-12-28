package com.github.conanchen.gedit.room.hello;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.common.base.Strings;

/**
 * Created by conanchen on 2017/12/20.
 */

@Entity(indices = {
        @Index(value = {"id", "created"})
})
public class Hello {
    @PrimaryKey
    @NonNull
    public long id;
    public String message;
    public long created;
    public long lastUpdated;

    public Hello() {
    }

    private Hello(@NonNull long id, String message, long created, long lastUpdated) {
        this.id = id;
        this.message = message;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private long id;
        private String message;
        private long created;
        private long lastUpdated;

        Builder() {
        }

        public Hello build() {
            String missing = "";
            if (id <= 0) {
                missing += " id must be > 0 ";
            }
            if (Strings.isNullOrEmpty(message)) {
                missing += " message";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new Hello(id, message, created, lastUpdated);
        }

        public Builder setId(long id) {
            this.id = id;
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
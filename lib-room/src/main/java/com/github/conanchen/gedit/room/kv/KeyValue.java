package com.github.conanchen.gedit.room.kv;

/*
 * Copyright (c) 2016 Razeware LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.common.base.Strings;

@Entity
public class KeyValue {
    public interface KEY {
        String USER_CURRENT_ACCESSTOKEN = "USER.CURRENT.LOGIN";
        String USER_CURRENT_WORKING_STORE = "USER.CURRENT.WORKING.STORE";
        String USER_CURRENT_USER_PROFILE = "USER.CURRENT.USER.PROFILE";
        String LOAD_GRPC_API_STATUS = "LOAD.GRPC.API.STATUS";
        String USER_SETTING_WORDSORTTYPE = "USER.SETTING.WORDSORTTYPE";
        String USER_STATS_WORD_LEVEL1 = "USER.STATS.WORD.LEVEL1";
        String USER_STATS_WORD_LEVEL2 = "USER.STATS.WORD.LEVEL2";
        String USER_STATS_WORD_LEVEL3 = "USER.STATS.WORD.LEVEL3";
    }

    @PrimaryKey
    @NonNull
    public String key;
    public Value value;
    public long created;
    public long lastUpdated;

    public KeyValue() {
    }

    private KeyValue(String key, Value value, long created, long lastUpdated) {
        this.key = key;
        this.value = value;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String key;
        private Value value;
        private long created;
        private long lastUpdated;

        Builder() {
        }

        public KeyValue build() {
            String missing = "";

            if (Strings.isNullOrEmpty(key)) {
                missing += " key";
            }

            if (value == null) {
                missing += " value";
            }


            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }

            return new KeyValue(key, value, created, lastUpdated);
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setValue(Value value) {
            this.value = value;
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

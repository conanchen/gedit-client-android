package com.github.conanchen.gedit.vo;

import com.google.common.base.Strings;

/**
 * Created by conanchen on 2018/1/9.
 */

public class StoreCreateInfo {
    public String name;

    private StoreCreateInfo(String name) {
        this.name = name;
    }



    public static StoreCreateInfo.Builder builder() {
        return new StoreCreateInfo.Builder();
    }

    public static final class Builder {
        private String name;

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
            return new StoreCreateInfo(name);
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }
    }

}

package com.github.conanchen.utils.vo;

import com.google.common.base.Strings;

/**
 * Created by Conan Chen on 2018/1/10.
 */

public class StoreUpdateInfo {
    public VoAccessToken voAccessToken;
    public String uuid;
    //单项修改
    public Field name;
    public Object value;

    public enum Field {
        NAME, LOGO, TYPE, DESC, DISTRICT_UUID, DETAIL_ADDRESS, LOCATION, POINTS_RATE, IMAGES
    }
//
//    public String name;
//    public String logo;
//    public String type;
//    public String desc;
//    public String districtUuid;
//    public String detailAddress;
//    public Location location;
//    public int pointsRate;
//    public List<String> images;


    private StoreUpdateInfo(VoAccessToken voAccessToken, String uuid, Field name, Object value) {
        this.voAccessToken = voAccessToken;
        this.uuid = uuid;
        this.name = name;
        this.value = value;
    }

    public static StoreUpdateInfo.Builder builder() {
        return new StoreUpdateInfo.Builder();
    }


    public static final class Builder {
        private VoAccessToken voAccessToken;
        private String uuid;
        //单项修改
        private Field name;
        private Object value;

        Builder() {
        }

        public StoreUpdateInfo build() {
            String missing = "";
            if (voAccessToken ==null) {
                missing += " voAccessToken ";
            }
            if (Strings.isNullOrEmpty(uuid)) {
                missing += " uuid ";
            }
            if (name == null) {
                missing += " name ";
            }
            if (value == null) {
                missing += " value ";
            }


            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new StoreUpdateInfo(voAccessToken,uuid, name, value);
        }

        public Builder setVoAccessToken(VoAccessToken voAccessToken) {
            this.voAccessToken = voAccessToken;
            return this;
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setName(Field name) {
            this.name = name;
            return this;
        }

        public Builder setValue(Object value) {
            this.value = value;
            return this;
        }
    }
}

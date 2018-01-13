package com.github.conanchen.gedit.hello.grpc.store;

import android.location.Location;

import com.google.common.base.Strings;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */

public class StoreUpdateInfo {
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


    private StoreUpdateInfo(String uuid, Field name, Object value) {
        this.uuid = uuid;
        this.name = name;
        this.value = value;
    }

    public static StoreUpdateInfo.Builder builder() {
        return new StoreUpdateInfo.Builder();
    }


    public static final class Builder {
        private String uuid;
        //单项修改
        private Field name;
        private Object value;

        Builder() {
        }

        public StoreUpdateInfo build() {
            String missing = "";
            if (Strings.isNullOrEmpty(uuid)) {
                missing += " uuid ";
            }
            if (name == null) {
                missing += " uuid ";
            }
            if (value == null) {
                missing += " value ";
            }


            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new StoreUpdateInfo(uuid, name, value);
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

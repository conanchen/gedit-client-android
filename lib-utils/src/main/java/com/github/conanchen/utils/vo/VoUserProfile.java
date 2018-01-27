package com.github.conanchen.utils.vo;

/**
 * Created by Administrator on 2018/1/26.
 */

public class VoUserProfile {
    public String uuid;//用户id
    public String mobile;//电话
    public String name;//名字
    public String desc;//描述
    public String logo;//头像
    public String districtId;//地区id

    public VoUserProfile(String uuid, String mobile, String name, String desc, String logo, String districtId) {
        this.uuid = uuid;
        this.mobile = mobile;
        this.name = name;
        this.desc = desc;
        this.logo = logo;
        this.districtId = districtId;
    }

    public static VoUserProfile.Builder builder() {
        return new VoUserProfile.Builder();
    }

    public static final class Builder {
        private String uuid;//用户id
        private String mobile;//电话
        private String name;//名字
        private String desc;//描述
        private String logo;//头像
        private String districtId;//地区id

        Builder() {
        }

        public VoUserProfile build() {

            return new VoUserProfile(uuid, mobile, name, desc, logo, districtId);
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDesc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder setLogo(String logo) {
            this.logo = logo;
            return this;
        }

        public Builder setDistrictId(String districtId) {
            this.districtId = districtId;
            return this;
        }
    }
}

package com.github.conanchen.gedit.vo;

/**
 * Created by conanchen on 2018/1/9.
 */

public class Location {
    public double lat;
    public double lon;

    private Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private double lat;
        private double lon;

        Builder() {
        }

        public Location build() {
            String missing = "";
            if (lat <= 0) {
                missing += " lat must be > 0 ";
            }
            if (lon <= 0) {
                missing += " lon must be > 0 ";
            }


            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new Location(lat,lon);
        }

        public Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLon(double lon) {
            this.lon = lon;
            return this;
        }
    }

}

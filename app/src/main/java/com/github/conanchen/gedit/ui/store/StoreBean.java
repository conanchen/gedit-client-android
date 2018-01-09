package com.github.conanchen.gedit.ui.store;

/**
 * Created by Administrator on 2018/1/9.
 */

public class StoreBean {
    private String uuid;
    private String storeName;
    private String address;

    public String getUuid() {
        return uuid;
    }

    public StoreBean setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getStoreName() {
        return storeName;
    }

    public StoreBean setStoreName(String storeName) {
        this.storeName = storeName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public StoreBean setAddress(String address) {
        this.address = address;
        return this;
    }
}

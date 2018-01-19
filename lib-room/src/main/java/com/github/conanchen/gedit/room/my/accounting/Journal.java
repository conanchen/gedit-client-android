package com.github.conanchen.gedit.room.my.accounting;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import com.github.conanchen.gedit.room.kv.Value;
import com.google.common.base.Strings;

/**
 * Created by Conan Chen on 2018/1/17.
 */
@Entity(indices = {
        @Index(value = {"uuid"}),
        @Index(value = {"created"})
})
public class Journal {
//### 日志Journal
//`系统记账需要的事实日志如：支付发生、注册用户、创建店铺`
//
//            | 日志UUID          | 日志事件类型       | 日志事件内容                  |创建日期  |流水创建标记  |流水创建日期 |
//            | :--------------- |:------------------| :---------------------------|--------:|:----------:|----------:|
//            | journal-uuid-001 | PAYMENT_CREATED   | {payment created event json}|20180101 |    Y       |20180101   |
//            | journal-uuid-002 | USER_VERIFIED     | {user register event json}  |20180101 |    N       |19000101   |
//            | journal-uuid-003 | STORE_VERIFIED    | {store created event json}  |20180102 |    N       |19000101   |
    @PrimaryKey
    @NonNull
    public String uuid; //journal uuid
    public String accountUuid;
    public String type; //journal type
    public Value value;
    public long created;


    public Journal(@NonNull String uuid, String accountUuid, String type, Value value, long created) {
        this.uuid = uuid;
        this.accountUuid = accountUuid;
        this.type = type;
        this.value = value;
        this.created = created;
    }

    public static DiffCallback<Journal> DIFF_CALLBACK = new DiffCallback<Journal>() {
        @Override
        public boolean areItemsTheSame(@NonNull Journal oldItem, @NonNull Journal newItem) {
            return oldItem.uuid == newItem.uuid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Journal oldItem, @NonNull Journal newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Journal myStore = (Journal) obj;

        return myStore.accountUuid == this.accountUuid;
    }

    public static Journal.Builder builder() {
        return new Journal.Builder();
    }

    public static final class Builder {
        private String uuid; //journal uuid
        private String accountUuid;
        private String type; //journal type
        private Value value;
        private long created;

        public Builder() {
        }

        public Journal build() {
            String missing = "";
            if (Strings.isNullOrEmpty(uuid)) {
                missing += " uuid ";
            }
            if (Strings.isNullOrEmpty(accountUuid)) {
                missing += " accountUuid";
            }

            if (Strings.isNullOrEmpty(type)) {
                missing += " type";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new Journal(uuid,   accountUuid,   type,   value,   created);
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setAccountUuid(String accountUuid) {
            this.accountUuid = accountUuid;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
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
    }


}

package com.github.conanchen.gedit.room.my.accounting;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import com.google.common.base.Strings;

/**
 * Created by Conan Chen on 2018/1/17.
 */
@Entity(indices = {
        @Index(value = {"uuid"}),
        @Index(value = {"created"})
})
public class Account {
//### 账户Account
//`每个用户拥有如下账户：CASH现金、POINTS积分、REDPACK红包、COUPON优惠券`
//
//            | 用户编号UUID   | 账户类型  | 账户编号UUID        | 创建日期 |
//            | :------------ |:---------| :------------------|--------:|
//            | user-uuid-001 | CASH     | account-uuid-101   |20180101 |
//            | user-uuid-001 | POINTS   | account-uuid-102   |20180101 |
//            | user-uuid-002 | CASH     | account-uuid-103   |20180102 |
//            | user-uuid-002 | POINTS   | account-uuid-104   |20180102 |

    @PrimaryKey
    @NonNull
    public String uuid;
    public String type;
    public int previousBalance;
    public long previousDate;
    public int currentChanges;
    public long currentDate;
    public int currentBalance;

    public long created;
    public long lastUpdated;

    public Account() {
    }

    private Account(@NonNull String uuid, String type, int previousBalance, long previousDate, int currentChanges, long currentDate, int currentBalance, long created, long lastUpdated) {
        this.uuid = uuid;
        this.type = type;
        this.previousBalance = previousBalance;
        this.previousDate = previousDate;
        this.currentChanges = currentChanges;
        this.currentDate = currentDate;
        this.currentBalance = currentBalance;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public static DiffCallback<Account> DIFF_CALLBACK = new DiffCallback<Account>() {
        @Override
        public boolean areItemsTheSame(@NonNull Account oldItem, @NonNull Account newItem) {
            return oldItem.uuid == newItem.uuid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Account oldItem, @NonNull Account newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Account myStore = (Account) obj;

        return myStore.uuid == this.uuid;
    }

    public static Account.Builder builder() {
        return new Account.Builder();
    }

    public static final class Builder {

        private String uuid;
        private String type;
        private int previousBalance;
        private long previousDate;
        private int currentChanges;
        private long currentDate;
        private int currentBalance;

        private long created;
        private long lastUpdated;

        public Builder() {
        }

        public Account build() {
            String missing = "";
            if (Strings.isNullOrEmpty(uuid)) {
                missing += " uuid ";
            }
            if (Strings.isNullOrEmpty(type)) {
                missing += " type";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new Account(  uuid,   type,   previousBalance,   previousDate,   currentChanges,   currentDate,   currentBalance,   created,   lastUpdated);
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setPreviousBalance(int previousBalance) {
            this.previousBalance = previousBalance;
            return this;
        }

        public Builder setPreviousDate(long previousDate) {
            this.previousDate = previousDate;
            return this;
        }

        public Builder setCurrentChanges(int currentChanges) {
            this.currentChanges = currentChanges;
            return this;
        }

        public Builder setCurrentDate(long currentDate) {
            this.currentDate = currentDate;
            return this;
        }

        public Builder setCurrentBalance(int currentBalance) {
            this.currentBalance = currentBalance;
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

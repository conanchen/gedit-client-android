package com.github.conanchen.gedit.room.my.accounting;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import com.github.conanchen.gedit.room.my.store.MyStore;
import com.google.common.base.Strings;

/**
 * Created by Conan Chen on 2018/1/17.
 */
@Entity(indices = {
        @Index(value = {"accountUuid"}),
        @Index(value = {"lastUpdated"})
})
public class Balance {
    //| 账户编号UUID      | 前期余额  | 前期日期  | 当期变化 | 当期日期  | 当期余额 |  CRC    |
    @PrimaryKey
    @NonNull
    public String accountUuid;
    public String accountType;
    public int previousBalance;
    public long previousDate;
    public int currentChanges;
    public long currentDate;
    public int currentBalance;
    public long lastUpdated;

    private Balance(@NonNull String accountUuid, String accountType, int previousBalance,
                    long previousDate, int currentChanges, long currentDate,
                    int currentBalance, long lastUpdated) {
        this.accountUuid = accountUuid;
        this.accountType = accountType;
        this.previousBalance = previousBalance;
        this.previousDate = previousDate;
        this.currentChanges = currentChanges;
        this.currentDate = currentDate;
        this.currentBalance = currentBalance;
        this.lastUpdated = lastUpdated;
    }

    public static DiffCallback<MyStore> DIFF_CALLBACK = new DiffCallback<MyStore>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyStore oldItem, @NonNull MyStore newItem) {
            return oldItem.storeUuid == newItem.storeUuid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyStore oldItem, @NonNull MyStore newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Balance myStore = (Balance) obj;

        return myStore.accountUuid == this.accountUuid;
    }

    public static MyStore.Builder builder() {
        return new MyStore.Builder();
    }

    public static final class Builder {
        private String accountUuid;
        private String accountType;
        private int previousBalance;
        private long previousDate;
        private int currentChanges;
        private long currentDate;
        private int currentBalance;
        private  long lastUpdated;
        public Builder() {
        }

        public Balance build() {
            String missing = "";
            if (Strings.isNullOrEmpty(accountUuid)) {
                missing += " accountUuid ";
            }
            if (Strings.isNullOrEmpty(accountType)) {
                missing += " accountType";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new Balance(accountUuid, accountType, previousBalance, previousDate,
                    currentChanges, currentDate, currentBalance,  lastUpdated);
        }

        public Builder setAccountUuid(String accountUuid) {
            this.accountUuid = accountUuid;
            return this;
        }

        public Builder setAccountType(String accountType) {
            this.accountType = accountType;
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

        public Builder setLastUpdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }
    }


}

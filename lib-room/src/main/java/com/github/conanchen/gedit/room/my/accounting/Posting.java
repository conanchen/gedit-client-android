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
        @Index(value = {"accountUuid"}),
        @Index(value = {"lastUpdated"})
})
public class Posting {
//### 账户流水Postings
//`根据系统记账需要的事实计算奖励积分、转换积分，记录在相应账户流水里。`
//
//            | 流水编号UUID      | 用户编号UUID     | 账户编号UUID        |   日志编号UUID    | 数量   | 创建日期  |
//            | :--------------- |:----------------| :------------------|-----------------:|-------:|--------:|
//            | posting-uuid-001 | user-uuid-001   | account-uuid-101   | journal-uuid-001 |  -30   |20180101 |
//            | posting-uuid-002 | user-uuid-002   | account-uuid-102   | journal-uuid-001 |   30   |20180101 |
//            | posting-uuid-003 | user-uuid-003   | account-uuid-103   | journal-uuid-001 |   20   |20180101 |
//            | posting-uuid-004 | user-uuid-004   | account-uuid-104   | journal-uuid-001 |  -20   |20180101 |
//
    @PrimaryKey
    @NonNull
    public String uuid;
    public String journalUuid;
    public String accountUuid;

    public String type;//posting type
    public int amount; //posting amount
    public long created;

    public Posting(@NonNull String uuid, String journalUuid, String accountUuid, String type, int amount, long created) {
        this.uuid = uuid;
        this.journalUuid = journalUuid;
        this.accountUuid = accountUuid;
        this.type = type;
        this.amount = amount;
        this.created = created;
    }

    public static DiffCallback<Posting> DIFF_CALLBACK = new DiffCallback<Posting>() {
        @Override
        public boolean areItemsTheSame(@NonNull Posting oldItem, @NonNull Posting newItem) {
            return oldItem.uuid == newItem.uuid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Posting oldItem, @NonNull Posting newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Posting myStore = (Posting) obj;

        return myStore.accountUuid == this.accountUuid;
    }

    public static Posting.Builder builder() {
        return new Posting.Builder();
    }

    public static final class Builder {
        private String uuid;
        private String journalUuid;
        private String accountUuid;

        private String type;//posting type
        private int amount; //posting amount
        private long created;

        public Builder() {
        }

        public Posting build() {
            String missing = "";
            if (Strings.isNullOrEmpty(uuid)) {
                missing += " uuid ";
            }
            if (Strings.isNullOrEmpty(journalUuid)) {
                missing += " journalUuid ";
            }
            if (Strings.isNullOrEmpty(accountUuid)) {
                missing += " accountUuid ";
            }
            if (Strings.isNullOrEmpty(type)) {
                missing += " type";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }
            return new Posting(uuid,   journalUuid,   accountUuid,   type,   amount,   created);
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setJournalUuid(String journalUuid) {
            this.journalUuid = journalUuid;
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

        public Builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder setCreated(long created) {
            this.created = created;
            return this;
        }
    }


}

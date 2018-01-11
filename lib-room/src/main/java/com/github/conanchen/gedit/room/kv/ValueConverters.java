package com.github.conanchen.gedit.room.kv;

import android.arch.persistence.room.TypeConverter;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ValueConverters {
    private static final Gson gson = new GsonBuilder().create();

    //---------------
    @TypeConverter
    public static Value gsonString2Vo(String gsonString) {

        if (Strings.isNullOrEmpty(gsonString))
            return null;


        return gson.fromJson(gsonString, Value.class);
    }

    @TypeConverter
    public static String vo2GsonString(Value value) {

        if (value == null)
            return null;

        return gson.toJson(value, Value.class);
    }

}
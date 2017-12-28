package com.github.conanchen.yeamore.hello.room;

import android.arch.persistence.room.TypeConverter;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    private static final Gson gson = new GsonBuilder().create();


    @TypeConverter
    public static List<String> fromStringToList(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromStringListToString(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
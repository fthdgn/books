package com.fthdgn.books.repository;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class AuthorConverter {

    @TypeConverter
    public static List<String> authorsFromString(String value) {
        String[] values = value.split("\\|");
        List<String> authors = new ArrayList<>(values.length);
        for (String v : values) {
            if (v.length() > 0) {
                authors.add(v);
            }
        }
        return authors;
    }

    @TypeConverter
    public static String authorsToString(List<String> values) {
        if (values == null)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            stringBuilder.append("|").append(value).append("|");
        }
        return stringBuilder.toString();
    }

}

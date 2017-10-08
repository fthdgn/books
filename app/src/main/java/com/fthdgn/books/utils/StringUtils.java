package com.fthdgn.books.utils;

import android.support.annotation.Nullable;

public class StringUtils {

    public static boolean isEmpty(@Nullable String string) {
        return string == null || string.isEmpty() || string.trim().isEmpty();
    }

}

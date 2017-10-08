package com.fthdgn.books.api.response;

import android.support.annotation.Keep;

import java.util.List;

@Keep
@SuppressWarnings({"unused", "WeakerAccess"})
public class Volumes {

    public static final String KIND_BOOKS_VOLUMES = "books#volumes";

    /**
     * {@link #KIND_BOOKS_VOLUMES}
     */
    public String kind;
    public int totalItems;
    public List<Volume> items;

}

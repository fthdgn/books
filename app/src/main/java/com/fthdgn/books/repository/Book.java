package com.fthdgn.books.repository;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fthdgn.books.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
@Entity
public class Book {

    @PrimaryKey
    @NonNull
    private final String bookId;

    private String title;

    private String sortTitle;

    @TypeConverters({AuthorConverter.class})
    private List<String> authors;

    private String originalTitle;

    @TypeConverters({AuthorConverter.class})
    private List<String> originalAuthors;

    public Book(@NonNull String bookId) {
        this.bookId = bookId;
    }

    @NonNull
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSortTitle() {
        return sortTitle;
    }

    public void setSortTitle(String sortTitle) {
        this.sortTitle = sortTitle;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<String> getOriginalAuthors() {
        return originalAuthors;
    }

    public void setOriginalAuthors(List<String> originalAuthors) {
        this.originalAuthors = originalAuthors;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Book))
            return false;
        Book b = (Book) obj;

        return ObjectUtils.equals(this.bookId, b.bookId)
                && ObjectUtils.equals(this.title, b.title)
                && ObjectUtils.equals(this.sortTitle, b.sortTitle)
                && ObjectUtils.equals(this.authors, b.authors)
                && ObjectUtils.equals(this.originalAuthors, b.originalAuthors);
    }

    public void setFormattedAuthors(@Nullable String formattedAuthors) {
        setAuthors(formattedStringToStringArray(formattedAuthors));
    }

    @NonNull
    public String getFormattedAuthors() {
        String r = stringArrayToFormattedString(getAuthors());
        return r == null ? "" : r;
    }

    @NonNull
    public String getFormattedOriginalAuthors() {
        String r = stringArrayToFormattedString(getOriginalAuthors());
        return r == null ? "" : r;
    }

    @Nullable
    private String stringArrayToFormattedString(@Nullable List<String> array) {
        if (array == null)
            return null;
        else if (array.size() == 1)
            return array.get(0);
        else {
            StringBuilder s;
            s = new StringBuilder(array.get(0));
            for (int i = 1; i < array.size(); i++)
                s.append(", ").append(array.get(i));
            return s.toString();
        }
    }

    @Nullable
    private List<String> formattedStringToStringArray(@Nullable String string) {
        if (string == null)
            return null;

        String[] tmp = string.split(",");
        ArrayList<String> strings = new ArrayList<>();
        for (String t : tmp) {
            t = t.trim();
            if (!t.equals(""))
                strings.add(t);
        }
        return strings;
        //TODO Be sure never returns array with length 0
    }

}

package tr.name.fatihdogan.books.data;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import tr.name.fatihdogan.books.BaseApplication;
import tr.name.fatihdogan.books.Constants;
import tr.name.fatihdogan.books.utils.ArrayUtils;
import tr.name.fatihdogan.books.utils.FileUtils;
import tr.name.fatihdogan.googlebooksapi.output.VolumeOutput;

public class Book {

    public static void clear() {
        BOOKS.clear();
        FileUtils.deleteDirectory(Constants.localBookPath);
        FileUtils.deleteDirectory(Constants.onlineBookPath);
        FileUtils.deleteDirectory(Constants.localCoverPath);
        FileUtils.deleteDirectory(Constants.onlineCoverPath);
        notifyChange();
    }

    private static class BookData {

        @Nullable
        String title;
        @Nullable
        String[] author;
        @Nullable
        String sortTitle;
        @Nullable
        Date lastUsage;
    }

    public static final HashMap<String, Book> BOOKS = new HashMap<>();

    @NonNull
    private final String id;
    @Nullable
    private BookData localData;
    @NonNull
    private BookData onlineData;
    private boolean localDataDirty;

    private Book(@NonNull String id) {
        this.id = id;
        onlineData = new BookData();
    }

    public void setTitle(@Nullable String title) {
        if (localData == null)
            localData = new BookData();
        localDataDirty = true;
        if (title == null) {
            localData.title = null;
            return;
        }
        title = title.trim();
        if (title.equals(""))
            title = null;
        localData.title = title;
    }

    @NonNull
    public String getTitle() {
        if (localData != null && localData.title != null)
            return localData.title;
        else if (onlineData.title != null)
            return onlineData.title;
        else
            return "";
    }

    @Nullable
    public String getLocalTitle() {
        return localData == null ? null : localData.title;
    }

    @Nullable
    public String getOnlineTitle() {
        return onlineData.title;
    }

    public void setSortTitle(@Nullable String sortTitle) {
        if (localData == null)
            localData = new BookData();
        localDataDirty = true;
        if (sortTitle != null) {
            sortTitle = sortTitle.trim();
            if (sortTitle.equals(""))
                sortTitle = null;
        }
        localData.sortTitle = sortTitle;
    }

    @NonNull
    public String getSortTitle() {
        if (localData != null) {
            if (localData.sortTitle != null)
                return localData.sortTitle;
        }

        if (onlineData.title != null)
            return onlineData.title;
        else
            return "";
    }

    @Nullable
    public String getLocalSortTitle() {
        return localData == null ? null : localData.sortTitle;
    }

    @Nullable
    public String getOnlineSortTitle() {
        return onlineData.title;
    }

    private void setAuthors(@Nullable String[] authors) {
        if (localData == null)
            localData = new BookData();
        localDataDirty = true;
        if (authors != null && authors.length == 0)
            authors = null;

        if (authors != null)
            authors = authors.clone();

        localData.author = authors;
    }

    public void setAuthors(@Nullable String formattedAuthors) {
        setAuthors(formattedStringToStringArray(formattedAuthors));
    }

    @Nullable
    public String[] getAuthors() {
        if (localData != null && localData.author != null)
            return localData.author.clone();
        else if (onlineData.author != null)
            return onlineData.author.clone();
        return null;
    }

    @NonNull
    public String getFormattedAuthors() {
        String r = stringArrayToFormattedString(getAuthors());
        return r == null ? "" : r;
    }

    @Nullable
    private String[] getLocalAuthors() {
        if (localData == null)
            return null;
        else {
            if (localData.author == null)
                return null;
            else
                return localData.author.clone();
        }
    }

    @Nullable
    public String getFormattedLocalAuthors() {
        return stringArrayToFormattedString(getLocalAuthors());
    }

    @Nullable
    private String[] getOnlineAuthors() {
        if (onlineData.author != null)
            return onlineData.author.clone();
        return null;
    }

    @Nullable
    public String getFormattedOnlineAuthors() {
        return stringArrayToFormattedString(getOnlineAuthors());
    }

    @NonNull
    public Date getLastUsageDate() {
        if (localData != null && localData.lastUsage != null) {
            return localData.lastUsage;
        } else if (onlineData.lastUsage != null) {
            return onlineData.lastUsage;
        }
        return new Date(0);
    }

    public void updateLastUsageDate() {
        if (localData == null)
            localData = new BookData();
        localData.lastUsage = new Date();
        notifyChange();
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getCover() {
        File localCover = new File(Constants.localCoverPath, id);
        if (localCover.exists()) {
            return localCover.getAbsolutePath();
        } else {
            File onlineCover = new File(Constants.onlineCoverPath, id);
            if (onlineCover.exists()) {
                return onlineCover.getAbsolutePath();
            }
        }
        return null;
    }

    @Nullable
    private String stringArrayToFormattedString(@Nullable String[] array) {
        if (array == null)
            return null;
        else if (array.length == 1)
            return array[0];
        else {
            String s;
            s = array[0];
            for (int i = 1; i < array.length; i++)
                s += ", " + array[i];
            return s;
        }
    }

    @Nullable
    private String[] formattedStringToStringArray(@Nullable String string) {
        if (string == null)
            return null;

        String[] tmp = string.split(",");
        ArrayList<String> strings = new ArrayList<>();
        for (String t : tmp) {
            t = t.trim();
            if (!t.equals(""))
                strings.add(t);
        }
        return strings.toArray(new String[strings.size()]);
        //TODO Be sure never returns array with length 0
    }

    @SuppressWarnings("unused")
    public void save() {
        if (!localDataDirty)
            return;
        FileUtils.writeSilently(new File(Constants.localBookPath, id), new Gson().toJson(this.localData));
        localDataDirty = false;
        notifyChange();
    }

    public static void loadBooks() {
        Gson gson = new Gson();
        BOOKS.clear();
        File[] onlineBooks = new File(Constants.onlineBookPath).listFiles();
        if (onlineBooks == null)
            return;
        for (File onlineBook : onlineBooks) {
            String id = onlineBook.getName();
            String online = FileUtils.readStringSilently(onlineBook);
            String local = FileUtils.readStringSilently(new File(Constants.localBookPath, id));

            Book book = new Book(id);
            book.onlineData = gson.fromJson(online, BookData.class);
            if (local != null) {
                book.localData = gson.fromJson(local, BookData.class);
            }
            BOOKS.put(id, book);
        }
        notifyChange();
    }

    public static void createBook(VolumeOutput volumeOutput) {
        Book book = new Book(volumeOutput.id);
        book.onlineData.title = volumeOutput.volumeInfo.title;
        book.onlineData.author = volumeOutput.volumeInfo.authors;
        if (volumeOutput.userInfo != null && volumeOutput.userInfo.readingPosition != null)
            book.onlineData.lastUsage = volumeOutput.userInfo.readingPosition.updated;
        try {
            FileUtils.write(new File(Constants.onlineBookPath, book.id), new Gson().toJson(book.onlineData));
            BOOKS.put(book.id, book);
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyChange();
    }

    /**
     * Changes authors' names of all books
     *
     * @param oldName Author name which will be changed
     * @param newName New author name
     */
    public static void changeAuthorNames(String oldName, String newName) {
        for (Book book : BOOKS.values()) {
            String[] authors = book.getAuthors();
            if (authors != null)
                ArrayUtils.replace(authors, oldName, newName);
            book.setAuthors(authors);
        }
        notifyChange();
    }

    public static final String BOOKS_CHANGED_EVENT = "BOOKS_CHANGED_EVENT";

    private static void notifyChange() {
        Intent intent = new Intent(BOOKS_CHANGED_EVENT);
        LocalBroadcastManager.getInstance(BaseApplication.getInstance()).sendBroadcast(intent);
    }

}

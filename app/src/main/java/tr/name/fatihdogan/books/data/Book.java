package tr.name.fatihdogan.books.data;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import tr.name.fatihdogan.googlebooksapi.output.VolumeOutput;

import static tr.name.fatihdogan.books.BaseApplication.getFilesDirPath;
import static tr.name.fatihdogan.books.utils.FileUtils.deleteDirectory;
import static tr.name.fatihdogan.books.utils.FileUtils.readStringFromFileSafely;
import static tr.name.fatihdogan.books.utils.FileUtils.saveStringToFile;
import static tr.name.fatihdogan.books.utils.FileUtils.saveStringToFileSafely;

public class Book {

    public static void clear() {
        BOOKS.clear();
        deleteDirectory(localBookPath);
        deleteDirectory(onlineBookPath);
        deleteDirectory(localCoverPath);
        deleteDirectory(onlineCoverPath);
    }

    private static class BookData {

        String title;
        String[] author;

    }

    private static String localBookPath = getFilesDirPath() + File.separator + "local" + File.separator + "book";
    private static String onlineBookPath = getFilesDirPath() + File.separator + "online" + File.separator + "book";
    private static String localCoverPath = getFilesDirPath() + File.separator + "local" + File.separator + "cover";
    private static String onlineCoverPath = getFilesDirPath() + File.separator + "online" + File.separator + "cover";

    public static HashMap<String, Book> BOOKS = new HashMap<>();

    private String id;
    private BookData localData;
    private BookData onlineData;
    private boolean localDataDirty;

    private void localCreator() {
        if (localData == null)
            localData = new BookData();
    }

    @SuppressWarnings("unused")
    void setTitle(String title) {
        localCreator();
        localData.title = title;
        localDataDirty = true;
    }

    @SuppressWarnings("unused")
    void setAuthors(String[] authors) {
        localCreator();
        localData.author = authors;
        localDataDirty = true;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public String getCover() {
        File localCover = new File(localCoverPath, id);
        if (localCover.exists()) {
            return localCover.getAbsolutePath();
        } else {
            File onlineCover = new File(onlineCoverPath, id);
            if (onlineCover.exists()) {
                return onlineCover.getAbsolutePath();
            }
        }
        return null;
    }

    public String getTitle() {
        if (localData != null && localData.title != null)
            return localData.title;
        else
            return onlineData.title;
    }

    @SuppressWarnings("WeakerAccess")
    public String[] getAuthors() {
        if (localData != null && localData.author != null)
            return localData.author;
        else
            return onlineData.author;
    }

    public String getFormatedAuthorNames() {
        String[] a = getAuthors();
        if (a == null)
            return "";
        else if (a.length == 1)
            return a[0];
        else {
            String s;
            s = a[0];
            for (int i = 1; i < a.length; i++)
                s += ", " + a[i];
            return s;

        }
    }

    @SuppressWarnings("unused")
    public void save() {
        if (!localDataDirty)
            return;
        saveStringToFileSafely(new File(localBookPath, id), new Gson().toJson(this.localData));
        localDataDirty = false;
    }

    public static void loadBooks() {
        Gson gson = new Gson();
        BOOKS.clear();
        File[] onlineBooks = new File(onlineBookPath).listFiles();
        if (onlineBooks == null)
            return;
        for (File onlineBook : onlineBooks) {
            String id = onlineBook.getName();
            String online = readStringFromFileSafely(onlineBook);
            String local = readStringFromFileSafely(new File(localBookPath, id));

            Book book = new Book();
            book.id = id;
            book.onlineData = gson.fromJson(online, BookData.class);
            if (local != null) {
                book.localData = gson.fromJson(local, BookData.class);
            }
            BOOKS.put(id, book);
        }
    }

    public static void createBook(VolumeOutput volumeOutput) {
        Book book = new Book();
        book.id = volumeOutput.id;
        book.onlineData = new BookData();
        book.onlineData.title = volumeOutput.volumeInfo.title;
        book.onlineData.author = volumeOutput.volumeInfo.authors;
        try {
            saveStringToFile(new File(onlineBookPath, book.id), new Gson().toJson(book.onlineData));
            BOOKS.put(book.id, book);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

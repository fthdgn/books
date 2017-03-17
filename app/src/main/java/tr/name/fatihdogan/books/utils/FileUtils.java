package tr.name.fatihdogan.books.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings({"WeakerAccess", "unused"})
public class FileUtils {
    //region Write Operations

    /**
     * Writes byte data to file
     *
     * @param file The target file, it is created and overwritten
     * @param data Data array
     * @throws IOException Exception while IO operations
     * @see FileUtils#write(File, String)
     * @see FileUtils#writeSilently(File, byte[])
     * @see FileUtils#writeSilently(File, String)
     */
    public static void write(@NonNull File file, @NonNull byte[] data) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * Writes string to file
     *
     * @param file   The target file, it is created and overwritten
     * @param string String content
     * @throws IOException Exception while IO operations
     * @see FileUtils#write(File, byte[])
     * @see FileUtils#writeSilently(File, byte[])
     * @see FileUtils#writeSilently(File, String)
     */
    public static void write(@NonNull File file, @NonNull String string) throws IOException {
        write(file, string.getBytes());
    }

    /**
     * Writes byte data to the file
     * This method does not throw exception in case of an error.
     *
     * @param file The target file, it is created and overwritten
     * @param data Data array
     * @see FileUtils#write(File, byte[])
     * @see FileUtils#write(File, String)
     * @see FileUtils#writeSilently(File, String)
     */
    public static void writeSilently(@NonNull File file, @NonNull byte[] data) {
        try {
            write(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes string to file
     * This method does not throw exception in case of an error.
     *
     * @param file   The target file, it is created and overwritten
     * @param string String content
     * @see FileUtils#write(File, byte[])
     * @see FileUtils#write(File, String)
     * @see FileUtils#writeSilently(File, byte[])
     */
    public static void writeSilently(@NonNull File file, @NonNull String string) {
        writeSilently(file, string.getBytes());
    }

    /**
     * Writes byte data to file
     *
     * @param path The path of target file, it is created and overwritten
     * @param data Data array
     * @throws IOException Exception while IO operations
     * @see FileUtils#write(File, String)
     * @see FileUtils#writeSilently(File, byte[])
     * @see FileUtils#writeSilently(File, String)
     */
    public static void write(@NonNull String path, @NonNull byte[] data) throws IOException {
        write(new File(path), data);
    }

    /**
     * Writes string to file
     *
     * @param path   The path of target file, it is created and overwritten
     * @param string String content
     * @throws IOException Exception while IO operations
     */
    public static void write(@NonNull String path, @NonNull String string) throws IOException {
        write(new File(path), string.getBytes());
    }

    /**
     * Writes byte data to the file
     * This method does not throw exception in case of an error.
     *
     * @param path The path of target file, it is created and overwritten
     * @param data Data array
     * @see FileUtils#write(File, byte[])
     * @see FileUtils#write(File, String)
     * @see FileUtils#writeSilently(File, String)
     */
    public static void writeSilently(@NonNull String path, @NonNull byte[] data) {
        writeSilently(new File(path), data);
    }

    /**
     * Writes string to file
     * This method does not throw exception in case of an error.
     *
     * @param path   The  path target file, it is created and overwritten
     * @param string String content
     * @see FileUtils#write(File, byte[])
     * @see FileUtils#write(File, String)
     * @see FileUtils#writeSilently(File, byte[])
     */
    public static void writeSilently(@NonNull String path, @NonNull String string) {
        writeSilently(new File(path), string.getBytes());
    }
    //endregion

    //region Read Operations

    /**
     * Reads string content of a file
     *
     * @param file The source file
     * @return Content of the file
     * @throws IOException Exception while IO operations
     */
    public static String readString(@NonNull File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        StringBuilder fileContent = new StringBuilder("");

        byte[] buffer = new byte[1024];
        int n;
        while ((n = (fis.read(buffer))) != -1) {
            fileContent.append(new String(buffer, 0, n));
        }
        //TODO Check whether it is nullable
        return fileContent.toString();
    }

    /**
     * Reads string content of a file
     * This method does not throw exception in case of an error.
     *
     * @param file The source file
     * @return Content of the file, null if cannot read
     */
    @Nullable
    public static String readStringSilently(@NonNull File file) {
        try {
            return readString(file);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads string content of a file
     *
     * @param path The path of source file
     * @return Content of the file
     * @throws IOException Exception while IO operations
     */
    public static String readString(@NonNull String path) throws IOException {
        return readString(new File(path));
    }

    /**
     * Reads string content of a file
     * This method does not throw exception in case of an error.
     *
     * @param path The path of source file
     * @return Content of the file, null if cannot read
     */
    @Nullable
    public static String readStringSilently(@NonNull String path) {
        return readStringSilently(new File(path));
    }
    //endregion

    //region Delete Operations

    /**
     * Deletes a file or a directory recursively
     *
     * @param file File or directory
     */
    public static void deleteDirectory(File file) {
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            for (File f : file.listFiles())
                deleteDirectory(f);
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        } else {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    /**
     * Deletes a file or a directory recursively
     *
     * @param path The path of file or directory
     */
    public static void deleteDirectory(String path) {
        deleteDirectory(new File(path));
    }
    //endregion

}

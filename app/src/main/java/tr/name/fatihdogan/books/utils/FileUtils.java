package tr.name.fatihdogan.books.utils;

import java.io.File;

@SuppressWarnings({"unused", "WeakerAccess"})
public class FileUtils {

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
}

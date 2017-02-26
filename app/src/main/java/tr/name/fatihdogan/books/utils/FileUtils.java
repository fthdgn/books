package tr.name.fatihdogan.books.utils;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings({"WeakerAccess", "unused"})
public class FileUtils {

    public static void saveBytesToFile(File file, byte[] data) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    public static void saveBytesToPath(String path, byte[] data) throws IOException {
        File file = new File(path);
        saveBytesToFile(file, data);
    }

    public static void saveBytesToFileSafely(File file, byte[] data) {
        try {
            saveBytesToFile(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBytesToPathSafely(String path, byte[] data) {
        try {
            saveBytesToPath(path, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveStringToPath(String path, String data) throws IOException {
        File file = new File(path);
        saveStringToFile(file, data);
    }

    public static void saveStringToFile(File file, String data) throws IOException {
        saveBytesToFile(file, data.getBytes());
    }

    public static void saveStringToPathSafely(String path, String data) {
        File file = new File(path);
        saveStringToFileSafely(file, data);
    }

    public static void saveStringToFileSafely(File file, String data) {
        saveBytesToFileSafely(file, data.getBytes());
    }

    public static String readStringFromFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        StringBuilder fileContent = new StringBuilder("");

        byte[] buffer = new byte[1024];
        int n;
        while ((n = (fis.read(buffer))) != -1) {
            fileContent.append(new String(buffer, 0, n));
        }
        return fileContent.toString();
    }

    @Nullable
    public static String readStringFromFileSafely(File file) {
        try {
            return readStringFromFile(file);
        } catch (IOException e) {
            return null;
        }
    }

    public static void deleteDirectory(String path) {
        deleteDirectory(new File(path));
    }

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

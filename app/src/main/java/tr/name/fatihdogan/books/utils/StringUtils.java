package tr.name.fatihdogan.books.utils;

public class StringUtils {

    public static boolean contains(String string, String[] strings) {
        for (String s : strings) {
            if (s.equals(string))
                return true;
        }
        return false;
    }
}

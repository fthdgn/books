package tr.name.fatihdogan.books.utils;

import android.support.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ObjectUtils {

    /**
     * Null-safe equals method
     * Returns true if both object are null.
     * Uses Object{@link #equals(Object)} method.
     *
     * @param o1 First object
     * @param o2 Second object
     * @return Whether they are equals
     */
    public static boolean equals(@Nullable Object o1, @Nullable Object o2) {
        return o1 == o2 || o1 != null && o2 != null && o1.equals(o2);
    }
}

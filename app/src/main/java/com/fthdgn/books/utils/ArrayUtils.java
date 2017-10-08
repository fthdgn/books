package com.fthdgn.books.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ArrayUtils {

    /**
     * Searches of an object inside an array
     * This methods uses {@link Object#equals(Object)}for nonnull objects.
     * For null objects null index of array.
     *
     * @param objects    Array
     * @param object     Object
     * @param startIndex Starting index
     * @return Index of object inside array, -1 means not found
     */
    public static int indexOf(@NonNull Object[] objects, @Nullable Object object, int startIndex) {
        for (int i = startIndex; i < objects.length; i++) {
            if (ObjectUtils.equals(objects[i], object))
                return i;
        }
        return -1;
    }

    /**
     * Searches of an object inside an array
     * This methods uses {@link Object#equals(Object)}for nonnull objects.
     * For null objects null index of array.
     *
     * @param objects Array
     * @param object  Object
     * @return Index of object inside array, -1 means not found
     */
    public static int indexOf(@NonNull Object[] objects, @Nullable Object object) {
        return indexOf(objects, object, 0);
    }

    /**
     * Checks whether an array contains an object
     * This methods uses {@link Object#equals(Object)}for nonnull objects.
     * For null objects checks whether array contains null index.
     *
     * @param objects Array
     * @param object  Object
     * @return Whether the array contains the object
     */
    public static boolean contains(@NonNull Object[] objects, @Nullable Object object) {
        return indexOf(objects, object) != -1;
    }

    /**
     * Replaces old object with the new one on the array.
     * Objects can be null. Uses Object{@link #equals(Object)}.
     *
     * @param objects   Array
     * @param oldObject Object which will be replaced
     * @param newObject New object
     */
    public static void replace(@NonNull Object[] objects, @Nullable Object oldObject, @Nullable Object newObject) {
        for (int i = 0; i < objects.length; i++) {
            if (ObjectUtils.equals(objects[i], oldObject))
                objects[i] = newObject;
        }
    }
}

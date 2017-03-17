package tr.name.fatihdogan.books.utils;

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
    public static int indexOf(Object[] objects, Object object, int startIndex) {
        for (int i = startIndex; i < objects.length; i++) {
            Object o = objects[i];
            if (object == o)
                return i;

            if (object == null)
                continue;

            if (o == null)
                continue;

            if (o.equals(object))
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
    public static int indexOf(Object[] objects, Object object) {
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
    public static boolean contains(Object[] objects, Object object) {
        return indexOf(objects, object) != -1;
    }
}

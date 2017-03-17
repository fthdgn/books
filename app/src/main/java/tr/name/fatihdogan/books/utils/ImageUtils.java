package tr.name.fatihdogan.books.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ImageUtils {

    /**
     * Creates a solid color bitmap
     *
     * @param width  Width of bitmap
     * @param height Height of bitmap
     * @param color  Color of bitmap
     * @return Solid color bitmap
     */
    @NonNull
    public static Bitmap createImage(@IntRange(from = 1) int width, @IntRange(from = 1) int height, @ColorInt int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

    /**
     * Calculates a average color from given bitmap
     * Less pixel scaling means more accurate average,
     * but it increases calculation time
     *
     * @param bitmap       Bitmap
     * @param pixelSpacing Pixel spacing, higher values are more accurate but slower
     * @return Average color as ColorInt
     */
    @ColorInt
    public static int calculateAverageColor(@NonNull Bitmap bitmap, @IntRange(from = 1) int pixelSpacing) {
        int R = 0;
        int G = 0;
        int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i += pixelSpacing) {
            int color = pixels[i];
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        return Color.rgb(R / n, G / n, B / n);
    }
}

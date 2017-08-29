package tr.name.fatihdogan.books.api.response;

import android.support.annotation.Keep;

import java.util.Date;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class ReadingPosition {

    public static final String KIND_BOOKS_READINGPOSITION = "books#readingPosition";

    /**
     * {@link #KIND_BOOKS_READINGPOSITION}
     */
    public String kind;
    public String volumeId;
    public String gbTextPosition;
    public String gbImagePosition;
    public String epubCfiPosition;
    public String pdfPosition;
    public Date updated;

}

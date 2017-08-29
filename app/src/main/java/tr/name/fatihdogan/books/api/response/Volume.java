package tr.name.fatihdogan.books.api.response;

import android.support.annotation.Keep;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class Volume {

    public static final String KIND_BOOKS_VOLUME = "books#volume";

    /**
     * {@link #KIND_BOOKS_VOLUME}
     */
    public String kind;
    public String id;
    public String etag;
    public String selfLink;
    public VolumeInfo volumeInfo;
    public UserInfo userInfo;
    public SaleInfo saleInfo;
    public AccessInfo accessInfo;
    public SearchInfo searchInfo;
}

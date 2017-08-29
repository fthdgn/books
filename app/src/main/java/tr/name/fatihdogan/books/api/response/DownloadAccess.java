package tr.name.fatihdogan.books.api.response;

import android.support.annotation.Keep;

@Keep
@SuppressWarnings({"unused", "WeakerAccess"})
public class DownloadAccess {

    public static final String KIND_BOOKS_DOWNLOAD_ACCESS_RESTRICTION = "books#downloadAccessRestriction";

    /**
     * {@link #KIND_BOOKS_DOWNLOAD_ACCESS_RESTRICTION}
     */
    public String kind;
    public String volumeId;
    public Boolean restricted;
    public Boolean deviceAllowed;
    public Boolean justAcquired;
    public Integer maxDownloadDevices;
    public Integer downloadsAcquired;
    public String nonce;
    public String source;
    public String reasonCode;
    public String message;
    public String signature;

}

package com.fthdgn.books.api.response;

import android.support.annotation.Keep;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Keep
@SuppressWarnings({"unused", "WeakerAccess"})
public class AccessInfo {

    //region Viewability
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            VIEWABILITY_ALL_PAGE,
            VIEWABILITY_PARTIAL,
            VIEWABILITY_NO_PAGES,
            VIEWABILITY_UNKNOWN
    })
    public @interface Viewability {

    }

    public static final String VIEWABILITY_ALL_PAGE = "ALL_PAGES";
    public static final String VIEWABILITY_PARTIAL = "PARTIAL";
    public static final String VIEWABILITY_NO_PAGES = "NO_PAGES";
    public static final String VIEWABILITY_UNKNOWN = "UNKNOWN";

    //endregion

    //region AccessViewStatus
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ACCESS_VIEW_STATUS_FULL_PURCHASED,
            ACCESS_VIEW_STATUS_FULL_PUBLIC_DOMAIN,
            ACCESS_VIEW_STATUS_SAMPLE,
            ACCESS_VIEW_STATUS_NONE
    })
    public @interface AccessViewStatus {

    }

    public static final String ACCESS_VIEW_STATUS_FULL_PURCHASED = "FULL_PURCHASED";
    public static final String ACCESS_VIEW_STATUS_FULL_PUBLIC_DOMAIN = "FULL_PUBLIC_DOMAIN";
    public static final String ACCESS_VIEW_STATUS_SAMPLE = "SAMPLE";
    public static final String ACCESS_VIEW_STATUS_NONE = "NONE";

    //endregion

    //region TextToSpeechPermission
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            TEXT_TO_SPEECH_PERMISSION_ALLOWED,
            TEXT_TO_SPEECH_PERMISSION_ALLOWED_FOR_ACCESSIBILITY,
            TEXT_TO_SPEECH_PERMISSION_NOT_ALLOWED
    })
    public @interface TextToSpeechPermission {

    }

    public static final String TEXT_TO_SPEECH_PERMISSION_ALLOWED = "ALLOWED";
    public static final String TEXT_TO_SPEECH_PERMISSION_ALLOWED_FOR_ACCESSIBILITY = "ALLOWED_FOR_ACCESSIBILITY";
    public static final String TEXT_TO_SPEECH_PERMISSION_NOT_ALLOWED = "NOT_ALLOWED";
    //endregion

    public String country;
    @Viewability
    public String viewability;
    public Boolean embeddable;
    public Boolean publicDomain;
    @TextToSpeechPermission
    public String textToSpeechPermission;
    public EpubPdf epub;
    public EpubPdf pdf;
    public String webReaderLink;
    public Boolean quoteSharingAllowed;
    @AccessViewStatus
    public String accessViewStatus;
    public DownloadAccess downloadAccess;

}

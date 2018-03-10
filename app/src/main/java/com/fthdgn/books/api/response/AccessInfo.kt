@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep
import android.support.annotation.StringDef


@Keep
class AccessInfo {

    var country: String? = null
    @Viewability
    var viewability: String? = null
    var embeddable: Boolean? = null
    var publicDomain: Boolean? = null
    @TextToSpeechPermission
    var textToSpeechPermission: String? = null
    var epub: EpubPdf? = null
    var pdf: EpubPdf? = null
    var webReaderLink: String? = null
    var quoteSharingAllowed: Boolean? = null
    @AccessViewStatus
    var accessViewStatus: String? = null
    var downloadAccess: DownloadAccess? = null


    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @StringDef(VIEWABILITY_ALL_PAGE, VIEWABILITY_PARTIAL, VIEWABILITY_NO_PAGES, VIEWABILITY_UNKNOWN)
        annotation class Viewability


        @Retention(AnnotationRetention.SOURCE)
        @StringDef(ACCESS_VIEW_STATUS_FULL_PURCHASED, ACCESS_VIEW_STATUS_FULL_PUBLIC_DOMAIN, ACCESS_VIEW_STATUS_SAMPLE, ACCESS_VIEW_STATUS_NONE)
        annotation class AccessViewStatus


        @Retention(AnnotationRetention.SOURCE)
        @StringDef(TEXT_TO_SPEECH_PERMISSION_ALLOWED, TEXT_TO_SPEECH_PERMISSION_ALLOWED_FOR_ACCESSIBILITY, TEXT_TO_SPEECH_PERMISSION_NOT_ALLOWED)
        annotation class TextToSpeechPermission

        const val VIEWABILITY_ALL_PAGE = "ALL_PAGES"
        const val VIEWABILITY_PARTIAL = "PARTIAL"
        const val VIEWABILITY_NO_PAGES = "NO_PAGES"
        const val VIEWABILITY_UNKNOWN = "UNKNOWN"

        const val ACCESS_VIEW_STATUS_FULL_PURCHASED = "FULL_PURCHASED"
        const val ACCESS_VIEW_STATUS_FULL_PUBLIC_DOMAIN = "FULL_PUBLIC_DOMAIN"
        const val ACCESS_VIEW_STATUS_SAMPLE = "SAMPLE"
        const val ACCESS_VIEW_STATUS_NONE = "NONE"

        const val TEXT_TO_SPEECH_PERMISSION_ALLOWED = "ALLOWED"
        const val TEXT_TO_SPEECH_PERMISSION_ALLOWED_FOR_ACCESSIBILITY = "ALLOWED_FOR_ACCESSIBILITY"
        const val TEXT_TO_SPEECH_PERMISSION_NOT_ALLOWED = "NOT_ALLOWED"
    }

}

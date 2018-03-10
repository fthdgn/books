@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep
import android.support.annotation.StringDef

@Keep
class VolumeInfo {

    var title: String? = null
    var subtitle: String? = null
    var authors: Array<String>? = null
    var publisher: String? = null
    var publishedDate: String? = null
    var description: String? = null
    var industryIdentifierses: Array<IndustryIdentifiers>? = null
    var readingModes: ReadingModes? = null
    var pageCount: Int? = null
    var dimensions: Dimensions? = null
    @PrintType
    var printType: String? = null
    var mainCategory: String? = null
    var categories: Array<String>? = null
    var averageRating: Double? = null
    var ratingsCount: Int? = null
    var maturityRating: String? = null
    var allowAnonLogging: Boolean? = null
    var panelizationSummary: PanelizationSummary? = null
    var contentVersion: String? = null
    var imageLinks: ImageLinks? = null
    var language: String? = null
    var previewLink: String? = null
    var layerInfo: LayerInfo? = null
    var infoLink: String? = null
    var canonicalVolumeLink: String? = null

    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @StringDef(PRINT_TYPE_BOOK, PRINT_TYPE_MAGAZINE)
        internal annotation class PrintType

        const val PRINT_TYPE_BOOK = "BOOK"
        const val PRINT_TYPE_MAGAZINE = "MAGAZINE"
    }
}

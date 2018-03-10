@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep

@Keep
class Volume {

    /**
     * [.KIND_BOOKS_VOLUME]
     */
    var kind: String? = null
    lateinit var id: String
    var etag: String? = null
    var selfLink: String? = null
    var volumeInfo: VolumeInfo? = null
    var userInfo: UserInfo? = null
    var saleInfo: SaleInfo? = null
    var accessInfo: AccessInfo? = null
    var searchInfo: SearchInfo? = null

    companion object {

        val KIND_BOOKS_VOLUME = "books#volume"
    }
}

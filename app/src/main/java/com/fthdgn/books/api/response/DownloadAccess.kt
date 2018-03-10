@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep

@Keep
class DownloadAccess {

    /**
     * [.KIND_BOOKS_DOWNLOAD_ACCESS_RESTRICTION]
     */
    var kind: String? = null
    var volumeId: String? = null
    var restricted: Boolean? = null
    var deviceAllowed: Boolean? = null
    var justAcquired: Boolean? = null
    var maxDownloadDevices: Int? = null
    var downloadsAcquired: Int? = null
    var nonce: String? = null
    var source: String? = null
    var reasonCode: String? = null
    var message: String? = null
    var signature: String? = null

    companion object {

        val KIND_BOOKS_DOWNLOAD_ACCESS_RESTRICTION = "books#downloadAccessRestriction"
    }

}

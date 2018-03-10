@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep
import java.util.*

@Keep
class UserInfo {

    //TODO Documention mentions review but cannot find structure Review review;
    var copy: Copy? = null
    var readingPosition: ReadingPosition? = null
    var isPurchased: Boolean? = null
    var isInMyBooks: Boolean? = null
    var isPreordered: Boolean? = null
    var updated: Date? = null
    var acquiredTime: Date? = null
    var entitlementType: Int? = null

}

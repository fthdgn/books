@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep
import java.util.*

@Keep
class Copy {

    var remainingCharacterCount: Int? = null
    var allowedCharacterCount: Int? = null
    var limitType: String? = null
    var updated: Date? = null

}

@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep

@Keep
class Offer {

    var finskyOfferType: Int? = null
    var listPrice: PriceMicro? = null
    var retailPrice: PriceMicro? = null

}

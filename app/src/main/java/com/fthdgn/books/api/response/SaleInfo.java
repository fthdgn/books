package com.fthdgn.books.api.response;

import android.support.annotation.Keep;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class SaleInfo {

    //region Saleability
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SALEABILITY_FOR_SALE,
            SALEABILITY_FREE,
            SALEABILITY_NOT_FOR_SALE,
            SALEABILITY_FOR_PREORDER

    })
    public @interface Saleability {

    }

    public static final String SALEABILITY_FOR_SALE = "FOR_SALE";
    public static final String SALEABILITY_FREE = "FREE";
    public static final String SALEABILITY_NOT_FOR_SALE = "NOT_FOR_SALE";
    public static final String SALEABILITY_FOR_PREORDER = "FOR_PREORDER";
    //endregion

    public String country;
    @Saleability
    public String saleability;
    public Date onSaleDate;
    boolean isEbook;
    public Price listPrice;
    public Price retailPrice;
    public Offer[] offers;
    public String buyLink;

}

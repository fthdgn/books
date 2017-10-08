package com.fthdgn.books.api.response;

import android.support.annotation.Keep;

import java.util.Date;

@Keep
@SuppressWarnings({"unused", "WeakerAccess"})
public class Copy {

    public Integer remainingCharacterCount;
    public Integer allowedCharacterCount;
    public String limitType;
    public Date updated;

}

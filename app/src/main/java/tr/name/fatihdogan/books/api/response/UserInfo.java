package tr.name.fatihdogan.books.api.response;

import android.support.annotation.Keep;

import java.util.Date;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class UserInfo {

    //TODO Documention mentions review but cannot find structure Review review;
    public Copy copy;
    public ReadingPosition readingPosition;
    public Boolean isPurchased;
    public Boolean isInMyBooks;
    public Boolean isPreordered;
    public Date updated;
    public Date acquiredTime;
    public Integer entitlementType;

}

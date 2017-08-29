package tr.name.fatihdogan.books.api.response;

import android.support.annotation.Keep;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Keep
@SuppressWarnings({"unused", "WeakerAccess"})
public class IndustryIdentifiers {

    //region IndustryIdentifier
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            INDUSTRY_IDENTIFIER_ISBN_10,
            INDUSTRY_IDENTIFIER_ISBN_13,
            INDUSTRY_IDENTIFIER_ISSN,
            INDUSTRY_IDENTIFIER_OTHER,

    })
    public @interface IndustryIdentifier {

    }

    public static final String INDUSTRY_IDENTIFIER_ISBN_10 = "ISBN_10";
    public static final String INDUSTRY_IDENTIFIER_ISBN_13 = "ISBN_13";
    public static final String INDUSTRY_IDENTIFIER_ISSN = "ISSN";
    public static final String INDUSTRY_IDENTIFIER_OTHER = "OTHER";
    //endregion

    @IndustryIdentifier
    public String type;
    public String identifier;
}

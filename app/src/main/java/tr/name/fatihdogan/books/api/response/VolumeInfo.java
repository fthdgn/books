package tr.name.fatihdogan.books.api.response;

import android.support.annotation.Keep;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class VolumeInfo {

    //region PrintType
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            PRINT_TYPE_BOOK,
            PRINT_TYPE_MAGAZINE

    })
    @interface PrintType {

    }

    public static final String PRINT_TYPE_BOOK = "BOOK";
    public static final String PRINT_TYPE_MAGAZINE = "MAGAZINE";
    //endregion

    public String title;
    public String subtitle;
    public String[] authors;
    public String publisher;
    public String publishedDate;
    public String description;
    public IndustryIdentifiers[] industryIdentifierses;
    public ReadingModes readingModes;
    public Integer pageCount;
    public Dimensions dimensions;
    @PrintType
    public String printType;
    public String mainCategory;
    public String[] categories;
    public Double averageRating;
    public Integer ratingsCount;
    public String maturityRating;
    public Boolean allowAnonLogging;
    public PanelizationSummary panelizationSummary;
    public String contentVersion;
    public ImageLinks imageLinks;
    public String language;
    public String previewLink;
    public LayerInfo layerInfo;
    public String infoLink;
    public String canonicalVolumeLink;
}

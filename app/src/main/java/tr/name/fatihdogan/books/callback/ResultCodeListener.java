package tr.name.fatihdogan.books.callback;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface ResultCodeListener<T> {

    @Retention(SOURCE)
    @IntDef({
            OK,
            CANCEL
    })
    @interface Result {

    }

    int OK = 1;
    int CANCEL = 2;

    void onResponse(T object, @Result int resultCode);

}

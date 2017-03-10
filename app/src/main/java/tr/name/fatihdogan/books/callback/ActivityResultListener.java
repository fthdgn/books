package tr.name.fatihdogan.books.callback;

import android.content.Intent;

public interface ActivityResultListener {

    /**
     * {@link tr.name.fatihdogan.books.BaseActivity} calls this method when receives activity result.
     *
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data       An Intent, which can return result data to the caller
     *                   (various data can be attached to Intent "extras").
     * @see tr.name.fatihdogan.books.BaseActivity#startActivityForResult(Intent, ActivityResultListener)
     * @see android.app.Activity#onActivityResult(int, int, Intent)
     */
    void onActivityResult(int resultCode, Intent data);

}

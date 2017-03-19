package tr.name.fatihdogan.books.callback;

import android.content.Intent;

import tr.name.fatihdogan.books.activity.BaseActivity;

public interface ActivityResultListener {

    /**
     * {@link BaseActivity} calls this method when receives activity result.
     *
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data       An Intent, which can return result data to the caller
     *                   (various data can be attached to Intent "extras").
     * @see BaseActivity#startActivityForResult(Intent, ActivityResultListener)
     * @see android.app.Activity#onActivityResult(int, int, Intent)
     */
    void onActivityResult(int resultCode, Intent data);

}

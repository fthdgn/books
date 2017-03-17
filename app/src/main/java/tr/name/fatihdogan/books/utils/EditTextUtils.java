package tr.name.fatihdogan.books.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import tr.name.fatihdogan.books.BaseApplication;

public class EditTextUtils {

    /**
     * Focus to the edit text and open keyboard
     *
     * @param editText EditText
     */
    public static void focusAndShowKeyboard(@NonNull EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) BaseApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.setSelection(editText.getText().length());
    }
}

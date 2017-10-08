package com.fthdgn.books.utils;

import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.fthdgn.books.BaseApplication;

public class EditTextUtils {

    /**
     * Focus to the edit text and open keyboard
     *
     * @param editText EditText
     */
    public static void focusAndShowKeyboard(@NonNull EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = BaseApplication.getAppComponent().inputMethodManager();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.setSelection(editText.getText().length());
    }
}

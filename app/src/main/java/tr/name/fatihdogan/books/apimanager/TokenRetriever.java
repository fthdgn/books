package tr.name.fatihdogan.books.apimanager;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

class TokenRetriever extends AsyncTask<Object, Object, Void> {

    private final Context context;
    private final Account account;
    private final OnSuccessListener onSuccessListener;
    private final OnErrorListener onErrorListener;

    TokenRetriever(Context context, Account account, OnSuccessListener onSuccessListener, OnErrorListener onErrorListener) {
        this.context = context;
        this.account = account;
        this.onErrorListener = onErrorListener;
        this.onSuccessListener = onSuccessListener;
    }

    protected Void doInBackground(Object... params) {
        String mOuathToken = null;
        try {
            mOuathToken = GoogleAuthUtil.getToken(context, account,
                    "oauth2:https://www.googleapis.com/auth/books");

        } catch (UserRecoverableAuthException userRecoverableException) {
            if (onErrorListener != null)
                onErrorListener.onRecoverableException(userRecoverableException);
        } catch (GoogleAuthException | IOException e) {
            if (onErrorListener != null)
                onErrorListener.onException(e);
        }

        if (onSuccessListener != null)
            onSuccessListener.onSuccess(mOuathToken);
        return null;
    }

    interface OnSuccessListener {

        void onSuccess(String token);
    }

    interface OnErrorListener {

        void onRecoverableException(Exception e);

        void onException(Exception e);
    }

}

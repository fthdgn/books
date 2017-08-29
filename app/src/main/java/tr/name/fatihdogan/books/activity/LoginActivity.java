package tr.name.fatihdogan.books.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;

import tr.name.fatihdogan.books.BaseApplication;
import tr.name.fatihdogan.books.utils.ThreadUtils;

public class LoginActivity extends BaseActivity {

    private tr.name.fatihdogan.books.api.AccountManager accountManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = BaseApplication.getAppComponent().accountManager();
        accountSelection();

    }

    private void accountSelection() {
        Account account = accountManager.getAccount();
        if (account == null) {
            Log.i("ACCOUNT", "Account picking.");
            String[] accountTypes = new String[]{"com.google"};
            Intent intent;

            if (Build.VERSION.SDK_INT < 23) {
                //noinspection deprecation
                intent = AccountManager.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
            } else {
                intent = AccountManager.newChooseAccountIntent(null, null, accountTypes, null, null, null, null);
            }

            startActivityForResult(intent, (resultCode, data) -> {
                if (resultCode == Activity.RESULT_OK) {
                    Account newAccount = new Account(
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME),
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
                    retrieveToken(newAccount);
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
        } else {
            retrieveToken(account);
        }
    }

    private void retrieveToken(Account account) {

        ThreadUtils.runOnBackground(() -> {
            try {
                String token = GoogleAuthUtil.getToken(LoginActivity.this, account, "oauth2:https://www.googleapis.com/auth/books");
                accountManager.setAccount(account, token);
                setResult(RESULT_OK);
                finish();
            } catch (UserRecoverableAuthException userRecoverableException) {
                runOnUiThread(() -> recoverException(userRecoverableException));
            } catch (GoogleAuthException | IOException e) {
                e.printStackTrace();
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private void recoverException(UserRecoverableAuthException ex) {
        if (ex instanceof GooglePlayServicesAvailabilityException) {
            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
            int code = googleApiAvailability.isGooglePlayServicesAvailable(this);
            if (code != ConnectionResult.SUCCESS) {
                int requestCode = addActivityResultListener((resultCode, data) -> {
                    if (resultCode == Activity.RESULT_OK)
                        accountSelection();
                    else {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
                Dialog dialog = googleApiAvailability.getErrorDialog(this, code, requestCode);
                dialog.show();
            }
        } else if (ex != null) {
            Intent intent = ex.getIntent();
            startActivityForResult(intent, (resultCode, data) -> {
                if (resultCode == Activity.RESULT_OK)
                    accountSelection();
                else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
        }
    }
}

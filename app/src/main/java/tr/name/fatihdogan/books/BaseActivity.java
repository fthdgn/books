package tr.name.fatihdogan.books;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;

import tr.name.fatihdogan.books.callback.ResultCodeListener;
import tr.name.fatihdogan.books.callback.TwoObjectListener;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    private static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;
    private static final int REQUEST_CODE_PERMISSION_FROM_PLAY_SERVICES_ERROR = 1002;

    private static TwoObjectListener<BaseActivity, Account> accountPickerListener;
    private static ResultCodeListener<BaseActivity> googlePlayServiceDialogListener;
    private static ResultCodeListener<BaseActivity> googlePlayServicePermissionListener;

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_ACCOUNT && accountPickerListener != null) {
            if (resultCode == RESULT_OK) {
                accountPickerListener.onResponse(this, new Account(
                        data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME),
                        data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)));
            } else if (resultCode == RESULT_CANCELED) {
                accountPickerListener.onResponse(this, null);
            }
            accountPickerListener = null;
        } else if (requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR && googlePlayServiceDialogListener != null) {
            if (resultCode == RESULT_OK)
                googlePlayServiceDialogListener.onResponse(this, ResultCodeListener.OK);
            else
                googlePlayServiceDialogListener.onResponse(this, ResultCodeListener.CANCEL);
            googlePlayServiceDialogListener = null;
        } else if (requestCode == REQUEST_CODE_PERMISSION_FROM_PLAY_SERVICES_ERROR && googlePlayServicePermissionListener != null) {
            if (resultCode == RESULT_OK)
                googlePlayServicePermissionListener.onResponse(this, ResultCodeListener.OK);
            else
                googlePlayServicePermissionListener.onResponse(this, ResultCodeListener.CANCEL);
            googlePlayServiceDialogListener = null;
        }
    }

    //region Google Play Services login
    public void showGooglePlayServicePermission(Intent intent, ResultCodeListener<BaseActivity> listener) {
        if (googlePlayServicePermissionListener != null) {
            return;
        }

        googlePlayServicePermissionListener = listener;

        startActivityForResult(intent, REQUEST_CODE_PERMISSION_FROM_PLAY_SERVICES_ERROR);
    }

    public void showGooglePlayServiceDialog(GoogleApiAvailability googleApiAvailability, int result, ResultCodeListener<BaseActivity> listener) {
        if (googlePlayServiceDialogListener != null) {
            return;
        }
        googlePlayServiceDialogListener = listener;

        Dialog dialog = googleApiAvailability.getErrorDialog(this, result, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
        dialog.show();
    }

    public void pickAccount(TwoObjectListener<BaseActivity, Account> listener) {
        if (accountPickerListener != null) {
            return;
        }

        accountPickerListener = listener;

        Log.i("ACCOUNT", "Account picking.");
        String[] accountTypes = new String[]{"com.google"};
        Intent intent;

        if (Build.VERSION.SDK_INT < 23) {
            //noinspection deprecation
            intent = AccountManager.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
        } else {
            intent = AccountManager.newChooseAccountIntent(null, null, accountTypes, null, null, null, null);
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }
    //endregion
}

package tr.name.fatihdogan.books.sync;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.os.Bundle;

import tr.name.fatihdogan.books.R;
import tr.name.fatihdogan.books.activity.BaseActivity;

public class LoginActivity extends BaseActivity {

    private AccountManager mAccountManager;
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        onCreateAccountAuthenticator();

        mAccountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);

        final Account account = new Account("hülo", ACCOUNT_TYPE);
        mAccountManager.addAccountExplicitly(account, "hülo", null);

        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        setAccountAuthenticatorResult(bundle);
        finish();

    }

    @Override
    public void finish() {
        finishAccountAuthenticator();
        super.finish();
    }

    //region AccountAuthenticatorActivity
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;

    /**
     * @see AccountAuthenticatorActivity#setAccountAuthenticatorResult(Bundle)
     */
    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    /**
     * @see AccountAuthenticatorActivity#onCreate(Bundle)
     */
    private void onCreateAccountAuthenticator() {
        mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    /**
     * @see AccountAuthenticatorActivity#finish()
     */
    private void finishAccountAuthenticator() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
    }
    //endregion

}

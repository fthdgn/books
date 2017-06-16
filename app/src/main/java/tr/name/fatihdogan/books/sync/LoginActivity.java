package tr.name.fatihdogan.books.sync;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;

import tr.name.fatihdogan.books.R;

public class LoginActivity extends AccountAuthenticatorActivity {

    private AccountManager mAccountManager;
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAccountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);

        final Account account = new Account("hülo", ACCOUNT_TYPE);
        mAccountManager.addAccountExplicitly(account, "hülo", null);

        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        setAccountAuthenticatorResult(bundle);
        finish();

    }
}

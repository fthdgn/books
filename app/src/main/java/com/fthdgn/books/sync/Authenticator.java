package com.fthdgn.books.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fthdgn.books.utils.LogUtils;

import static android.accounts.AccountManager.KEY_ERROR_CODE;
import static android.accounts.AccountManager.KEY_ERROR_MESSAGE;

public class Authenticator extends AbstractAccountAuthenticator {

    public static final String ACCOUNT_TYPE = "example.com";

    private AccountManager mAccountManager;
    private Context mContext;

    // Simple constructor
    public Authenticator(Context context) {
        super(context);
        mContext = context;
        mAccountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

    }

    // Editing properties is not supported
    @Override
    public Bundle editProperties(AccountAuthenticatorResponse r, String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response,
                             String accountType,
                             String authTokenType,
                             String[] requiredFeatures,
                             Bundle options) throws NetworkErrorException {
        LogUtils.i("ACCOUNT", "ADD");
        @SuppressLint("MissingPermission")
        Account[] accounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);
        Bundle bundle = new Bundle();
        if (accounts.length == 1) {
            LogUtils.i("ACCOUNT", "RE" + accounts[0].name);
            //Do not add new account if already one exist
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, accounts[0].name);
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, accounts[0].type);
        } else {
            LogUtils.i("ACCOUNT", "INT");
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        }
        return bundle;
    }

    // Ignore attempts to confirm credentials
    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse r, Account account, Bundle bundle) throws NetworkErrorException {
        LogUtils.i("ACCOUNT", "confirmCredentials");

        return null;
    }

    // Getting an authentication token is not supported
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse r, Account account, String s, Bundle bundle) throws NetworkErrorException {
        LogUtils.i("ACCOUNT", "getAuthToken");

        Bundle bundle1 = new Bundle();
        bundle1.putInt(KEY_ERROR_CODE, 1);
        bundle1.putString(KEY_ERROR_MESSAGE, "DSDA");
        return bundle1;
    }

    // Getting a label for the auth token is not supported
    @Override
    public String getAuthTokenLabel(String s) {
        LogUtils.i("ACCOUNT", "getAuthTokenLabel");

        throw new UnsupportedOperationException();
    }

    // Updating user credentials is not supported
    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse r, Account account, String s, Bundle bundle) throws NetworkErrorException {
        LogUtils.i("ACCOUNT", "updateCredentials");

        throw new UnsupportedOperationException();
    }

    // Checking features for the account is not supported
    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse r, Account account, String[] strings) throws NetworkErrorException {
        LogUtils.i("ACCOUNT", "hasFeatures");

        throw new UnsupportedOperationException();
    }
}
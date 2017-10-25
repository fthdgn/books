package com.fthdgn.books.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.fthdgn.books.utils.LogUtils;

/**
 * Created by fatih on 15/06/17.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    Context mContext;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        mContext = context;
    }

    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        AccountManager mAccountManager = (AccountManager) mContext.getSystemService(Context.ACCOUNT_SERVICE);
        AccountManagerFuture<Bundle> token = mAccountManager.getAuthToken(account, " example.com", null, true, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> accountManagerFuture) {

            }
        }, null);

        LogUtils.i("SYNC", "ONPERFORM");
    }
}

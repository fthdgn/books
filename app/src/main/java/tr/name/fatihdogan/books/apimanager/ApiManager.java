package tr.name.fatihdogan.books.apimanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.util.Arrays;

import tr.name.fatihdogan.books.Constants;
import tr.name.fatihdogan.books.activity.BaseActivity;
import tr.name.fatihdogan.books.callback.SimpleListener;
import tr.name.fatihdogan.books.repository.AppDatabase;
import tr.name.fatihdogan.books.repository.Book;
import tr.name.fatihdogan.books.utils.ThreadUtils;
import tr.name.fatihdogan.googlebooksapi.BooksApi;
import tr.name.fatihdogan.googlebooksapi.BooksApiManager;
import tr.name.fatihdogan.googlebooksapi.NotInitializedException;
import tr.name.fatihdogan.googlebooksapi.Parameters;
import tr.name.fatihdogan.googlebooksapi.constants.ParameterConstants;
import tr.name.fatihdogan.googlebooksapi.output.VolumeOutput;

public class ApiManager {

    private static final ApiManager mInstance;

    private Account account;

    static {
        mInstance = new ApiManager();
    }

    public static ApiManager getInstance() {
        return mInstance;
    }

    public static void sync(final BaseActivity activity, final SimpleListener callback) {
        if (getInstance().getAccount(activity) == null) {
            Log.i("ACCOUNT", "Account picking.");
            String[] accountTypes = new String[]{"com.google"};
            Intent intent;

            if (Build.VERSION.SDK_INT < 23) {
                //noinspection deprecation
                intent = AccountManager.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
            } else {
                intent = AccountManager.newChooseAccountIntent(null, null, accountTypes, null, null, null, null);
            }

            activity.startActivityForResult(intent, (resultCode, data) -> {
                if (resultCode == Activity.RESULT_OK) {
                    Account account = new Account(
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME),
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

                    getInstance().setAccount(account, activity);
                    sync(activity, callback);
                }
            });
            return;
        }

        TokenRetriever tokenRetriever = new TokenRetriever(activity, getInstance().account, token -> {
            BooksApiManager.getInstance().setAuthToken(token);
            //TODO Implement paging
            BooksApi.MyLibrary.Bookshelves.Volumes.list(
                    new Parameters.MyLibrary.Bookshelves.Volumes.List("7").maxResults(100).projection(ParameterConstants.LITE),
                    (response, e) -> {
                        if (e instanceof NotInitializedException) {
                            BooksApiManager.initialize(activity);
                        }
                        String authToken = BooksApiManager.getInstance().getAuthToken();

                        for (final VolumeOutput volumeOutput : (response != null && response.items != null) ? response.items : new
                                VolumeOutput[0]) {
                            String url = "https://books.google.com/books/content?id=" + volumeOutput.id +
                                    "&printsec=frontcover&img=1&zoom=1&source=gbs_api&w=320&access_token=" + authToken;

                            FileDownloadRequest fileDownloadRequest = new FileDownloadRequest(
                                    url,
                                    Constants.localCoverPath + File.separator + volumeOutput.id,
                                    () -> {
                                        final Book book = new Book();
                                        book.setBookId(volumeOutput.id);
                                        book.setTitle(volumeOutput.volumeInfo.title);
                                        book.setSortTitle(book.getTitle());
                                        book.setOriginalTitle(book.getTitle());
                                        book.setAuthors(Arrays.asList(volumeOutput.volumeInfo.authors));
                                        book.setOriginalAuthors(book.getAuthors());
                                        ThreadUtils.runOnBackground(() -> {
                                            //TODO Don't override edited fields
                                            AppDatabase.getBookDao().insertAll(book);
                                        });
                                    });
                            FileDownloadRequest.addRequest(fileDownloadRequest);
                        }
                        callback.onResponse();
                    });
        }, new TokenRetriever.OnErrorListener() {
            @Override
            public void onRecoverableException(final Exception e) {
                activity.runOnUiThread(() -> {
                    if (e instanceof GooglePlayServicesAvailabilityException) {
                        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                        int code = googleApiAvailability.isGooglePlayServicesAvailable(activity);
                        if (code != ConnectionResult.SUCCESS) {
                            int requestCode = activity.addActivityResultListener((resultCode, data) -> {
                                if (resultCode == Activity.RESULT_OK)
                                    sync(activity, callback);
                            });
                            Dialog dialog = googleApiAvailability.getErrorDialog(activity, code, requestCode);
                            dialog.show();
                        }
                    } else if (e instanceof UserRecoverableAuthException) {
                        Intent intent = ((UserRecoverableAuthException) e).getIntent();
                        activity.startActivityForResult(intent, (resultCode, data) -> {
                            if (resultCode == Activity.RESULT_OK)
                                sync(activity, callback);
                        });
                    }
                });
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        });
        tokenRetriever.execute();
    }

    public boolean isLoggedIn(Activity activity) {
        return getAccount(activity) != null;
    }

    public void setAccount(Account account, Activity activity) {
        this.account = account;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (account != null) {
            editor.putString("mail", account.name);
            editor.putString("type", account.type);
        } else {
            editor.remove("mail");
            editor.remove("type");
        }
        editor.apply();
    }

    @Nullable
    private Account getAccount(Activity activity) {
        if (account == null) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences("MyPref", 0);
            String mail = sharedPreferences.getString("mail", null);
            String type = sharedPreferences.getString("type", null);
            if (mail == null || type == null)
                return null;

            account = new Account(mail, type);
        }

        return account;
    }
}

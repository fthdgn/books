package tr.name.fatihdogan.books.apimanager;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import tr.name.fatihdogan.books.BaseActivity;
import tr.name.fatihdogan.books.callback.ResultCodeListener;
import tr.name.fatihdogan.books.callback.SimpleListener;
import tr.name.fatihdogan.books.callback.TwoObjectListener;
import tr.name.fatihdogan.books.data.Book;
import tr.name.fatihdogan.googlebooksapi.BooksApi;
import tr.name.fatihdogan.googlebooksapi.BooksApiManager;
import tr.name.fatihdogan.googlebooksapi.NotInitializedException;
import tr.name.fatihdogan.googlebooksapi.Parameters;
import tr.name.fatihdogan.googlebooksapi.constants.ParameterConstants;
import tr.name.fatihdogan.googlebooksapi.listener.Listener;
import tr.name.fatihdogan.googlebooksapi.output.VolumeListOutput;
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
            activity.pickAccount(new TwoObjectListener<BaseActivity, Account>() {

                @Override
                public void onResponse(BaseActivity object, Account object2) {
                    if (object2 != null) {
                        getInstance().setAccount(object2, activity);
                        sync(activity, callback);
                    }
                }
            });
            return;
        }

        TokenRetriever tokenRetriever = new TokenRetriever(activity, getInstance().account, new TokenRetriever.OnSuccessListener() {
            @Override
            public void onSuccess(String token) {
                BooksApiManager.getInstance().setAuthToken(token);
                //TODO Implement paging
                BooksApi.MyLibrary.Bookshelves.Volumes.list(
                        new Parameters.MyLibrary.Bookshelves.Volumes.List("7").maxResults(100).projection(ParameterConstants.LITE),
                        new Listener<VolumeListOutput>() {
                            @Override
                            public void onResponse(@Nullable VolumeListOutput response, @Nullable Exception e) {
                                if (e instanceof NotInitializedException) {
                                    BooksApiManager.initialize(activity);
                                }

                                for (VolumeOutput volumeOutput : response != null ? response.items : new VolumeOutput[0]) {
                                    Book.createBook(volumeOutput);
                                }
                                callback.onResponse();
                            }
                        });

            }
        }, new TokenRetriever.OnErrorListener() {
            @Override
            public void onRecoverableException(final Exception e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e instanceof GooglePlayServicesAvailabilityException) {
                            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                            int code = googleApiAvailability.isGooglePlayServicesAvailable(activity);
                            if (code != ConnectionResult.SUCCESS) {
                                activity.showGooglePlayServiceDialog(googleApiAvailability, code, new ResultCodeListener<BaseActivity>() {
                                    @Override
                                    public void onResponse(BaseActivity object, @Result int resultCode) {
                                        if (resultCode == OK)
                                            sync(activity, callback);
                                    }
                                });
                            }
                        } else if (e instanceof UserRecoverableAuthException) {
                            Intent intent = ((UserRecoverableAuthException) e).getIntent();
                            activity.showGooglePlayServicePermission(intent, new ResultCodeListener<BaseActivity>() {
                                public void onResponse(BaseActivity object, @Result int resultCode) {
                                    if (resultCode == OK)
                                        sync(activity, callback);
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
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

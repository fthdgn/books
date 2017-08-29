package tr.name.fatihdogan.books.api;

import android.accounts.Account;
import android.content.SharedPreferences;

public class AccountManager {

    private final SharedPreferences sharedPreferences;

    public AccountManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Account getAccount() {
        String mail = sharedPreferences.getString("mail", null);
        String type = sharedPreferences.getString("type", null);
        if (mail == null || type == null)
            return null;

        return new Account(mail, type);
    }

    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    public void setAccount(Account account, String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (account != null && token != null) {
            editor.putString("mail", account.name);
            editor.putString("type", account.type);
            editor.putString("token", token);
        } else {
            editor.remove("mail");
            editor.remove("type");
            editor.remove("token");
        }
        editor.apply();
    }

    public void removeAccount() {
        setAccount(null, null);
    }
}

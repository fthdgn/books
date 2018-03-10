package com.fthdgn.books.api

import android.accounts.Account
import android.content.SharedPreferences

class AccountManager(private val sharedPreferences: SharedPreferences) {

    val account: Account?
        get() {
            val mail = sharedPreferences.getString("mail", null)
            val type = sharedPreferences.getString("type", null)
            return if (mail == null || type == null) null else Account(mail, type)
        }

    val token: String?
        get() = sharedPreferences.getString("token", null)

    fun setAccount(account: Account?, token: String?) {
        val editor = sharedPreferences.edit()
        if (account != null && token != null) {
            editor.putString("mail", account.name)
            editor.putString("type", account.type)
            editor.putString("token", token)
        } else {
            editor.remove("mail")
            editor.remove("type")
            editor.remove("token")
        }
        editor.apply()
    }

    fun removeAccount() {
        setAccount(null, null)
    }
}

package com.fthdgn.books.activity

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.fthdgn.books.BaseApplication
import com.fthdgn.books.utils.runBackground
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.io.IOException

class LoginActivity : BaseActivity() {

    private val accountManager: com.fthdgn.books.api.AccountManager = BaseApplication.getAppComponent().accountManager()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountSelection()
    }

    private fun accountSelection() {
        val account = accountManager.account
        if (account == null) {
            Log.i("ACCOUNT", "Account picking.")
            val accountTypes = arrayOf("com.google")
            val intent: Intent

            intent = if (Build.VERSION.SDK_INT < 23) {
                @Suppress("DEPRECATION")
                AccountManager.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null)
            } else {
                AccountManager.newChooseAccountIntent(null, null, accountTypes, null, null, null, null)
            }

            startActivityForResult(intent) { resultCode, data ->
                if (resultCode == Activity.RESULT_OK) {
                    val newAccount = Account(
                            data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME),
                            data?.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE))
                    retrieveToken(newAccount)
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
        } else {
            retrieveToken(account)
        }
    }

    private fun retrieveToken(account: Account) {
        runBackground {
            try {
                val token = GoogleAuthUtil.getToken(this@LoginActivity, account, "oauth2:https://www.googleapis.com/auth/books")
                accountManager.setAccount(account, token)
                setResult(Activity.RESULT_OK)
                finish()
            } catch (userRecoverableException: UserRecoverableAuthException) {
                runOnUiThread { recoverException(userRecoverableException) }
            } catch (e: GoogleAuthException) {
                e.printStackTrace()
                setResult(Activity.RESULT_CANCELED)
                finish()
            } catch (e: IOException) {
                e.printStackTrace()
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }

    private fun recoverException(ex: UserRecoverableAuthException?) {
        if (ex is GooglePlayServicesAvailabilityException) {
            val googleApiAvailability = GoogleApiAvailability.getInstance()
            val code = googleApiAvailability.isGooglePlayServicesAvailable(this)
            if (code != ConnectionResult.SUCCESS) {
                val requestCode = addActivityResultListener { resultCode, _ ->
                    if (resultCode == Activity.RESULT_OK)
                        accountSelection()
                    else {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                }
                val dialog = googleApiAvailability.getErrorDialog(this, code, requestCode)
                dialog.show()
            }
        } else if (ex != null) {
            val intent = ex.intent
            startActivityForResult(intent) { resultCode, _ ->
                if (resultCode == Activity.RESULT_OK)
                    accountSelection()
                else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
        }
    }
}

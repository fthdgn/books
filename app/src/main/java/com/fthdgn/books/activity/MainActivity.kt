package com.fthdgn.books.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.Menu
import android.view.MenuItem
import com.fthdgn.books.BaseApplication
import com.fthdgn.books.R
import com.fthdgn.books.api.AccountManager
import com.fthdgn.books.fragment.AuthorsFragment
import com.fthdgn.books.fragment.BooksFragment
import com.fthdgn.books.repository.BookDao
import com.fthdgn.books.service.SyncService
import com.fthdgn.books.utils.runBackground
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val accountManager: AccountManager = BaseApplication.getAppComponent().accountManager()
    private val bookDao: BookDao = BaseApplication.getAppComponent().bookDao()
    private val fragmentManager: FragmentManager = supportFragmentManager

    //region Fragments
    private lateinit var allBooksFragment: Fragment
    private lateinit var authorsFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //region Prepare bottom navigation_main view
        val navigation = findViewById<BottomNavigationViewEx>(R.id.navigation)
        navigation.setTextVisibility(true)
        navigation.enableItemShiftingMode(false)
        navigation.enableShiftingMode(false)
        navigation.onNavigationItemSelectedListener = this
        //endregion

        if (savedInstanceState == null) {
            val transaction = fragmentManager.beginTransaction()
            authorsFragment = AuthorsFragment.newInstance()
            allBooksFragment = BooksFragment.allBooksInstance()
            transaction.add(R.id.container, authorsFragment, "authorsFragment")
            transaction.add(R.id.container, allBooksFragment, "allBooksFragment")
            transaction.show(allBooksFragment)
            transaction.hide(authorsFragment)
            transaction.commit()
        } else {
            authorsFragment = fragmentManager.findFragmentByTag("authorsFragment")
            allBooksFragment = fragmentManager.findFragmentByTag("allBooksFragment")
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val s = intent.getStringExtra("AUTHOR")
        if (s != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, BooksFragment.authorBooksInstance(s))
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun showAllBooksFragment() {
        showMainFragment(allBooksFragment)
    }

    private fun showAuthorsFragment() {
        showMainFragment(authorsFragment)
    }

    @Synchronized private fun showMainFragment(fragment: Fragment?) {
        fragmentManager.popBackStack()
        val transaction = fragmentManager.beginTransaction()
        if (fragment !== authorsFragment)
            transaction.hide(authorsFragment)
        if (fragment !== allBooksFragment)
            transaction.hide(allBooksFragment)
        transaction.show(fragment)
        transaction.commit()
    }
    //endregion

    //region OptionsMenu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val login = menu.findItem(R.id.login_button)
        val logout = menu.findItem(R.id.logout_button)
        val sync = menu.findItem(R.id.sync_button)
        val loggedin = accountManager.account != null

        login.isVisible = !loggedin
        sync.isVisible = loggedin
        logout.isVisible = loggedin

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.sync_button -> {
                startService(Intent(this, SyncService::class.java))
                return true
            }
            R.id.login_button -> {
                startActivityForResult(Intent(this, LoginActivity::class.java)) { resultCode, _ ->
                    if (resultCode == Activity.RESULT_OK) {
                        startService(Intent(this, SyncService::class.java))
                        invalidateOptionsMenu()
                    }
                }
                return true
            }
            R.id.logout_button -> {
                runBackground {
                    accountManager.removeAccount()
                    bookDao.deleteAll()
                    invalidateOptionsMenu()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    //endregion

    //region BottomNavigationView.OnNavigationItemSelectedListener
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_all -> {
                showAllBooksFragment()
                return true
            }
            R.id.navigation_authors -> {
                showAuthorsFragment()
                return true
            }
            R.id.navigation_bookshelves ->
                //TODO Implement bookshelves
                return false
        }
        return false
    }
    //endregion
}

package com.fthdgn.books.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import com.fthdgn.books.BaseApplication;
import com.fthdgn.books.R;
import com.fthdgn.books.api.AccountManager;
import com.fthdgn.books.fragment.AuthorsFragment;
import com.fthdgn.books.fragment.BooksFragment;
import com.fthdgn.books.repository.BookDao;
import com.fthdgn.books.service.SyncService;
import com.fthdgn.books.utils.ThreadUtils;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private AccountManager accountManager;

    private BookDao bookDao;

    private FragmentManager fragmentManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = BaseApplication.getAppComponent().accountManager();
        bookDao = BaseApplication.getAppComponent().bookDao();
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        //region Prepare bottom navigation_main view
        BottomNavigationViewEx navigation = findViewById(R.id.navigation);
        navigation.setTextVisibility(true);
        navigation.enableItemShiftingMode(false);
        navigation.enableShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(this);
        //endregion

        if (savedInstanceState == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            authorsFragment = AuthorsFragment.newInstance();
            allBooksFragment = BooksFragment.allBooksInstance();
            transaction.add(R.id.container, authorsFragment, "authorsFragment");
            transaction.add(R.id.container, allBooksFragment, "allBooksFragment");
            transaction.show(allBooksFragment);
            transaction.hide(authorsFragment);
            transaction.commit();
        } else {
            authorsFragment = fragmentManager.findFragmentByTag("authorsFragment");
            allBooksFragment = fragmentManager.findFragmentByTag("allBooksFragment");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String s = intent.getStringExtra("AUTHOR");
        if (s != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, BooksFragment.authorBooksInstance(s));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    //region Fragments
    private Fragment allBooksFragment;
    private Fragment authorsFragment;

    private void showAllBooksFragment() {
        showMainFragment(allBooksFragment);
    }

    private void showAuthorsFragment() {
        showMainFragment(authorsFragment);
    }

    private synchronized void showMainFragment(Fragment fragment) {
        fragmentManager.popBackStack();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment != authorsFragment)
            transaction.hide(authorsFragment);
        if (fragment != allBooksFragment)
            transaction.hide(allBooksFragment);
        transaction.show(fragment);
        transaction.commit();
    }
    //endregion

    //region OptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem login = menu.findItem(R.id.login_button);
        MenuItem logout = menu.findItem(R.id.logout_button);
        MenuItem sync = menu.findItem(R.id.sync_button);
        boolean loggedin = accountManager.getAccount() != null;

        login.setVisible(!loggedin);
        sync.setVisible(loggedin);
        logout.setVisible(loggedin);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sync_button) {
            startService(new Intent(this, SyncService.class));
            return true;
        } else if (id == R.id.login_button) {
            startActivityForResult(new Intent(this, LoginActivity.class), (resultCode, data) -> {
                if (resultCode == RESULT_OK) {
                    startService(new Intent(this, SyncService.class));
                    invalidateOptionsMenu();
                }
            });
            return true;
        } else if (id == R.id.logout_button) {
            ThreadUtils.runOnBackground(() -> {
                accountManager.removeAccount();
                bookDao.deleteAll();
                invalidateOptionsMenu();
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region BottomNavigationView.OnNavigationItemSelectedListener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_all:
                showAllBooksFragment();
                return true;
            case R.id.navigation_authors:
                showAuthorsFragment();
                return true;
            case R.id.navigation_bookshelves:
                //TODO Implement bookshelves
                return false;
        }
        return false;
    }
    //endregion
}

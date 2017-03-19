package tr.name.fatihdogan.books.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import tr.name.fatihdogan.books.R;
import tr.name.fatihdogan.books.apimanager.ApiManager;
import tr.name.fatihdogan.books.callback.SimpleListener;
import tr.name.fatihdogan.books.data.Book;
import tr.name.fatihdogan.books.fragment.AuthorsFragment;
import tr.name.fatihdogan.books.fragment.BooksFragment;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();

        //region Prepare bottom navigation_main view
        BottomNavigationViewEx navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
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
        boolean loggedin = ApiManager.getInstance().isLoggedIn(this);

        login.setVisible(!loggedin);
        sync.setVisible(loggedin);
        logout.setVisible(loggedin);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sync_button) {
            ApiManager.sync(this, new SimpleListener() {
                @Override
                public void onResponse() {
                    invalidateOptionsMenu();
                }
            });
            return true;
        } else if (id == R.id.login_button) {
            ApiManager.sync(this, new SimpleListener() {
                @Override
                public void onResponse() {
                    invalidateOptionsMenu();
                }
            });
            return true;
        } else if (id == R.id.logout_button) {
            Book.clear();
            ApiManager.getInstance().setAccount(null, this);
            invalidateOptionsMenu();
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

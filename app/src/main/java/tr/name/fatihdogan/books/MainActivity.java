package tr.name.fatihdogan.books;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import tr.name.fatihdogan.books.apimanager.ApiManager;
import tr.name.fatihdogan.books.callback.SimpleListener;
import tr.name.fatihdogan.books.data.Book;
import tr.name.fatihdogan.books.fragment.AuthorsFragment;
import tr.name.fatihdogan.books.fragment.BooksFragment;
import tr.name.fatihdogan.books.utils.LogUtils;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;

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

        showAllBooksFragment();
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
    BooksFragment allBooksFragment;
    AuthorsFragment authorsFragment;

    private void showAllBooksFragment() {
        if (allBooksFragment == null) {
            allBooksFragment = BooksFragment.allBooksInstance();
            showFragment(allBooksFragment, false);
        } else {
            showFragment(allBooksFragment, true);
        }
    }

    private void showAuthorsFragment() {
        if (authorsFragment == null) {
            authorsFragment = AuthorsFragment.newInstance();
            showFragment(authorsFragment, false);
        } else {
            showFragment(authorsFragment, true);
        }
    }

    private void showFragment(Fragment fragment, boolean attach) {
        Fragment old = fragmentManager.findFragmentById(R.id.container);
        if (old == fragment)
            return;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (old != null)
            fragmentTransaction.detach(old);

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (attach)
            fragmentTransaction.attach(fragment);
        else
            fragmentTransaction.add(R.id.container, fragment);

        fragmentTransaction.commit();
    }
    //endregion

    //region OptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LogUtils.logCodeLocation("Main Activity onCreateOptionsMenu Start");
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem login = menu.findItem(R.id.login_button);
        MenuItem logout = menu.findItem(R.id.logout_button);
        MenuItem sync = menu.findItem(R.id.sync_button);
        boolean loggedin = ApiManager.getInstance().isLoggedIn(this);

        login.setVisible(!loggedin);
        sync.setVisible(loggedin);
        logout.setVisible(loggedin);

        LogUtils.logCodeLocation("Main Activity onCreateOptionsMenu End");
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

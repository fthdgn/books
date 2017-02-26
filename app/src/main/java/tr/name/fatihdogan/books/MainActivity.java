package tr.name.fatihdogan.books;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import tr.name.fatihdogan.books.apimanager.ApiManager;
import tr.name.fatihdogan.books.callback.SimpleListener;
import tr.name.fatihdogan.books.data.Book;
import tr.name.fatihdogan.books.utils.LogUtils;

public class MainActivity extends BaseActivity {

    public static MainActivity mActivity;

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    BooksAdapter booksAdapter;
    Button sycncButton;

    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.logCodeLocation("Main Activity onCreate Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BookLoad().execute();
        mActivity = this;
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.column));
        recyclerView.setLayoutManager(gridLayoutManager);
        booksAdapter = new BooksAdapter();
        recyclerView.setAdapter(booksAdapter);
        sycncButton = (Button) findViewById(R.id.syncButton);
        sycncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiManager.sync(mActivity, new SimpleListener() {
                    @Override
                    public void onResponse() {
                        booksAdapter.refresh();
                        invalidateOptionsMenu();
                    }
                });
            }
        });
        LogUtils.logCodeLocation("Main Activity onCreate End");

    }

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

        if (!loggedin)
            sycncButton.setVisibility(View.VISIBLE);
        else
            sycncButton.setVisibility(View.GONE);

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
                    booksAdapter.refresh();
                    invalidateOptionsMenu();
                }
            });
            return true;
        } else if (id == R.id.login_button) {
            ApiManager.sync(this, new SimpleListener() {
                @Override
                public void onResponse() {
                    booksAdapter.refresh();
                    invalidateOptionsMenu();
                }
            });
            return true;
        } else if (id == R.id.logout_button) {
            Book.clear();
            booksAdapter.refresh();
            ApiManager.getInstance().setAccount(null, this);
            invalidateOptionsMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class BookLoad extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Book.loadBooks();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            booksAdapter.refresh();
        }
    }

}

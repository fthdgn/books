package tr.name.fatihdogan.books.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import tr.name.fatihdogan.books.BaseApplication;
import tr.name.fatihdogan.books.activity.MainActivity;
import tr.name.fatihdogan.books.data.Book;

public class AuthorsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AuthorsAdapter authorsAdapter;
    private final List<String> authors = new ArrayList<>();

    private final BroadcastReceiver bookRefreshEvent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };

    public static AuthorsFragment newInstance() {
        Bundle args = new Bundle();
        AuthorsFragment fragment = new AuthorsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = new RecyclerView(getActivity());
        //noinspection ResourceType
        recyclerView.setId(10);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        authorsAdapter = new AuthorsAdapter();
        recyclerView.setAdapter(authorsAdapter);

        refresh();
        LocalBroadcastManager.getInstance(BaseApplication.getInstance()).registerReceiver(bookRefreshEvent,
                new IntentFilter(Book.BOOKS_CHANGED_EVENT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return recyclerView;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(BaseApplication.getInstance()).unregisterReceiver(bookRefreshEvent);
        super.onDestroy();
    }

    private void refresh() {
        SortedSet<String> authorsSet = new TreeSet<>();
        Collection<Book> all_books = Book.BOOKS.values();
        for (Book b : all_books) {
            String[] authors = b.getAuthors();
            if (authors != null)
                Collections.addAll(authorsSet, authors);
        }
        authors.clear();
        authors.addAll(authorsSet);
        Collections.sort(authors);
        authorsAdapter.notifyDataSetChanged();
    }

    private class AuthorsViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        AuthorsViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof TextView) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("AUTHOR", ((TextView) v).getText());
                v.getContext().startActivity(intent);
            }
        }
    };

    private class AuthorsAdapter extends RecyclerView.Adapter<AuthorsViewHolder> {

        @Override
        public AuthorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setTextSize(20);
            textView.setOnClickListener(onClickListener);
            return new AuthorsViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(AuthorsViewHolder holder, int position) {
            holder.textView.setText(authors.get(position));
        }

        @Override
        public int getItemCount() {
            return authors.size();
        }
    }

}

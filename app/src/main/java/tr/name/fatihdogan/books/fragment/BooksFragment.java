package tr.name.fatihdogan.books.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import tr.name.fatihdogan.books.BaseApplication;
import tr.name.fatihdogan.books.BookView;
import tr.name.fatihdogan.books.R;
import tr.name.fatihdogan.books.data.Book;
import tr.name.fatihdogan.books.utils.StringUtils;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class BooksFragment extends Fragment {

    private static final int NONE = 0;
    private static final int ALL = 1;
    private static final int AUTHORS = 2;
    private static final int BOOKSHELF = 3;

    @Retention(SOURCE)
    @IntDef({
            NONE,
            ALL,
            AUTHORS,
            BOOKSHELF
    })
    @interface Filter {

    }

    @Filter
    private int filterType;
    private String filterTerm;

    private RecyclerView recyclerView;
    private BooksAdapter booksAdapter;
    private final ArrayList<Book> books = new ArrayList<>();
    private final Comparator<Book> titleComparator = new Comparator<Book>() {
        @Override
        public int compare(Book o1, Book o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    };

    private final BroadcastReceiver bookRefreshEvent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };

    public static BooksFragment allBooksInstance() {
        return newInstance(ALL, null);
    }

    public static BooksFragment authorBooksInstance(@NonNull String authorName) {
        return newInstance(AUTHORS, authorName);
    }

    private static BooksFragment newInstance(@Filter int filterType, @Nullable String filterTerm) {
        Bundle args = new Bundle();
        args.putInt("filterType", filterType);
        args.putString("filterTerm", filterTerm);
        BooksFragment fragment = new BooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filterTerm = getArguments().getString("filterTerm", "");
            //noinspection WrongConstant
            filterType = getArguments().getInt("filterType", NONE);
        } else {
            filterType = NONE;
        }

        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getActivity().getResources().getInteger(R.integer.column)));
        booksAdapter = new BooksAdapter();
        recyclerView.setAdapter(booksAdapter);

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
        books.clear();
        Collection<Book> all_books = Book.BOOKS.values();
        for (Book b : all_books) {
            if (filter(b))
                books.add(b);
        }
        Collections.sort(books, titleComparator);
        booksAdapter.notifyDataSetChanged();
    }

    private boolean filter(Book book) {
        switch (filterType) {
            case NONE:
                return false;
            case ALL:
                return true;
            case AUTHORS:
                return (StringUtils.contains(filterTerm, book.getAuthors()));
            case BOOKSHELF:
                //TODO Implement
                return false;
            default:
                return false;
        }
    }

    private class BookViewHolder extends RecyclerView.ViewHolder {

        private final BookView bookView;

        BookViewHolder(BookView v) {
            super(v);
            bookView = v;
        }
    }

    private class BooksAdapter extends RecyclerView.Adapter<BookViewHolder> {

        @Override
        public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BookView bookView = new BookView(parent.getContext());
            return new BookViewHolder(bookView);
        }

        @Override
        public void onBindViewHolder(BookViewHolder holder, int position) {
            holder.bookView.setBook(books.get(position));
        }

        @Override
        public int getItemCount() {
            return books.size();
        }
    }
}

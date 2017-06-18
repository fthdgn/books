package tr.name.fatihdogan.books.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import tr.name.fatihdogan.books.R;
import tr.name.fatihdogan.books.repository.AppDatabase;
import tr.name.fatihdogan.books.repository.Book;
import tr.name.fatihdogan.books.view.BookView;

public class BooksFragment extends BaseFragment {

    private static final int ALL = 1;
    private static final int AUTHORS = 2;
    private static final int BOOKSHELF = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            ALL,
            AUTHORS,
            BOOKSHELF
    })
    @interface Filter {

    }

    private String filterTerm;

    private RecyclerView recyclerView;
    private BooksAdapter booksAdapter;
    private final ArrayList<Book> books = new ArrayList<>();

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
        @Filter
        int filterType;
        if (getArguments() != null) {
            filterTerm = getArguments().getString("filterTerm", "");
            //noinspection WrongConstant
            filterType = getArguments().getInt("filterType", ALL);
        } else {
            filterType = ALL;
        }

        recyclerView = new RecyclerView(getActivity());
        recyclerView.setId(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getActivity().getResources().getInteger(R.integer.column)));
        booksAdapter = new BooksAdapter();
        recyclerView.setAdapter(booksAdapter);

        LiveData<List<Book>> bookLiveData;
        switch (filterType) {
            case ALL:
                bookLiveData = AppDatabase.getBookDao().getAllSortedLive();
                break;
            case AUTHORS:
                bookLiveData = AppDatabase.getBookDao().getByAuthorLive(filterTerm);
                break;
            case BOOKSHELF:
                //TODO Implement
            default:
                bookLiveData = AppDatabase.getBookDao().getAllSortedLive();
        }

        bookLiveData.observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> newBooks) {
                books.clear();
                if (newBooks != null)
                    books.addAll(newBooks);
                booksAdapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return recyclerView;
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

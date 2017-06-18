package tr.name.fatihdogan.books.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tr.name.fatihdogan.books.R;
import tr.name.fatihdogan.books.repository.AppDatabase;
import tr.name.fatihdogan.books.view.AuthorView;

public class AuthorsFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private AuthorsAdapter authorsAdapter;
    private final List<String> authors = new ArrayList<>();

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
        recyclerView.setId(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        authorsAdapter = new AuthorsAdapter();
        recyclerView.setAdapter(authorsAdapter);

        LiveData<List<String>> authorsLiveData = AppDatabase.getBookDao().getAllAuthorsLive();
        authorsLiveData.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                authors.clear();
                if (strings == null) {
                    return;
                } else {
                    authors.addAll(strings);
                }
                authorsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return recyclerView;
    }

    private class AuthorsViewHolder extends RecyclerView.ViewHolder {

        private final AuthorView authorView;

        AuthorsViewHolder(AuthorView v) {
            super(v);
            authorView = v;
        }
    }

    private class AuthorsAdapter extends RecyclerView.Adapter<AuthorsViewHolder> {

        @Override
        public AuthorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AuthorView authorView = new AuthorView(parent.getContext());
            authorView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new AuthorsViewHolder(authorView);
        }

        @Override
        public void onBindViewHolder(AuthorsViewHolder holder, int position) {
            holder.authorView.setName(authors.get(position));
        }

        @Override
        public int getItemCount() {
            return authors.size();
        }
    }

}

package tr.name.fatihdogan.books;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import tr.name.fatihdogan.books.apimanager.FileDownloadRequest;
import tr.name.fatihdogan.books.callback.SimpleListener;
import tr.name.fatihdogan.books.data.Book;
import tr.name.fatihdogan.googlebooksapi.BooksApiManager;

import static tr.name.fatihdogan.books.BaseApplication.getFilesDirPath;

class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private Book[] books;

    void refresh() {
        books = Book.BOOKS.values().toArray(new Book[0]);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private BookView bookView;

        ViewHolder(View v) {
            super(v);
            bookView = (BookView) v.findViewById(R.id.bookView);
            bookView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/books/reader?id=" + v.getTag()));
                    bookView.getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_book, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bookView.setTitle(books[position].getTitle());
        holder.bookView.setAuthor(books[position].getFormatedAuthorNames());

        holder.bookView.setTag(books[position].getId());

        String path = books[position].getCover();
        if (path == null) {
            holder.bookView.setCover(R.drawable.image_not_available);
            String url = "https://books.google.com/books/content?id=" + books[position].getId() + "&printsec=frontcover&img=1&zoom=1&source=gbs_api&w=320&access_token=" +
                    BooksApiManager.getInstance().getAuthToken();

            final int p = position;
            FileDownloadRequest fileDownloadRequest = new FileDownloadRequest(
                    url,
                    getFilesDirPath() + File.separator + "online" + File.separator + "cover" + File.separator + books[position].getId(),
                    new SimpleListener() {
                        @Override
                        public void onResponse() {
                            notifyItemChanged(p);
                        }
                    });
            FileDownloadRequest.addRequest(fileDownloadRequest);
        } else
            holder.bookView.setCover(books[position].getCover());
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.length;
    }

}

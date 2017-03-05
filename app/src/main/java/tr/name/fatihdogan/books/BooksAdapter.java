package tr.name.fatihdogan.books;

import android.support.v7.widget.RecyclerView;
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

        ViewHolder(BookView v) {
            super(v);
            bookView = v;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BookView bookView = new BookView(parent.getContext());
        return new ViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bookView.setTitle(books[position].getTitle());
        holder.bookView.setAuthor(books[position].getFormatedAuthorNames());

        holder.bookView.setBookId(books[position].getId());

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

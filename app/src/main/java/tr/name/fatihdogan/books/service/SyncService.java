package tr.name.fatihdogan.books.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tr.name.fatihdogan.books.BaseApplication;
import tr.name.fatihdogan.books.api.GoogleBooksApi;
import tr.name.fatihdogan.books.api.response.Volume;
import tr.name.fatihdogan.books.api.response.Volumes;
import tr.name.fatihdogan.books.repository.Book;
import tr.name.fatihdogan.books.repository.BookDao;
import tr.name.fatihdogan.books.utils.StringUtils;

public class SyncService extends IntentService {

    private final GoogleBooksApi googleBooksApi;

    private final BookDao bookDao;

    public SyncService() {
        super("SyncService");
        googleBooksApi = BaseApplication.getAppComponent().googleBooksApi();
        bookDao = BaseApplication.getAppComponent().bookDao();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        List<Volume> volumesList = new ArrayList<>();
        int total_count;
        int last_count;
        int last_index = 0;
        do {
            try {
                Volumes volumes = googleBooksApi.getMyVolumes(last_index).execute().body();
                if (volumes == null) {
                    return;
                    //TODO Handle error
                }
                total_count = volumes.totalItems;
                last_count = volumes.items == null ? 0 : volumes.items.size();
                last_index += last_count;
                if (volumes.items != null) {
                    volumesList.addAll(volumes.items);
                }
            } catch (IOException e) {
                //TODO Handle error
                e.printStackTrace();
                return;
            }

        } while (total_count > 0 && last_count > 0);

        if (volumesList.size() == total_count) {
            String[] ids = new String[volumesList.size()];
            for (int i = 0; i < volumesList.size(); i++)
                ids[i] = volumesList.get(i).id;
            bookDao.deleteOtherThanTheseIds(ids);
        }

        Book[] books = new Book[volumesList.size()];

        for (int i = 0; i < volumesList.size(); i++) {
            Volume volume = volumesList.get(i);
            Book book = bookDao.getById(volume.id);
            if (book == null) {
                book = new Book();
                book.setBookId(volume.id);
            }
            book.setOriginalAuthors(Arrays.asList(volume.volumeInfo.authors));
            book.setOriginalTitle(volume.volumeInfo.title);
            if (book.getAuthors() == null || book.getAuthors().size() == 0)
                book.setAuthors(book.getOriginalAuthors());

            if (StringUtils.isEmpty(book.getTitle()))
                book.setTitle(book.getOriginalTitle());

            if (StringUtils.isEmpty(book.getSortTitle()))
                book.setSortTitle(book.getTitle());

            books[i] = book;
        }

        bookDao.insertAll(books);
    }
}

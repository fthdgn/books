package tr.name.fatihdogan.books.repository;

import android.arch.persistence.room.TypeConverters;

import java.util.List;

class AuthorContainer {

    @TypeConverters({AuthorConverter.class})
    private List<String> authors;

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
}

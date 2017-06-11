package tr.name.fatihdogan.books.repository;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Workaround for LiveData/Room bug.
 * <url>https://issuetracker.google.com/issues/62510164</url>
 * <p>
 * If the bug is fixed, delete this class and run androidTest to verify.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Entity
public class Split {

    @PrimaryKey
    int id;

}

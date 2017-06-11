package tr.name.fatihdogan.books.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import tr.name.fatihdogan.books.R;
import tr.name.fatihdogan.books.repository.AppDatabase;
import tr.name.fatihdogan.books.repository.Book;
import tr.name.fatihdogan.books.utils.EditTextUtils;
import tr.name.fatihdogan.books.utils.ThreadUtils;

public class BookEditActivity extends BaseActivity implements View.OnClickListener {

    @SuppressWarnings("FieldCanBeLocal")
    private String bookId;
    private Book book;
    @SuppressWarnings("FieldCanBeLocal")
    private ImageView coverImageView;
    private ImageButton changeCoverButton;

    private EditText titleEditText;
    private EditText sortTitleEditText;
    private EditText authorsEditText;

    private CheckBox titleCheckBox;
    private CheckBox sortTitleCheckBox;
    private CheckBox authorsCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coverImageView = findViewById(R.id.cover_image_view);
        changeCoverButton = findViewById(R.id.change_cover_button);

        titleEditText = findViewById(R.id.title_edit_text);
        sortTitleEditText = findViewById(R.id.sort_title_edit_text);
        authorsEditText = findViewById(R.id.authors_edit_text);

        titleCheckBox = findViewById(R.id.title_check_box);
        sortTitleCheckBox = findViewById(R.id.sort_title_check_box);
        authorsCheckBox = findViewById(R.id.authors_check_box);

        titleCheckBox.setOnClickListener(BookEditActivity.this);
        sortTitleCheckBox.setOnClickListener(BookEditActivity.this);
        authorsCheckBox.setOnClickListener(BookEditActivity.this);

        Intent intent = getIntent();
        bookId = intent.getStringExtra("BOOK_ID");
        LiveData<Book> bookLiveData = AppDatabase.getBookDao().getByIdLive(bookId);
        bookLiveData.observe(this, new Observer<Book>() {
            @Override
            public void onChanged(@Nullable Book book) {
                if (book == null) {
                    Toast.makeText(BookEditActivity.this, R.string.activity_start_error, Toast.LENGTH_SHORT).show();
                } else {
                    BookEditActivity.this.book = book;
                    titleCheckBox.setChecked(!book.getTitle().equals(book.getOriginalTitle()));
                    titleEditText.setEnabled(titleCheckBox.isChecked());
                    titleEditText.setText(book.getTitle());

                    sortTitleCheckBox.setChecked(!book.getSortTitle().equals(book.getOriginalTitle()));
                    sortTitleEditText.setEnabled(sortTitleCheckBox.isChecked());
                    sortTitleEditText.setText(book.getSortTitle());

                    authorsCheckBox.setChecked(!book.getFormattedAuthors().equals(book.getFormattedOriginalAuthors()));
                    authorsEditText.setEnabled(authorsCheckBox.isChecked());
                    authorsEditText.setText(book.getFormattedAuthors());

                    if (book.getCover() != null)
                        coverImageView.setImageURI(Uri.fromFile(new File(book.getCover())));
                }
            }
        });

        changeCoverButton.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("title_enabled", titleCheckBox.isChecked());
        outState.putBoolean("sort_title_enabled", sortTitleCheckBox.isChecked());
        outState.putBoolean("authors_enabled", authorsCheckBox.isChecked());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        titleCheckBox.setChecked(savedInstanceState.getBoolean("title_enabled", false));
        sortTitleCheckBox.setChecked(savedInstanceState.getBoolean("sort_title_enabled", false));
        authorsCheckBox.setChecked(savedInstanceState.getBoolean("authors_enabled", false));

        titleEditText.setEnabled(titleCheckBox.isChecked());
        sortTitleEditText.setEnabled(sortTitleCheckBox.isChecked());
        authorsEditText.setEnabled(authorsCheckBox.isChecked());
        setNextFocuses();
    }

    @Override
    public void onClick(View v) {
        if (v == titleCheckBox) {
            titleEditText.setEnabled(titleCheckBox.isChecked());
            setNextFocuses();
            if (!titleCheckBox.isChecked())
                titleEditText.setText(book.getOriginalTitle());
            else
                EditTextUtils.focusAndShowKeyboard(titleEditText);
        } else if (v == sortTitleCheckBox) {
            sortTitleEditText.setEnabled(sortTitleCheckBox.isChecked());
            setNextFocuses();
            if (!sortTitleCheckBox.isChecked())
                sortTitleEditText.setText(book.getOriginalTitle());
            else
                EditTextUtils.focusAndShowKeyboard(sortTitleEditText);
        } else if (v == authorsCheckBox) {
            authorsEditText.setEnabled(authorsCheckBox.isChecked());
            setNextFocuses();
            if (!authorsCheckBox.isChecked())
                authorsEditText.setText(book.getFormattedOriginalAuthors());
            else
                EditTextUtils.focusAndShowKeyboard(authorsEditText);
        }
        if (v == changeCoverButton) {
            //TODO Implement change cover
            Toast.makeText(this, "Click Change Cover Button", Toast.LENGTH_SHORT).show();
        }

    }

    private void setNextFocuses() {
        ArrayList<EditText> editTexts = new ArrayList<>();
        if (titleEditText.isEnabled())
            editTexts.add(titleEditText);
        if (sortTitleEditText.isEnabled())
            editTexts.add(sortTitleEditText);
        if (authorsEditText.isEnabled())
            editTexts.add(authorsEditText);

        for (int i = 0; i < editTexts.size(); i++) {
            EditText editText = editTexts.get(i);
            if (i + 1 >= editTexts.size()) {
                editText.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            } else {
                editText.setImeOptions(EditorInfo.IME_ACTION_NEXT | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                editText.setNextFocusForwardId(editTexts.get(i + 1).getId());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_button) {
            save();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {
        book.setTitle(titleEditText.getText().toString());
        book.setSortTitle(sortTitleEditText.getText().toString());
        book.setFormattedAuthors(authorsEditText.getText().toString());

        ThreadUtils.runOnBackground(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getBookDao().insertAll(book);
            }
        });
        finish();
    }
}

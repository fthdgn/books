package tr.name.fatihdogan.books;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import tr.name.fatihdogan.books.data.Book;
import tr.name.fatihdogan.books.utils.EditTextUtils;

public class BookEditActivity extends BaseActivity implements View.OnClickListener {

    String bookId;
    Book book;
    ImageView coverImageView;
    ImageButton changeCoverButton;

    EditText titleEditText;
    EditText sortTitleEditText;
    EditText authorsEditText;

    CheckBox titleCheckBox;
    CheckBox sortTitleCheckBox;
    CheckBox authorsCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        bookId = intent.getStringExtra("BOOK_ID");
        book = Book.BOOKS.get(bookId);
        if (book == null) {
            Toast.makeText(this, R.string.activity_start_error, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        coverImageView = (ImageView) findViewById(R.id.cover_image_view);
        changeCoverButton = (ImageButton) findViewById(R.id.change_cover_button);

        titleEditText = (EditText) findViewById(R.id.title_edit_text);
        sortTitleEditText = (EditText) findViewById(R.id.sort_title_edit_text);
        authorsEditText = (EditText) findViewById(R.id.authors_edit_text);

        titleCheckBox = (CheckBox) findViewById(R.id.title_check_box);
        sortTitleCheckBox = (CheckBox) findViewById(R.id.sort_title_check_box);
        authorsCheckBox = (CheckBox) findViewById(R.id.authors_check_box);

        if (book.getCover() != null)
            coverImageView.setImageURI(Uri.fromFile(new File(book.getCover())));

        titleCheckBox.setChecked(book.getLocalTitle() != null);
        titleEditText.setEnabled(titleCheckBox.isChecked());
        titleEditText.setText(book.getTitle());
        titleCheckBox.setOnClickListener(this);

        sortTitleCheckBox.setChecked(book.getLocalSortTitle() != null);
        sortTitleEditText.setEnabled(sortTitleCheckBox.isChecked());
        sortTitleEditText.setText(book.getSortTitle());
        sortTitleCheckBox.setOnClickListener(this);

        authorsCheckBox.setChecked(book.getFormattedLocalAuthors() != null);
        authorsEditText.setEnabled(authorsCheckBox.isChecked());
        authorsEditText.setText(book.getFormattedAuthors());
        authorsCheckBox.setOnClickListener(this);

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
                titleEditText.setText(book.getOnlineTitle());
            else
                EditTextUtils.focusAndShowKeyboard(titleEditText);
        } else if (v == sortTitleCheckBox) {
            sortTitleEditText.setEnabled(sortTitleCheckBox.isChecked());
            setNextFocuses();
            if (!sortTitleCheckBox.isChecked())
                sortTitleEditText.setText(book.getOnlineSortTitle());
            else
                EditTextUtils.focusAndShowKeyboard(sortTitleEditText);
        } else if (v == authorsCheckBox) {
            authorsEditText.setEnabled(authorsCheckBox.isChecked());
            setNextFocuses();
            if (!authorsCheckBox.isChecked())
                authorsEditText.setText(book.getFormattedOnlineAuthors());
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
        book.setTitle(titleEditText.isEnabled() ? titleEditText.getText().toString() : null);
        book.setSortTitle(sortTitleEditText.isEnabled() ? sortTitleEditText.getText().toString() : null);
        book.setAuthors(authorsEditText.isEnabled() ? authorsEditText.getText().toString() : null);
        book.save();
        finish();
    }
}

package tr.name.fatihdogan.books.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import tr.name.fatihdogan.books.BaseApplication;
import tr.name.fatihdogan.books.R;
import tr.name.fatihdogan.books.activity.BookEditActivity;
import tr.name.fatihdogan.books.api.ImageManager;
import tr.name.fatihdogan.books.repository.Book;

public class BookView extends CardView implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TOOLS_NS = "http://schemas.android.com/tools/bookview";

    private ImageManager imageManager;

    private Book book;
    private ImageView coverImageView;
    private TextView titleTextView;
    private TextView authorTextView;
    private ImageButton optionButton;
    private PopupMenu optionPopupMenu;
    private MenuItem editMenuItem;
    private MenuItem aboutMenuItem;

    public BookView(Context context) {
        super(context);
        init(context, null);
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        imageManager = BaseApplication.getAppComponent().imageManager();
        setRadius(5);
        ViewCompat.setElevation(this, 5);
        inflate(context, R.layout.view_bookview, this);
        setUseCompatPadding(true);
        coverImageView = findViewById(R.id.cover_image_view);
        titleTextView = findViewById(R.id.title_text_view);
        authorTextView = findViewById(R.id.author_text_view);
        optionButton = findViewById(R.id.option_button);
        optionButton.setOnClickListener(this);
        optionPopupMenu = new PopupMenu(context, optionButton);
        editMenuItem = optionPopupMenu.getMenu().add(R.string.edit);
        aboutMenuItem = optionPopupMenu.getMenu().add(R.string.about);
        optionPopupMenu.setOnMenuItemClickListener(this);

        setOnClickListener(this);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BookView);
            setTitle(a.getText(R.styleable.BookView_title));
            coverImageView.setImageResource(a.getResourceId(R.styleable.BookView_cover, 0));
            setAuthor(a.getText(R.styleable.BookView_author));
            a.recycle();
        }

        if (isInEditMode() && attrs != null) {
            setTitle(attrs.getAttributeValue(TOOLS_NS, "title"));
            setAuthor(attrs.getAttributeValue(TOOLS_NS, "author"));
            coverImageView.setImageResource(attrs.getAttributeResourceValue(TOOLS_NS, "cover", 0));
        }
    }

    public void setBook(Book book) {
        this.book = book;
        setTitle(book.getTitle());
        setAuthor(book.getFormattedAuthors());
        setCover();
    }

    private void setTitle(CharSequence charSequence) {
        titleTextView.setText(charSequence);
    }

    private void setCover() {
        imageManager.load(this.book.getBookId(), coverImageView);
    }

    private void setAuthor(CharSequence charSequence) {
        authorTextView.setText(charSequence);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(optionButton)) {
            optionPopupMenu.show();
        } else if (v.equals(this)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/books/reader?id=" + book.getBookId()));
            getContext().startActivity(intent);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item == editMenuItem) {
            Intent intent = new Intent(getContext(), BookEditActivity.class);
            intent.putExtra("BOOK_ID", book.getBookId());
            getContext().startActivity(intent);
            return true;
        } else if (item == aboutMenuItem) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/books/details?id=" + book.getBookId()));
            getContext().startActivity(intent);
            return true;
        }
        return false;
    }

    //endregion
}

package tr.name.fatihdogan.books.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import tr.name.fatihdogan.books.R;
import tr.name.fatihdogan.books.activity.BookEditActivity;
import tr.name.fatihdogan.books.data.Book;
import tr.name.fatihdogan.books.utils.ImageUtils;

public class BookView extends CardView implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TOOLS_NS = "http://schemas.android.com/tools/bookview";
    private Book book;
    private ImageView coverImageView;
    private TextView titleTextView;
    private TextView authorTextView;
    private ImageButton optionButton;
    private CoverLoader coverLoader;
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
        setRadius(5);
        ViewCompat.setElevation(this, 5);
        inflate(context, R.layout.view_bookview, this);
        setUseCompatPadding(true);
        coverImageView = (ImageView) findViewById(R.id.cover_image_view);
        titleTextView = (TextView) findViewById(R.id.title_text_view);
        authorTextView = (TextView) findViewById(R.id.author_text_view);
        optionButton = (ImageButton) findViewById(R.id.option_button);
        optionButton.setOnClickListener(this);
        optionPopupMenu = new PopupMenu(context, optionButton);
        editMenuItem = optionPopupMenu.getMenu().add(R.string.edit);
        aboutMenuItem = optionPopupMenu.getMenu().add(R.string.about);
        optionPopupMenu.setOnMenuItemClickListener(this);

        setOnClickListener(this);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BookView);
            setTitle(a.getText(R.styleable.BookView_title));
            setCover(a.getResourceId(R.styleable.BookView_cover, 0));
            setAuthor(a.getText(R.styleable.BookView_author));
            a.recycle();
        }

        if (isInEditMode() && attrs != null) {
            setTitle(attrs.getAttributeValue(TOOLS_NS, "title"));
            setAuthor(attrs.getAttributeValue(TOOLS_NS, "author"));
            setCover(attrs.getAttributeResourceValue(TOOLS_NS, "cover", 0));
        }
    }

    public void setBook(Book book) {
        this.book = book;
        setTitle(book.getTitle());
        setAuthor(book.getFormattedAuthors());
        setCover(book.getCover());
    }

    private void setTitle(CharSequence charSequence) {
        titleTextView.setText(charSequence);
    }

    private void setCover(@DrawableRes int resId) {
        coverImageView.setBackgroundResource(android.R.color.transparent);
        cancelCoverLoad();
        coverLoader = new CoverLoader(resId, getResources());
        coverLoader.execute();
        if (isInEditMode()) {
            coverLoader.executeSync();
        }
    }

    private void setCover(String filePath) {
        setCover(android.R.color.transparent);
        coverImageView.setBackgroundResource(android.R.color.transparent);
        cancelCoverLoad();
        coverLoader = new CoverLoader(filePath, getResources());
        coverLoader.execute();
    }

    private void cancelCoverLoad() {
        if (coverLoader != null)
            coverLoader.cancel(false);
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
            intent.setData(Uri.parse("https://play.google.com/books/reader?id=" + book.getId()));
            getContext().startActivity(intent);
            book.updateLastUsageDate();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item == editMenuItem) {
            Intent intent = new Intent(getContext(), BookEditActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            getContext().startActivity(intent);
            return true;
        } else if (item == aboutMenuItem) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/books/details?id=" + book.getId()));
            getContext().startActivity(intent);
            return true;
        }
        return false;
    }

    //endregion

    private class CoverLoader extends AsyncTask<Void, Void, Bitmap> {

        static final int RESOURCE = 1;
        static final int PATH = 2;

        final int mode;
        @DrawableRes
        int resourceId;
        Bitmap bitmap;
        String path;

        final Resources resources;
        @ColorInt
        int color;

        CoverLoader(@DrawableRes int resourceId, Resources resources) {
            mode = RESOURCE;
            this.resourceId = resourceId;
            this.resources = resources;
        }

        CoverLoader(String path, Resources resources) {
            mode = PATH;
            this.path = path;
            this.resources = resources;

        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            if (mode == RESOURCE) {
                if (resourceId == 0)
                    return null;
                bitmap = BitmapFactory.decodeResource(resources, resourceId);
            } else if (mode == PATH) {
                bitmap = BitmapFactory.decodeFile(path);
            }

            if (bitmap == null)
                return null;

            color = ImageUtils.calculateAverageColor(bitmap, bitmap.getHeight() * bitmap.getHeight() / 20);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                return;
            }
            coverImageView.setImageBitmap(bitmap);
            coverImageView.setBackgroundColor(color);
        }

        void executeSync(Void... params) {
            onPostExecute(doInBackground(params));
        }
    }
}

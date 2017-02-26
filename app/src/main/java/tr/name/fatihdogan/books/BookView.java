package tr.name.fatihdogan.books;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import tr.name.fatihdogan.books.utils.LogUtils;

import static tr.name.fatihdogan.books.utils.ImageUtils.calculateAverageColor;

public class BookView extends CardView {

    private static final String TOOLS_NS = "http://schemas.android.com/tools/bookview";
    public ImageView coverImageView;
    private TextView titleTextView;
    private TextView authorTextView;
    private CoverLoader coverLoader;

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
        coverImageView = (ImageView) findViewById(R.id.imageView);
        titleTextView = (TextView) findViewById(R.id.textView);
        authorTextView = (TextView) findViewById(R.id.authorText);
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

    public void setTitle(CharSequence charSequence) {
        titleTextView.setText(charSequence);
    }

    public void setCover(@DrawableRes int resId) {
        coverImageView.setBackgroundResource(android.R.color.transparent);
        cancelCoverLoad();
        coverLoader = new CoverLoader(resId, getResources());
        coverLoader.execute();
        if (isInEditMode()) {
            coverLoader.executeSync();
        }
    }

    public void setCover(String filePath) {
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

    public void setAuthor(CharSequence charSequence) {
        authorTextView.setText(charSequence);
    }

    //endregion

    private class CoverLoader extends AsyncTask<Void, Void, Bitmap> {

        static final int RESOURCE = 1;
        static final int PATH = 2;

        int mode;
        @DrawableRes
        int resourceId;
        Bitmap bitmap;
        String path;

        Resources resources;
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

            color = calculateAverageColor(bitmap, bitmap.getHeight() * bitmap.getHeight() / 20);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                LogUtils.logCodeLocation("canceled");
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

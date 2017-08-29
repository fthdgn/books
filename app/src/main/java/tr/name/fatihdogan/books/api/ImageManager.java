package tr.name.fatihdogan.books.api;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageManager {

    private final Picasso picasso;

    public ImageManager(Picasso picasso) {
        this.picasso = picasso;
    }

    public void load(String id, ImageView imageView) {
        String url = "https://books.google.com/books/content?id=" + id +
                "&printsec=frontcover&img=1&zoom=1&source=gbs_api&w=320";

        picasso.load(url).placeholder(android.R.color.transparent).fit().into(imageView);
    }

}

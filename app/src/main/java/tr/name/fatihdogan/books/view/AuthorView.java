package tr.name.fatihdogan.books.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import tr.name.fatihdogan.books.R;
import tr.name.fatihdogan.books.activity.MainActivity;
import tr.name.fatihdogan.books.repository.AppDatabase;
import tr.name.fatihdogan.books.utils.EditTextUtils;
import tr.name.fatihdogan.books.utils.ThreadUtils;

public class AuthorView extends CardView implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TOOLS_NS = "http://schemas.android.com/tools";

    private TextView nameTextView;
    private ImageButton optionButton;
    private PopupMenu optionPopupMenu;
    private MenuItem editMenuItem;

    public AuthorView(Context context) {
        super(context);
        init(context, null);

    }

    public AuthorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public AuthorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setRadius(5);
        ViewCompat.setElevation(this, 5);
        inflate(context, R.layout.view_authorview, this);
        setUseCompatPadding(true);
        nameTextView = findViewById(R.id.name_text_view);
        optionButton = findViewById(R.id.option_button);
        setOnClickListener(this);
        optionButton.setOnClickListener(this);
        optionPopupMenu = new PopupMenu(context, optionButton);
        editMenuItem = optionPopupMenu.getMenu().add(R.string.edit);
        optionPopupMenu.setOnMenuItemClickListener(this);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AuthorView);
            setName(a.getText(R.styleable.AuthorView_name));
            a.recycle();
        }

        if (isInEditMode() && attrs != null) {
            setName(attrs.getAttributeValue(TOOLS_NS, "name"));
        }
    }

    public void setName(CharSequence name) {
        nameTextView.setText(name);
    }

    public CharSequence getName() {
        return nameTextView.getText();
    }

    @Override
    public void onClick(View v) {
        if (v == this) {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("AUTHOR", nameTextView.getText());
            v.getContext().startActivity(intent);
        }
        if (v == optionButton) {
            optionPopupMenu.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item == editMenuItem) {
            showEditDialog();
            return true;
        }
        return false;
    }

    private void showEditDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.edit_author_name);

        final EditText input = new EditText(getContext());
        input.setSingleLine();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setText(getName());
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
                ThreadUtils.runOnBackground(() ->
                        AppDatabase.getBookDao().renameAuthor(
                                nameTextView.getText().toString(), input.getText().toString())));
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();

        EditTextUtils.focusAndShowKeyboard(input);
    }

}

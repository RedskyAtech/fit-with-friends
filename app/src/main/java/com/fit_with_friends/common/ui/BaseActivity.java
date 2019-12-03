package com.fit_with_friends.common.ui;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import com.fit_with_friends.fitWithFriends.utils.Constants;

public abstract class BaseActivity extends Activity {

    protected Typeface arialBlackBold, arialBold, arialBoldItalic, arialItalic, arialNarrow, arialNarrowBold, arialNarrowBoldItalic, arialNarrowItalic, arialRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();

        arialBlackBold = Typeface.createFromAsset(getAssets(), Constants.ARIAL_BLACK_BOLD);
        arialBold = Typeface.createFromAsset(getAssets(), Constants.ARIAL_BOLD);
        arialBoldItalic = Typeface.createFromAsset(getAssets(), Constants.ARIAL_BOLD_ITALIC);
        arialItalic = Typeface.createFromAsset(getAssets(), Constants.ARIAL_ITALIC);
        arialNarrow = Typeface.createFromAsset(getAssets(), Constants.ARIAL_NARROW);
        arialNarrowBold = Typeface.createFromAsset(getAssets(), Constants.ARIAL_NARROW_BOLD);
        arialRegular = Typeface.createFromAsset(getAssets(), Constants.ARIAL_REGULAR);
        arialNarrowItalic = Typeface.createFromAsset(getAssets(), Constants.ARIAL_NARROW_ITALIC);
        arialNarrowBoldItalic = Typeface.createFromAsset(getAssets(), Constants.ARIAL_NARROW_BOLD_ITALIC);
    }

    protected abstract void setupActivityComponent();


}

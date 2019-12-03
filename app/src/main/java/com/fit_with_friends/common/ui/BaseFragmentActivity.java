package com.fit_with_friends.common.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.fit_with_friends.fitWithFriends.utils.Constants;

public abstract class BaseFragmentActivity extends FragmentActivity {

    protected Typeface arialBlackBold, arialBold, arialBoldItalic, arialItalic, arialNarrow, arialNarrowBold,    arialNarrowBoldItalic, arialNarrowItalic, arial_regular;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();

        arialBlackBold=Typeface.createFromAsset(getAssets(), Constants.ARIAL_BLACK_BOLD);
        arialBold =Typeface.createFromAsset(getAssets(), Constants.ARIAL_BOLD);
        arialBoldItalic =Typeface.createFromAsset(getAssets(), Constants.ARIAL_BOLD_ITALIC);
        arialItalic =Typeface.createFromAsset(getAssets(), Constants.ARIAL_ITALIC);
        arialNarrow =Typeface.createFromAsset(getAssets(), Constants.ARIAL_NARROW);
        arialNarrowBold =Typeface.createFromAsset(getAssets(), Constants.ARIAL_NARROW_BOLD);
        arial_regular =Typeface.createFromAsset(getAssets(), Constants.ARIAL_REGULAR);
        arialNarrowItalic =Typeface.createFromAsset(getAssets(), Constants.ARIAL_NARROW_ITALIC);
        arialNarrowBoldItalic =Typeface.createFromAsset(getAssets(), Constants.ARIAL_NARROW_BOLD_ITALIC);
    }

    protected abstract void setupActivityComponent();

}

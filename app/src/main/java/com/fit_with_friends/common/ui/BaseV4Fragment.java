package com.fit_with_friends.common.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.fit_with_friends.fitWithFriends.utils.Constants;

import java.util.Objects;

public abstract class BaseV4Fragment extends Fragment {

    protected Typeface arialBlackBold, arialBold, arialBoldItalic, arialItalic, arialNarrow, arialNarrowBold, arialNarrowBoldItalic, arialNarrowItalic, arialRegular;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();


        arialBlackBold = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), Constants.ARIAL_BLACK_BOLD);
        arialBold = Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_BOLD);
        arialBoldItalic = Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_BOLD_ITALIC);
        arialItalic = Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_ITALIC);
        arialNarrow = Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_NARROW);
        arialNarrowBold = Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_NARROW_BOLD);
        arialRegular = Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_REGULAR);
        arialNarrowItalic = Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_NARROW_ITALIC);
        arialNarrowBoldItalic = Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_NARROW_BOLD_ITALIC);
    }

    protected abstract void setupActivityComponent();

}

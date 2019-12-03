package com.fit_with_friends.common.ui;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import com.fit_with_friends.fitWithFriends.utils.Constants;

public abstract class BaseFragment extends Fragment {

    protected Typeface arialBlackBold, arialBold, arialBoldItalic, arialItalic, arialNarrow, arialNarrowBold,    arialNarrowBoldItalic, arialNarrowItalic, arial_regular;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();

        arialBlackBold=Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_BLACK_BOLD);
        arialBold =Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_BOLD);
        arialBoldItalic =Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_BOLD_ITALIC);
        arialItalic =Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_ITALIC);
        arialNarrow =Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_NARROW);
        arialNarrowBold =Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_NARROW_BOLD);
        arial_regular =Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_REGULAR);
        arialNarrowItalic =Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_NARROW_ITALIC);
        arialNarrowBoldItalic =Typeface.createFromAsset(getActivity().getAssets(), Constants.ARIAL_NARROW_BOLD_ITALIC);
    }

    protected abstract void setupActivityComponent();
}
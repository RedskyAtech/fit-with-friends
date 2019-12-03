package com.fit_with_friends.common.ui

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.fit_with_friends.R
import com.fit_with_friends.fitWithFriends.utils.CapitalizeFirstWord
import com.fit_with_friends.fitWithFriends.utils.Constants

abstract class BaseAppCompactActivity : AppCompatActivity() {

    private lateinit var arialBlackBold: Typeface
    private lateinit var arialBold: Typeface
    private lateinit var arialBoldItalic: Typeface
    private lateinit var arialItalic: Typeface
    private lateinit var arialNarrow: Typeface
    private lateinit var arialNarrowBold: Typeface
    private lateinit var arialNarrowBoldItalic: Typeface
    private lateinit var arialNarrowItalic: Typeface
    private lateinit var arialRegular: Typeface
    private lateinit var papyrus: Typeface
    private lateinit var copperplateGothicBOld: Typeface
    private lateinit var colonna: Typeface

    protected lateinit var capitalizeFirstWord: CapitalizeFirstWord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivityComponent()
        val window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_purple)

        capitalizeFirstWord = CapitalizeFirstWord()

        arialBlackBold = Typeface.createFromAsset(assets, Constants.ARIAL_BLACK_BOLD)
        arialBold = Typeface.createFromAsset(assets, Constants.ARIAL_BOLD)
        arialBoldItalic = Typeface.createFromAsset(assets, Constants.ARIAL_BOLD_ITALIC)
        arialItalic = Typeface.createFromAsset(assets, Constants.ARIAL_ITALIC)
        arialNarrow = Typeface.createFromAsset(assets, Constants.ARIAL_NARROW)
        arialNarrowBold = Typeface.createFromAsset(assets, Constants.ARIAL_NARROW_BOLD)
        arialRegular = Typeface.createFromAsset(assets, Constants.ARIAL_REGULAR)
        arialNarrowItalic = Typeface.createFromAsset(assets, Constants.ARIAL_NARROW_ITALIC)
        arialNarrowBoldItalic = Typeface.createFromAsset(assets, Constants.ARIAL_NARROW_BOLD_ITALIC)
        papyrus = Typeface.createFromAsset(assets, Constants.PAPYRUS)
        copperplateGothicBOld = Typeface.createFromAsset(assets, Constants.COPPERPLATE_GOTHIC_BOLD)
        colonna = Typeface.createFromAsset(assets, Constants.COLONNA)

    }

    protected abstract fun setupActivityComponent()
}

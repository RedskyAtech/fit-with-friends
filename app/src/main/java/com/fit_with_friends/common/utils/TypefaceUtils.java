package com.fit_with_friends.common.utils;

import android.graphics.Typeface;
import android.widget.TextView;

public class TypefaceUtils {

    public static void setTypeface(Typeface typefaceName, Object ... objects){
        for (Object obj: objects) {
            if(obj instanceof TextView){
                ((TextView)obj).setTypeface(typefaceName);
            }
        }
    }
}

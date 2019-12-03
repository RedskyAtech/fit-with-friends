package com.fit_with_friends.fitWithFriends.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {

    private EditText mEditText;

    public CustomTextWatcher(EditText e) {
        mEditText = e;
    }

    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
        int count = s.length();
        String str = s.toString();
        if (count == 1) {
            str = str + "'";
        } else if (count == 3) {
            str = str + "\"";
        } else if ((count > 4) && (str.charAt(str.length() - 1) != '\"')) {
            str = str.substring(0, str.length() - 2) + str.charAt(str.length() - 1) + "\"";
        } else {
            return;
        }
        mEditText.setText(str);
        mEditText.setSelection(mEditText.getText().length());
    }
}

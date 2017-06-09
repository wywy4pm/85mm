package com.arun.a85mm.utils;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by wy on 2017/6/9.
 */

public class InputUtils {
    public static void setInputEditEnd(EditText editText) {
        if (editText != null && !TextUtils.isEmpty(editText.getText())) {
            int tempSelection = editText.getText().length();
            editText.setSelection(tempSelection);//设置光标在最后
        }
    }
}

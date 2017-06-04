package com.jws.pandahealth.component.app.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyBoardUtil {


    public static void showKeyboard(Activity context) {
        if(context==null)
            return;
        final View v = context.getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)  context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(v, InputMethodManager.RESULT_SHOWN);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void hideKeyBoard(Activity context){
        if(context==null)
            return;
        final View v = context.getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


    /**
     * @param context （不保证返回结果一定正确  待补充）
     * @return
     */
    public static boolean isKeyBoardShow(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

}

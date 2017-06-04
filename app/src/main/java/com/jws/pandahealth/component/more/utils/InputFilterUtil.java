package com.jws.pandahealth.component.more.utils;

import android.text.InputFilter;
import android.text.Spanned;


public class InputFilterUtil implements InputFilter {

    private int maxLen = 20;// 最大输入字符

    public InputFilterUtil(int maxLen) {
        this.maxLen = maxLen;
    }

    @Override
    public CharSequence filter(CharSequence src, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int dindex = 0;
        int count = 0;

        while (count <= maxLen && dindex < dest.length()) {
            char c = dest.charAt(dindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > maxLen) {
            return dest.subSequence(0, dindex - 1);
        }

        int sindex = 0;
        while (count <= maxLen && sindex < src.length()) {
            char c = src.charAt(sindex++);
            if (c < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > maxLen) {
            sindex--;
        }

        return src.subSequence(0, sindex);
    }

}

package com.jblearning.puzzle;

import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.widget.TextView;

class DynamicSizing {
    static int MAX_FONT_SIZE = 200;
    private static final int MIN_FONT_SIZE = 1;

    static int setFontSizeToFitInView(TextView TextView) {
        TextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, MAX_FONT_SIZE);
        TextView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int lines = TextView.getLineCount();
        if (lines > 0) {
            while (lines != 1 && MAX_FONT_SIZE >= MIN_FONT_SIZE + 2) {
                MAX_FONT_SIZE--;
                TextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, MAX_FONT_SIZE);
                TextView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                lines = TextView.getLineCount();
            }
            TextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, --MAX_FONT_SIZE);
        }
        return MAX_FONT_SIZE;
    }
}

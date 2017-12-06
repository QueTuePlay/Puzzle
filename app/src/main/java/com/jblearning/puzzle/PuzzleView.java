package com.jblearning.puzzle;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Color;

public class PuzzleView extends RelativeLayout {
    private TextView[] TV_Array;
    private LayoutParams[] Layout_Params_Array;

    private int startY;
    private int labelHeight;
    private int startTouchY;
    private int[] positions;
    private int emptyPosition;

    public PuzzleView(Activity activity, int width, int height, int numberOfPieces) {
        super(activity);
        buildGuiByCode(activity, width, height, numberOfPieces);
    }

    public void buildGuiByCode(Activity activity, int width, int height, int numberOfPieces) {
        positions = new int[numberOfPieces];
        TV_Array = new TextView[numberOfPieces];
        int[] colors = new int[TV_Array.length];
        Layout_Params_Array = new LayoutParams[TV_Array.length];
        Random random = new Random();
        labelHeight = height / numberOfPieces;
        for (int i = 0; i < TV_Array.length; i++) {
            TV_Array[i] = new TextView(activity);
            TV_Array[i].setGravity(Gravity.CENTER);
            colors[i] = Color.rgb(random.nextInt(255),
                    random.nextInt(255), random.nextInt(255));
            TV_Array[i].setBackgroundColor(colors[i]);
            Layout_Params_Array[i] = new LayoutParams(width, labelHeight);
            Layout_Params_Array[i].leftMargin = 0;
            Layout_Params_Array[i].topMargin = labelHeight * i;
            addView(TV_Array[i], Layout_Params_Array[i]);
        }
    }

    public void fillGui(String[] scrambledText) {
        int minFontSize = DynamicSizing.MAX_FONT_SIZE;
        for (int i = 0; i < TV_Array.length; i++) {
            TV_Array[i].setText(scrambledText[i]);
            positions[i] = i;

            TV_Array[i].setWidth(Layout_Params_Array[i].width);
            TV_Array[i].setPadding(20, 5, 20, 5);

            int fontSize = DynamicSizing.setFontSizeToFitInView(TV_Array[i]);
            if (minFontSize > fontSize)
                minFontSize = fontSize;
        }
        Log.w("MainActivity", "font size = " + minFontSize);
        for (TextView aTextViewArray : TV_Array)
            aTextViewArray.setTextSize(TypedValue.COMPLEX_UNIT_SP, minFontSize);
    }

    public int indexOfTextView(View tv) {
        if (!(tv instanceof TextView))
            return -1;
        for (int i = 0; i < TV_Array.length; i++) {
            if (tv == TV_Array[i])
                return i;
        }
        return -1;
    }

    public void updateStartPositions(int index, int y) {
        startY = Layout_Params_Array[index].topMargin;
        startTouchY = y;
        emptyPosition = tvPosition(index);
    }

    public void moveTextViewVertically(int index, int y) {
        Layout_Params_Array[index].topMargin = startY + y - startTouchY;
        TV_Array[index].setLayoutParams(Layout_Params_Array[index]);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void enableListener(OnTouchListener listener) {
        for (TextView aTV_Array : TV_Array) aTV_Array.setOnTouchListener(listener);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void disableListener() {
        for (TextView aTV_Array : TV_Array) aTV_Array.setOnTouchListener(null);
    }

    public int tvPosition(int tvIndex) {
        return (Layout_Params_Array[tvIndex].topMargin + labelHeight / 2) / labelHeight;
    }

    public void placeTextViewAtPosition(int tvIndex, int toPosition) {
        Layout_Params_Array[tvIndex].topMargin = toPosition * labelHeight;
        TV_Array[tvIndex].setLayoutParams(Layout_Params_Array[tvIndex]);

        int index = positions[toPosition];
        Layout_Params_Array[index].topMargin = emptyPosition * labelHeight;
        TV_Array[index].setLayoutParams(Layout_Params_Array[index]);

        positions[emptyPosition] = index;
        positions[toPosition] = tvIndex;
    }

    public String[] currentSolution() {
        String[] current = new String[TV_Array.length];
        for (int i = 0; i < current.length; i++)
            current[i] = TV_Array[positions[i]].getText().toString();
        return current;
    }
}
package com.jblearning.puzzle;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements View.OnTouchListener {

    public static int STATUS_BAR_HEIGHT = 24;
    public static int ACTION_BAR_HEIGHT = 56;
    private PuzzleView puzzleView;
    private Puzzle puzzle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        puzzle = new Puzzle();
        Point size = new Point();

        getWindowManager().getDefaultDisplay().getSize(size);
        int screenHeight = size.y;
        int puzzleWidth = size.x;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float pixelDensity = metrics.density;

        TypedValue tv = new TypedValue();
        int actionBarHeight = (int) (pixelDensity * ACTION_BAR_HEIGHT);
        int statusBarHeight = (int) (pixelDensity * STATUS_BAR_HEIGHT);

        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, metrics);

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int puzzleHeight = screenHeight - statusBarHeight - actionBarHeight;
        if (resourceId != 0) statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        puzzleView = new PuzzleView(this, puzzleWidth, puzzleHeight, puzzle.getNumberOfParts());
        String[] scrambled = puzzle.scramble();
        puzzleView.fillGui(scrambled);
        puzzleView.enableListener(this);
        setContentView(puzzleView);
    }

    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        int index = puzzleView.indexOfTextView(view);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                puzzleView.updateStartPositions(index, (int) event.getY());
                puzzleView.bringChildToFront(view);
                break;
            case MotionEvent.ACTION_MOVE:
                puzzleView.moveTextViewVertically(index, (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                int newPosition = puzzleView.tvPosition(index);
                puzzleView.placeTextViewAtPosition(index, newPosition);
                if (puzzle.solved(puzzleView.currentSolution()))
                    puzzleView.disableListener();
                break;
        }
        return true;
    }
}

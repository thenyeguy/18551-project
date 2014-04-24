package edu.cmu.ece.ece551.uis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class SequencerView extends View {


    private Point size = new Point();
    private SparseArray pointerArray = new SparseArray();
    private Rect r = new Rect();
    private Paint paint = new Paint();

    private static final String TAG = "SequencerView";

    private boolean hasNext = false;

    public static final int FRAME_WIDTH = 130;
    public static final int FRAME_HEIGHT = 200;

    private SequencerState[][] measures = new SequencerState[4][16];
    private SequencerState nextMeasure;

    public SequencerView(Context context, AttributeSet attrs) {
        super(context);

        measures[2][5] = new SequencerState();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        display.getSize(size);

        setOnTouchListener(new MyOnTouchListener());

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Compute the height required to render the view
        // Assume Width will always be MATCH_PARENT.
        int width = View.MeasureSpec.getSize(widthMeasureSpec) - 20;
        int height = 1080 - 180;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ViewGroup.LayoutParams params = getLayoutParams();

        //Log.d("piano", "setting height and width");
        params.width = size.x;
        params.height = size.y;
        setLayoutParams(params);


        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        for (int i = 0; i < measures.length; i++) {
            for (int j = 0; j < measures[i].length; j++) {
                if (measures[i][j] == null) {
                    paint.setColor(Color.DKGRAY);
                } else {
                    paint.setColor(Color.LTGRAY);
                }

                paint.setStyle(Paint.Style.FILL);

                r.set(j * FRAME_WIDTH, i * FRAME_HEIGHT,
                        (j + 1) * FRAME_WIDTH, (i + 1) * FRAME_HEIGHT);

                canvas.drawRect(r, paint);

                paint.setStrokeWidth(3);
                paint.setColor(Color.GRAY);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(r, paint);

                if (measures[i][j] == null && hasNext) {
                    Log.d(TAG, "Making a crosshair");
                    // Green crosshair
                    paint.setColor(Color.GREEN);

                    canvas.drawLine(r.centerX() - 30, r.centerY(), r.centerX() + 30, r.centerY(), paint);

                    canvas.drawLine(r.centerX(), r.centerY() - 30, r.centerX(), r.centerY() + 30, paint);

                    canvas.drawArc(new RectF(r.left + 5, r.top + 5, r.left + 45, r.top + 85), 180, 90, false, paint);
                    canvas.drawArc(new RectF(r.right - 45, r.top + 5, r.right - 5, r.top + 85), 270, 90, false, paint);

                    canvas.drawArc(new RectF(r.left + 5, r.bottom - 85, r.left + 45, r.bottom - 5), 90, 90, false, paint);
                    canvas.drawArc(new RectF(r.right - 45, r.bottom - 85, r.right - 5, r.bottom - 5), 0, 90, false, paint);

                }
            }
        }
    }

    public void setNextThing(SequencerState ss) {
        hasNext = true;
        nextMeasure = ss;
    }


    private class MyOnTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent m) {
            int pointerCount = m.getPointerCount();

            clearAnimation();

            int action = m.getActionMasked();
            int actionIndex = m.getActionIndex();
            float x = m.getX(actionIndex);
            float y = m.getY(actionIndex);
            int id = m.getPointerId(actionIndex);

            Log.d("proll", "looking at pointer " + actionIndex + " of " + pointerCount + "with action " + action);

            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN: // Multitouch
                case MotionEvent.ACTION_DOWN:

                    int j = (int) (x) / FRAME_WIDTH;
                    //int k = (int) y / FRAME_HEIGHT + currentOctave * scale.getNotesPerOctave();
                    int k = (int) y / FRAME_HEIGHT;

                    Log.d("proll", "touch down with x: " + k + ", y: " + j);

                    if (hasNext) {
                        measures[k][j] = nextMeasure;
                        hasNext = false;
                    } else if (measures[k][j] != null) {
                        nextMeasure = measures[k][j];
                        hasNext = true;

                    }

                    break;

                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP:
                    if (pointerArray.get(id) != null) {
                        pointerArray.delete(id);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (pointerArray.get(id) == null) {
                        // do nothing if we move
                    }
                default:
            }

            return true;
        }
    }

    public SequencerState[][] getMeasures() {
        return measures;
    }

}

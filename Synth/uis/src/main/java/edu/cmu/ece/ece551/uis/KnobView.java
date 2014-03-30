package edu.cmu.ece.ece551.uis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.text.DecimalFormat;

/**
 * Created by michaelryan on 3/28/14.
 */
public class KnobView extends View {


    private SparseArray pointerArray = new SparseArray();
    private Paint paint = new Paint();

    float knobVal = 82f;

    private Point size = new Point();

    DecimalFormat dfor = new DecimalFormat("0");

    RectF rekt = new RectF();

    private GestureDetector gestures;

    public KnobView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setOnTouchListener(new MyTouchListener());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        display.getSize(size);

        gestures = new GestureDetector(getRootView().getContext(), new MyGestures());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestures.onTouchEvent(event);

    }

    @Override
    public void onDraw(Canvas canvas) {

        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setStrokeWidth(1);
        paint.setColor(Color.CYAN);

        paint.setTextSize(120f);

        canvas.drawText(dfor.format(knobVal / 165 * 100), size.x * 8 /17, size.y  / 2, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50);

        rekt.set(size.x / 2 - size.y / 4, size.y / 2 - size.y / 4, size.x / 2 + size.y / 4, size.y / 2 + size.y / 4);
        canvas.drawArc(rekt, 110f, 1 + knobVal * 2, false, paint);

        // First angle is start
        // Second is clockwise relative to it

    }

    private class MyGestures implements GestureDetector.OnGestureListener{


        private Float moveKnob(Float delta) {
            float result;
            if (delta > 0f) {
                result = knobVal + delta > 165f ? 165f : knobVal + delta;
            } else {
                result = knobVal + delta < 0f ? 0f : knobVal + delta;
            }
            return result;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float distY) {

            float newVal = moveKnob(distY/5);
            int intVal = Math.round(knobVal * 100 / 165);
            knobVal = newVal;

            Log.d("knob", "knobVal is now " + knobVal);

            if (Math.round(newVal) != intVal) {
                invalidate();
                return true;
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
            return true;
        }
    }




}
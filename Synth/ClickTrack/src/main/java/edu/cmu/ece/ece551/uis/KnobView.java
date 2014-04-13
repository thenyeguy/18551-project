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
    private Paint paint = new Paint();

    float knobVal = 0.5f;

    private Point size = new Point();

    RectF rekt = new RectF();

    private KnobReceiver receiver;

    private GestureDetector gestures;

    public KnobView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setOnTouchListener(new MyTouchListener());

        gestures = new GestureDetector(getRootView().getContext(), new MyGestures());

        receiver = new DefaultReceiver();
    }

    public void registerKnobReceiver(KnobReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestures.onTouchEvent(event);

    }

    @Override
    public void onDraw(Canvas canvas) {
        size.set(getLayoutParams().width, getLayoutParams().height);

        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setStrokeWidth(1);
        paint.setColor(Color.CYAN);

        paint.setTextSize(120f);

        canvas.drawText(receiver.formatValue(knobVal), size.x * 8 /17, size.y  / 2, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50);

        rekt.set(size.x / 2 - size.y / 4, size.y / 2 - size.y / 4, size.x / 2 + size.y / 4, size.y / 2 + size.y / 4);
        canvas.drawArc(rekt, 110f, knobVal * 165 * 2, false, paint);

        // First angle is start
        // Second is clockwise relative to it

    }

    private class MyGestures implements GestureDetector.OnGestureListener{


        private Float moveKnob(Float delta) {
            float result;
            delta /= 165f;
            if (delta > 0f) {
                result = knobVal + delta > 1f ? 1f : knobVal + delta;
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
            int intVal = Math.round(knobVal * 100);
            knobVal = newVal;

            receiver.onKnobChange(knobVal);

            if (Math.round(newVal * 100) != intVal) {
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

    private class DefaultReceiver implements KnobReceiver {
        private DecimalFormat dfor = new DecimalFormat("0");

        @Override
        public void onKnobChange(float value) {}

        @Override
        public String formatValue(float value) {
            return dfor.format(value*100);
        }
    }
}
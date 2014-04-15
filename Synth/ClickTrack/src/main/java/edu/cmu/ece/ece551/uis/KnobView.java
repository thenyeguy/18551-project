package edu.cmu.ece.ece551.uis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
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

    int knobSteps = 500;
    float knobVal = 0.5f;

    RectF rekt = new RectF();

    private KnobReceiver receiver;
    String name;

    private GestureDetector gestures;

    public KnobView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setOnTouchListener(new MyTouchListener());

        gestures = new GestureDetector(getRootView().getContext(), new MyGestures());

        receiver = new DefaultReceiver();

        name = "";
    }

    public void registerKnobReceiver(KnobReceiver receiver) {
        this.receiver = receiver;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(float value) {
        knobVal = receiver.getValue(value);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestures.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(1, Paint.ANTI_ALIAS_FLAG));
        super.dispatchDraw(canvas);
    }

    @Override
    public void onDraw(Canvas canvas) {
        RectF bounds = new RectF(0, 0, getWidth(), getHeight());

        // Draw the arc -  a cyan arc for the foreground and a dark gray for the background
        float strokeWidth = bounds.width() / 12f;
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);

        rekt.set(0+strokeWidth, 0+strokeWidth, bounds.width()-strokeWidth, bounds.height()-strokeWidth);

        paint.setColor(Color.DKGRAY);
        canvas.drawArc(rekt, 105, 165 * 2, false, paint);

        paint.setColor(Color.CYAN);
        canvas.drawArc(rekt, 105, knobVal * 165 * 2, false, paint);
        // First angle is start
        // Second is clockwise relative to it

        // Draw the text
        paint.setColor(Color.CYAN);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(1.6f * strokeWidth);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);

        String text = receiver.formatValue(knobVal);
        float textOffset = (paint.descent() + paint.ascent()) / 2;

        // Draw in different locations if the dial is labeled
        if(name.equals("")) {
            canvas.drawText(text, bounds.centerX(), bounds.centerY() - textOffset, paint);
        } else {
            float lineOffset = textOffset * 1.6f;
            canvas.drawText(text, bounds.centerX(), bounds.centerY()-textOffset-lineOffset,  paint);
            paint.setColor(Color.LTGRAY);
            canvas.drawText(name, bounds.centerX(), bounds.centerY()-textOffset+lineOffset,  paint);
        }
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
            int intVal = Math.round(knobVal * knobSteps);
            knobVal = newVal;

            receiver.onKnobChange(knobVal);

            if (Math.round(newVal * knobSteps) != intVal) {
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

        @Override
        public float getValue(float value) {
            return value/100;
        }
    }
}
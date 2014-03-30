package edu.cmu.ece.ece551.uis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by michaelryan on 2/23/14.
 */
public class PianoRollView extends View {

    private int[][] sequences = new int[7*7][16];

    private static final int KEYSIZE = 150;
    private static final int NUM_OCTAVES = 7;
    private static final int FRAME_WIDTH = 130;
    private static final int FRAME_HEIGHT = 105;

    private String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};



    private int currentOctave = 2;

    private Point size = new Point();
    private SparseArray pointerArray = new SparseArray();

    private Scale scale = new DiatonicScale("C", Tonality.MAJOR);

    private Rect r = new Rect();

    private Paint paint = new Paint();


    private int rectX = 0;

    public PianoRollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(new myTouchListener());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        display.getSize(size);

        // This is the constructor that actually gets used.

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Compute the height required to render the view
        // Assume Width will always be MATCH_PARENT.
        int width = MeasureSpec.getSize(widthMeasureSpec) - 200;
        int height = 1080 - 180;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ViewGroup.LayoutParams params = getLayoutParams();

        //Log.d("piano", "setting height and width");
        params.width = size.x - 150;
        params.height = size.y - 40;
        setLayoutParams(params);

        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        List<String> scaleNotes = scale.getNoteNames();

        // Math here to determine how big to make things?

        int octStart = currentOctave * scale.getNotesPerOctave();
        int octEnd = octStart + scale.getNotesPerOctave();

        for (int i = octStart; i < octEnd; i++) {

            int normPos = i - octStart;
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.FILL);
            r.set(0, normPos * FRAME_HEIGHT,
                    KEYSIZE, (normPos + 1) * FRAME_HEIGHT);
            canvas.drawRect(r, paint);

            paint.setTextSize(100);
            paint.setColor(Color.GREEN);
            canvas.drawText(scaleNotes.get(normPos), 5, (normPos+.9f) * FRAME_HEIGHT, paint);

            for (int j = 0; j < sequences[i].length; j++) {

                if (sequences[i][j] == 1) {
                    paint.setColor(Color.RED);
                } else if (sequences[i][j] == 2) {
                    paint.setColor(Color.YELLOW);
                } else {
                    paint.setColor(Color.WHITE);
                }
                paint.setStyle(Paint.Style.FILL);
                r.set(KEYSIZE + j * FRAME_WIDTH, normPos * FRAME_HEIGHT,
                        KEYSIZE + (j + 1) * FRAME_WIDTH, (normPos + 1) * FRAME_HEIGHT);
                canvas.drawRect(r, paint);
                paint.setStrokeWidth(5);
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(r, paint);
            }
        }

        // draw a blue line

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20);
        canvas.drawLine(KEYSIZE + rectX, 0, KEYSIZE + rectX, params.height, paint);

    }


    public void setRectX(int x) {
        rectX = x;
        Log.d("proll", "moved rectX to " + x);
        invalidate();
    }

    public void moveRect(final int rectX) {

        int j = (rectX / FRAME_WIDTH);
        for (int i = 0; i < sequences.length; i++) {
            if (sequences[i][j] == 1) {
                sequences[i][j] = 2;
                // Start note playing
            }
            if (j > 0) {
                if (sequences[i][j-1] == 2) {
                    sequences[i][j-1] = 1;
                    // Stop Note Playing
                }
            }
        }

        if (rectX == 0) {
            for (int i = 0; i < sequences.length; i++) {
                for (int k = 0; k < sequences.length; k++) {
                    if (sequences[i][k] == 2) {
                        sequences[i][k] = 1;
                        // Stop note for the rest. We should be able to just loop through the last column anyway
                    }
                }

            }
        }

        setRectX(rectX);
        invalidate();
    }


    public void octaveUp() {
        currentOctave = (currentOctave < 6) ? currentOctave + 1 : currentOctave;
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public void octaveDown() {
        currentOctave = (currentOctave > 0) ? currentOctave - 1 : currentOctave;
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }


    public void setScale(ScaleType type, String startingNote) {
        Scale scale;
        switch (type) {
            case DIATONIC_MAJOR:
                scale = new DiatonicScale(startingNote, Tonality.MAJOR);
                break;
            case DIATONIC_MINOR:
                scale = new DiatonicScale(startingNote, Tonality.MINOR);
                break;
            case MAJOR_BLUES:
                scale = new BluesScale(startingNote, Tonality.MAJOR);
                break;
            case MINOR_BLUES:
                scale = new BluesScale(startingNote, Tonality.MINOR);
                break;
            case MAJOR_PENTATONIC:
                scale = new PentatonicScale(startingNote, Tonality.MAJOR);
                break;
            case MINOR_PENTATONIC:
                scale = new PentatonicScale(startingNote, Tonality.MINOR);
                break;
            default:
                scale = null;
        }

        sequences = new int[NUM_OCTAVES * scale.getNotesPerOctave()][16];
        this.scale = scale;
    }


    private class myTouchListener implements OnTouchListener {

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

                    if (pointerArray.get(id) == null) {

                        pointerArray.put(id, new PointF(x, y));
                        int j = (int) (x - KEYSIZE) / FRAME_WIDTH;
                        int k = (int) y / FRAME_HEIGHT + currentOctave * scale.getNotesPerOctave();

                        Log.d("proll", "touch down with x: " + k + ", y: " + j);

                        if (k < sequences.length && j < sequences[k].length) {
                            if (sequences[k][j] == 1) {
                                sequences[k][j] = 0;
                            } else {
                                sequences[k][j] = 1;
                            }
                            view.invalidate();
                        }
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





}
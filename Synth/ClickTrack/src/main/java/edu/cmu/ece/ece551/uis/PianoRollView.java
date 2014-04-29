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

import edu.cmu.ece.ece551.clicktrack.InstrumentController;
import edu.cmu.ece.ece551.uis.Notation.MusicNote;
import edu.cmu.ece.ece551.uis.scale.BluesScale;
import edu.cmu.ece.ece551.uis.scale.DiatonicScale;
import edu.cmu.ece.ece551.uis.scale.DrumScale;
import edu.cmu.ece.ece551.uis.scale.PentatonicScale;
import edu.cmu.ece.ece551.uis.scale.Scale;
import edu.cmu.ece.ece551.uis.scale.ScaleType;
import edu.cmu.ece.ece551.uis.scale.Tonality;

/**
 * Created by michaelryan on 2/23/14.
 */
public class PianoRollView extends View {

    private InstrumentController instrument;

    private static final int KEYSIZE = 150;
    private static final int NUM_OCTAVES = 7;
    public static final int FRAME_WIDTH = 100;
    public static final int FRAME_HEIGHT = 105;

    private Point size = new Point();
    private SparseArray pointerArray = new SparseArray();
    private Rect r = new Rect();
    private Paint paint = new Paint();

    private float rectX = 0;

    private SequencerState state;

    public PianoRollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(new myTouchListener());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        display.getSize(size);

        instrument = null;
        // This is the constructor that actually gets used.
        state = new SequencerState();

    }

    public void setInstrument(InstrumentController instrument) {
        this.instrument = instrument;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Compute the height required to render the view
        // Assume Width will always be MATCH_PARENT.
        int width = MeasureSpec.getSize(widthMeasureSpec) - 20;
        int height = 1080 - 180;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ViewGroup.LayoutParams params = getLayoutParams();

        params.width = size.x - 150;
        params.height = size.y - 40;
        setLayoutParams(params);

        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL);
        //canvas.drawPaint(paint);


        List<MusicNote> scaleNotes = state.getScale().getNoteNames();

        // Math here to determine how big to make things?


        int notesPerOctave = state.getScale().getNotesPerOctave();
        int octStart = state.getCurrentOctave() * notesPerOctave;
        int octEnd = octStart + notesPerOctave;

        int[][] sequences = state.getSequences();


        for (int i = octEnd - 1; i >= octStart; i--) {

            int normPos = i - octStart;
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.FILL);
            r.set(0, normPos * FRAME_HEIGHT,
                    KEYSIZE, (normPos + 1) * FRAME_HEIGHT);
            canvas.drawRect(r, paint);

            paint.setTextSize(100);
            paint.setColor(Color.GREEN);
            canvas.drawText(scaleNotes.get(notesPerOctave - 1 - normPos).toString(), 5,
                    (normPos + .9f) * FRAME_HEIGHT, paint);

            for (int j = 0; j < sequences[i].length; j++) {

                if (sequences[i][j] == 1) {
                    paint.setColor(Color.RED);
                } else if (sequences[i][j] == 2) {
                    paint.setColor(Color.YELLOW);
                } else {
                    paint.setColor(Color.WHITE);
                }
                paint.setStyle(Paint.Style.FILL);

                r.set(KEYSIZE + j * FRAME_WIDTH, (notesPerOctave - 1 - normPos) * FRAME_HEIGHT,
                        KEYSIZE + (j + 1) * FRAME_WIDTH, (notesPerOctave - normPos) * FRAME_HEIGHT);

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


    public void setRectX(float x) {
        rectX = x;
        invalidate();
    }


    public void moveRect(final float rectX) {

        int[][] sequences = state.getSequences();
        int notesPerOctave = state.getScale().getNotesPerOctave();

        if (rectX == 0) {
            for (int i = 0; i < sequences.length; i++) {
                for (int k = 0; k < sequences[i].length; k++) {
                    if (sequences[i][k] == 2) {
                        sequences[i][k] = 1;
                        // Stop note for the rest. We should be able to just loop through the last column anyway
                    }
                }

            }
        }
        else {
            int j = ((int) rectX / FRAME_WIDTH);
            if (j >= sequences[0].length || j < 0) return;
            for (int i = 0; i < sequences.length; i++) {
                if (j > 0) {
                    if (sequences[i][j - 1] == 2) {
                        sequences[i][j - 1] = 1;
                        int idx = i % notesPerOctave;
                        int note = state.getScale().getNoteNames().get(idx).midi + i / notesPerOctave * 12; // TODO: get this note number for MIDI
                        if ( j == 15 || (j < 15 && sequences[i][j] != 1)) {
                            //instrument.noteUp(note, 1.0f);
                        }
                    }
                }
            }
            for (int i = 0; i < sequences.length; i++) {
                if (sequences[i][j] == 1) {
                    sequences[i][j] = 2;
                    int idx = i % notesPerOctave;
                    int note = state.getScale().getNoteNames().get(idx).midi + i / notesPerOctave * 12; // TODO: get this note number for MIDI
                }
            }
        }

        setRectX(rectX);
        invalidate();
    }


    public int octaveUp() {
        int oct = state.getCurrentOctave();
        state.setCurrentOctave((oct < 6) ? oct + 1 : oct);
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });

        return state.getCurrentOctave();
    }

    public int octaveDown() {
        int oct = state.getCurrentOctave();
        state.setCurrentOctave((oct > 0) ? oct - 1 : oct);
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });

        return state.getCurrentOctave();
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

        state.setScale(scale);

    }

    public void clearGrid() {
        state.clearGrid();
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public int getOctave() {
        return state.getCurrentOctave();
    }

    public float getRectX() {
        return rectX;
    }

    public void resetBoard() {
        int[][] seqs = state.getSequences();
        for (int i = 0; i < seqs.length; i++) {
            for (int j = 0; j < seqs[i].length; j++) {
                if (seqs[i][j] == 2) {
                    seqs[i][j] = 1;
                }
            }
        }

    }

    public void prepForDrums() {
        // Drums are limited to octave 0.
        state.setScale(new DrumScale());
        state.setCurrentOctave(0);
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

            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN: // Multitouch
                case MotionEvent.ACTION_DOWN:

                    if (pointerArray.get(id) == null) {

                        pointerArray.put(id, new PointF(x, y));
                        int j = (int) (x - KEYSIZE) / FRAME_WIDTH;
                        //int k = (int) y / FRAME_HEIGHT + currentOctave * scale.getNotesPerOctave();
                        int k = (state.getCurrentOctave() + 1) * state.getScale().getNotesPerOctave() - 1 - (int) y / FRAME_HEIGHT;

                        int[][] sequences = state.getSequences();

                        if (0 <= k && k < sequences.length && 0 <= j && j < sequences[k].length) {
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

    public String convert(int[][] M) {
        String separator = ", ";
        StringBuffer result = new StringBuffer();

        // iterate over the first dimension
        for (int i = 0; i < M.length; i++) {
            // iterate over the second dimension
            if ( i/NUM_OCTAVES == state.getCurrentOctave()) {
                result.append("!!");
            }
            for(int j = 0; j < M[i].length; j++){
                result.append(M[i][j]);
                result.append(separator);
            }
            // remove the last separator
            result.setLength(result.length() - separator.length());
            // add a line break.
            result.append("\n");
        }
        return result.toString();
    }


    public SequencerState getState() {
        return state;
    }

    public void setState(SequencerState state) {
        this.state.update(state);
    }

}

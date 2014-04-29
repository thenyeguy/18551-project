package edu.cmu.ece.ece551.uis.scale;

import android.util.Log;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.ece.ece551.clicktrack.InstrumentController;
import edu.cmu.ece.ece551.clicktrack.SubtractiveSynthController;
import edu.cmu.ece.ece551.uis.PianoRollView;
import edu.cmu.ece.ece551.uis.SequencerState;

public class TimingManager {


    private String TAG = "Timing Manager";

    private SequencerState state;
    private PianoRollView prv;

    private boolean playing;
    private Timer timer;
    private InstrumentController inst;

    private int tempo = 100;

    private float rectIncrement = 10;
    final private HashSet<Integer> notesPlaying;

    public TimingManager(SequencerState state, PianoRollView prv) {
        this.state = state;
        this.prv = prv;
        notesPlaying = Sets.newHashSet();
    }

    public TimingManager(SequencerState state, int tempo, InstrumentController inst) {
        this.state = state;
        this.tempo = tempo;
        this.inst = inst;
        notesPlaying = Sets.newHashSet();
        prv = null;
    }

    public void playMeasure() {

        playing = true;
        // minutes / beats * ms / minute
        final float msPerMeasure = 4.25f / tempo * 60000f;
        final float msPerSixteenth = msPerMeasure / 16f;
        rectIncrement = PianoRollView.FRAME_WIDTH / msPerSixteenth * 10;

        TimerTask tt = new TimerTask() {
            long startTime = System.currentTimeMillis();

            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long diff = currentTime - startTime;

                int j = (int) (diff / (long) msPerSixteenth);

                int[][] sequences = state.getSequences();

                if (diff >= msPerMeasure || j >= sequences[0].length) {

                    stopPlaying();
                    cancel();
                    return;
                }

                if (prv != null) {
                    prv.post(new Runnable() {

                        @Override
                        public void run() {
                            prv.moveRect(prv.getRectX() + rectIncrement);
                        }
                    });
                }

                if (j < 0) return;

                for (int i = 0; i < sequences.length; i++) {
                    if (j > 0) {
                        if (sequences[i][j - 1] != 0) {

                            int idx = i % state.getScale().getNotesPerOctave();
                            int note = state.getScale().getNoteNames().get(idx).midi + i / state.getScale().getNotesPerOctave() * 12;

                            if (notesPlaying.contains(note)) {
                                if (j == 15 || sequences[i][j] == 0) {
                                    Log.d(TAG, "note up at " + (currentTime-startTime) +": " +
                                            note);
                                    inst.noteUp(note, 0.0f);
                                    notesPlaying.remove(note);
                                }
                            }

                        }
                    }
                }

                for (int i = 0; i < sequences.length; i++) {
                    if (sequences[i][j] != 0) {

                        int idx = i % state.getScale().getNotesPerOctave();
                        int note = state.getScale().getNoteNames().get(idx).midi + i / state.getScale().getNotesPerOctave() * 12;

                        if (!notesPlaying.contains(note)) {
                            if (j == 0 || sequences[i][j - 1] == 0) {
                                Log.d(TAG, "note down at " + (currentTime-startTime) + ": " + note);
                                inst.noteDown(note, 1.0f);
                                notesPlaying.add(note);
                            }

                        }
                    }
                }

            }

        };

        timer = new Timer();
        timer.schedule(tt, 0, 10);

    }


    public void setInst(InstrumentController inst) {
        this.inst = inst;
    }

    public void stopPlaying() {
        stopNotes();
        if(timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    /**
     * Clears all notes that are still being played
     */
    private void stopNotes() {
        for(Integer note : notesPlaying) {
            inst.noteUp(note, 0.0f);
        }
        notesPlaying.clear();

        if (prv != null) {
            prv.post(new Runnable() {

                @Override
                public void run() {
                    prv.setRectX(0);
                    prv.resetBoard();
                    prv.invalidate();
                }
            });
        }

        playing = false;
        timer = null;
    }


    public boolean isPlaying() {
        return playing;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }
}

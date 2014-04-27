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
        Log.d("timing", "rectInc: " + rectIncrement);
        Log.d("timing", "msPerM: " + msPerMeasure + "; msPerS:" + msPerSixteenth);

        timer = new Timer();

        TimerTask tt = new TimerTask() {
            long startTime = System.currentTimeMillis();

            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long diff = currentTime - startTime;

                int j = (int) ((diff - msPerSixteenth) / (long) msPerSixteenth);

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
                                    Log.d("timing", "ending " + state.getScale().getNoteNames().get(idx).toString());
                                    inst.noteUp(note, 1.0f);
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
                                Log.d("timing", "starting " + state.getScale().getNoteNames().get(idx).toString());
                                inst.noteDown(note, 1.0f);
                                notesPlaying.add(note);
                            }

                        }
                    }
                }

            }

        };

        timer.schedule(tt, 0, 10);

    }


    public void setInst(InstrumentController inst) {
        this.inst = inst;
    }

    public void stopPlaying() {
        Log.d("timing", "stopping!");
        stopNotes();
        timer.cancel();
        timer.purge();
    }

    /**
     * Clears all notes that are still being played
     */
    private void stopNotes() {
        Iterator<Integer> iter = notesPlaying.iterator();

        Log.d("timing", "STOPPING!");
        while (iter.hasNext()) {
            inst.noteUp(iter.next(), 1.0f);
        }

        if (prv != null) {
            prv.post(new Runnable() {

                @Override
                public void run() {
                    prv.setRectX(-100);
                    prv.resetBoard();
                    prv.invalidate();
                }
            });
        }

        playing = false;
    }


    public boolean isPlaying() {
        return playing;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }
}

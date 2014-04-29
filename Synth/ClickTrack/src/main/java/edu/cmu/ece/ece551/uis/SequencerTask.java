package edu.cmu.ece.ece551.uis;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.ece.ece551.clicktrack.DrumMachineController;
import edu.cmu.ece.ece551.clicktrack.FMSynthController;
import edu.cmu.ece.ece551.clicktrack.InstrumentController;
import edu.cmu.ece.ece551.clicktrack.SubtractiveSynthController;
import edu.cmu.ece.ece551.uis.scale.TimingManager;

/**
 * Task for running the sequencer. Can be set to loop and in that case will fire
 * piano roll music at the start of each measure.
 */
public class SequencerTask extends TimerTask {

    private static PianoRollFragment prf;
    private static boolean looping;
    private static SequencerState[][] ss;
    private static Timer timer;

    private static SequencerTask instance;
    private static int tempo;

    private static String TAG = "SequencerTask";

    private int index = -1;

    private TimingManager someTM;

    private SequencerTask() {
        someTM = null;
    }

    public static SequencerTask getInstance() {
        return instance;
    }

    @Override
    public void run() {
        while(someTM != null && someTM.isPlaying()) {
            try {
                Thread.sleep(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        index++;

        for (int j = 0; j < 3; j++) {
            SequencerState measure = ss[j][index];
            if (measure != null) {
                TimingManager tm = new TimingManager(measure,
                        tempo, getInstFromIndex(j));
                someTM = tm;

                tm.playMeasure();
                // TODO: Register to be canceled
            }

        }

        if (prf != null && looping) {
            prf.playMeasure(tempo);
        }

        if (index >= 7) {
            index = -1;
            if (!looping) {
                this.cancel();
                someTM = null;
            }
        }
    }

    public static PianoRollFragment getPrf() {
        return prf;
    }

    public static void setPrf(PianoRollFragment prf) {
        SequencerTask.prf = prf;
    }

    public static boolean isLooping() {
        return looping;
    }

    public static void setLooping(boolean looping) {
        SequencerTask.looping = looping;
    }

    public static void startSequencer(SequencerState[][] ss, int tempo1) {
        instance = new SequencerTask();
        instance.ss = ss;
        timer = new Timer();

        tempo = tempo1;

        int secondsPerMeasure = (int) (1f / ((float) tempo / 60f / 4f / 1000f));

        timer.schedule(instance, 0, secondsPerMeasure);

    }

    private InstrumentController getInstFromIndex(int i) {
        switch (i) {
            case 0:
                return SubtractiveSynthController.getInstance();
            case 1:
                return FMSynthController.getInstance();
            case 2:
                return DrumMachineController.getInstance();
        }
        return null;
    }

    public int getTempo() {
        return tempo;
    }

    public static void registerPianoRoll(PianoRollFragment pianoRollFragment) {
        prf = pianoRollFragment;
    }

    public static void stop() {
        if (instance != null) {
            timer.cancel();
            timer.purge();
            instance = null;
        }
    }
}

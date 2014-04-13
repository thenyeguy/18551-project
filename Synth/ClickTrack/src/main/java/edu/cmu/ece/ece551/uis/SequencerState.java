package edu.cmu.ece.ece551.uis;

import edu.cmu.ece.ece551.clicktrack.InstrumentController;
import edu.cmu.ece.ece551.clicktrack.SubtractiveSynthController;
import edu.cmu.ece.ece551.uis.scale.DiatonicScale;
import edu.cmu.ece.ece551.uis.scale.Scale;
import edu.cmu.ece.ece551.uis.scale.Tonality;

/**
 * Created by michaelryan on 4/8/14.
 */
public class SequencerState {

    private static int NUM_BEATS = 16;
    private static int NUM_OCTAVES = 7;

    private int[][] sequences;
    private int currentOctave = 2;
    private Scale scale;
    private InstrumentController instrument = SubtractiveSynthController.getInstance();

    public SequencerState() {
        scale = new DiatonicScale("C", Tonality.MAJOR);
        sequences = new int[NUM_OCTAVES* scale.getNotesPerOctave()][NUM_BEATS];
    }

    public SequencerState(Scale scale) {
        this.scale = scale;
        sequences = new int[NUM_OCTAVES * scale.getNotesPerOctave()][NUM_BEATS];
    }

    public int getCurrentOctave() {
        return currentOctave;
    }

    public void setCurrentOctave(int currentOctave) {
        this.currentOctave = currentOctave;
    }

    public int[][] getSequences() {
        return sequences;
    }

    public Scale getScale() {
        return scale;
    }

    public InstrumentController getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentController instrument) {
        this.instrument = instrument;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
        sequences = new int[NUM_OCTAVES * scale.getNotesPerOctave()][NUM_BEATS];
    }

    public void clearGrid() {
        sequences = new int[NUM_OCTAVES][NUM_BEATS];
    }
}

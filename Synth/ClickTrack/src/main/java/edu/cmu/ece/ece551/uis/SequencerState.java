package edu.cmu.ece.ece551.uis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

import edu.cmu.ece.ece551.clicktrack.InstrumentController;
import edu.cmu.ece.ece551.clicktrack.SubtractiveSynthController;
import edu.cmu.ece.ece551.uis.scale.DiatonicScale;
import edu.cmu.ece.ece551.uis.scale.Scale;
import edu.cmu.ece.ece551.uis.scale.Tonality;

public class SequencerState implements Serializable{

    private static int NUM_BEATS = 16;
    private static int NUM_OCTAVES = 7;

    private int[][] sequences;
    private int currentOctave = 2;

    private Scale scale;
    private String name;

    public SequencerState() {
        scale = new DiatonicScale("C", Tonality.MAJOR);
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

    public void setScale(Scale scale) {
        this.scale = scale;
        clearGrid();
    }

    public void clearGrid() {
        sequences = new int[NUM_OCTAVES * scale.getNotesPerOctave()][NUM_BEATS];
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        Type myType = new TypeToken<SequencerState>() {}.getType();
        return gson.toJson(this, myType);
    }

    public void update(SequencerState source) {
        sequences = source.sequences;
        currentOctave = source.currentOctave;
        scale = source.scale;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package edu.cmu.ece.ece551.uis;

import android.util.JsonToken;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

import edu.cmu.ece.ece551.clicktrack.InstrumentController;
import edu.cmu.ece.ece551.clicktrack.NativeClickTrack;
import edu.cmu.ece.ece551.clicktrack.SubtractiveSynthController;
import edu.cmu.ece.ece551.uis.scale.DiatonicScale;
import edu.cmu.ece.ece551.uis.scale.Scale;
import edu.cmu.ece.ece551.uis.scale.Tonality;

public class SequencerState implements Serializable{

    private static int NUM_BEATS = 16;
    private static int NUM_OCTAVES = 7;

    private int[][] sequences;
    private int currentOctave = 2;
    private int tempo = 100;

    private Scale scale;
    private static InstrumentController instrument = SubtractiveSynthController.getInstance();

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

    public InstrumentController getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentController instrument) {
        this.instrument = instrument;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
        clearGrid();
    }

    public void clearGrid() {
        sequences = new int[NUM_OCTAVES * scale.getNotesPerOctave()][NUM_BEATS];
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        Type myType = new TypeToken<SequencerState>() {}.getType();
        return gson.toJson(this, myType);
    }

}

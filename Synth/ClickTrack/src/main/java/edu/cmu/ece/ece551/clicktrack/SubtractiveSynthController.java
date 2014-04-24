package edu.cmu.ece.ece551.clicktrack;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 3/25/14.
 */
public class SubtractiveSynthController implements InstrumentController {
    protected final String TAG = "ClickTrack_SubSynthController";

    /*
     * Define all our parameters to track
     */
    protected float outputGain;

    protected NativeClickTrack.OscillatorMode osc1mode, osc2mode;
    protected float osc1transposition, osc2transposition;

    protected float attack, decay, sustain, release;

    protected NativeClickTrack.FilterMode filterMode;
    protected float filterCutoff, filterGain, filterQ;

    protected NativeClickTrack.OscillatorMode lfoMode;
    protected float lfoFreq;
    protected float lfoVibratoSteps, lfoTremeloDb;

    /*
     * Constructor and getter for the singleton
     */
    private static SubtractiveSynthController instance = null;
    protected SubtractiveSynthController() {
        Log.i("ClickTrack", "Creating a new SubtractiveSynthController");

        // Set defaults
        outputGain = 0f;

        osc1mode = NativeClickTrack.OscillatorMode.BLEPSAW;
        osc2mode = NativeClickTrack.OscillatorMode.BLEPSAW;

        attack = 0.05f;
        decay = 0.1f;
        sustain = 0.5f;
        release = 0.3f;

        filterMode = NativeClickTrack.FilterMode.LOWPASS;
        filterCutoff = 20000f;
        filterGain = 0.0f;
        filterQ = 5f;

        lfoMode = NativeClickTrack.OscillatorMode.SINE;
        lfoFreq = 5f;
        lfoVibratoSteps = 0.0f;
        lfoTremeloDb = 0.0f;
    }

    public static SubtractiveSynthController getInstance() {
        if(instance == null)
            instance = new SubtractiveSynthController();
        return instance;
    }

    /*
     * Implement the instrument controller interface
     */
    @Override
    public void noteDown(int note, float velocity) {
        NativeClickTrack.SubtractiveSynth.noteDown(note, velocity);
    }

    @Override
    public void noteUp(int note, float velocity) {
        NativeClickTrack.SubtractiveSynth.noteUp(note, velocity);
    }

    /*
     * Getters and setters for our parameters
     */
    public float getOutputGain() {
        return outputGain;
    }

    public void setOutputGain(float outputGain) {
        this.outputGain = outputGain;
        NativeClickTrack.SubtractiveSynth.setGain(outputGain);
    }

    public NativeClickTrack.OscillatorMode getOsc1mode() {
        return osc1mode;
    }

    public void setOsc1mode(NativeClickTrack.OscillatorMode osc1mode) {
        this.osc1mode = osc1mode;
        NativeClickTrack.SubtractiveSynth.setOsc1Mode(osc1mode.value);
    }

    public NativeClickTrack.OscillatorMode getOsc2mode() {
        return osc2mode;
    }

    public void setOsc2mode(NativeClickTrack.OscillatorMode osc2mode) {
        this.osc2mode = osc2mode;
        NativeClickTrack.SubtractiveSynth.setOsc2Mode(osc2mode.value);
    }

    public float getOsc1transposition() {
        return osc1transposition;
    }

    public void setOsc1transposition(float osc1transposition) {
        this.osc1transposition = osc1transposition;
        NativeClickTrack.SubtractiveSynth.setOsc1Transposition(osc1transposition);
    }

    public float getOsc2transposition() {
        return osc2transposition;
    }

    public void setOsc2transposition(float osc2transposition) {
        this.osc2transposition = osc2transposition;
        NativeClickTrack.SubtractiveSynth.setOsc2Transposition(osc2transposition);
    }

    public float getAttack() {
        return attack;
    }

    public void setAttack(float attack) {
        this.attack = attack;
        NativeClickTrack.SubtractiveSynth.setAttackTime(attack);
    }

    public float getDecay() {
        return decay;
    }

    public void setDecay(float decay) {
        this.decay = decay;
        NativeClickTrack.SubtractiveSynth.setDecayTime(decay);
    }

    public float getSustain() {
        return sustain;
    }

    public void setSustain(float sustain) {
        this.sustain = sustain;
        NativeClickTrack.SubtractiveSynth.setSustainLevel(sustain);
    }

    public float getRelease() {
        return release;
    }

    public void setRelease(float release) {
        this.release = release;
        NativeClickTrack.SubtractiveSynth.setReleaseTime(release);
    }

    public NativeClickTrack.FilterMode getFilterMode() {
        return filterMode;
    }

    public void setFilterMode(NativeClickTrack.FilterMode filterMode) {
        this.filterMode = filterMode;
        NativeClickTrack.SubtractiveSynth.setFilterMode(filterMode.value);
    }

    public float getFilterCutoff() {
        return filterCutoff;
    }

    public void setFilterCutoff(float filterCutoff) {
        this.filterCutoff = filterCutoff;
        NativeClickTrack.SubtractiveSynth.setFilterCutoff(filterCutoff);
    }

    public float getFilterGain() {
        return filterGain;
    }

    public void setFilterGain(float filterGain) {
        this.filterGain = filterGain;
        NativeClickTrack.SubtractiveSynth.setFilterGain(filterGain);
    }

    public float getFilterQ() {
        return filterQ;
    }

    public void setFilterQ(float filterQ) {
        this.filterQ = filterQ;
        NativeClickTrack.SubtractiveSynth.setFilterQ(filterQ);
    }

    public NativeClickTrack.OscillatorMode getLfoMode() {
        return lfoMode;
    }

    public void setLfoMode(NativeClickTrack.OscillatorMode lfoMode) {
        this.lfoMode = lfoMode;
        NativeClickTrack.SubtractiveSynth.setLfoMode(lfoMode.value);
    }

    public float getLfoFreq() {
        return lfoFreq;
    }

    public void setLfoFreq(float lfoFreq) {
        this.lfoFreq = lfoFreq;
        NativeClickTrack.SubtractiveSynth.setLfoFreq(lfoFreq);
    }

    public float getLfoVibratoSteps() {
        return lfoVibratoSteps;
    }

    public void setLfoVibratoSteps(float lfoVibratoSteps) {
        this.lfoVibratoSteps = lfoVibratoSteps;
        NativeClickTrack.SubtractiveSynth.setLfoVibrato(lfoVibratoSteps);
    }

    public float getLfoTremeloDb() {
        return lfoTremeloDb;
    }

    public void setLfoTremeloDb(float lfoTremeloDb) {
        this.lfoTremeloDb = lfoTremeloDb;
        NativeClickTrack.SubtractiveSynth.setLfoTremelo(lfoTremeloDb);
    }

    /*
     * Code for serialization. To serialize the object, we convert it to JSON
     */
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void fromString(String s) {
        Gson gson = new Gson();
        Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
        Map<String,String> map = gson.fromJson(s,stringStringMap);

        for (Map.Entry<String,String> entry : map.entrySet())
        {
            String k = entry.getKey();
            String v = entry.getValue();

            if(k.equals("outputGain"))
                setOutputGain(Float.valueOf(v));
            else if(k.equals("osc1mode"))
                setOsc1mode(NativeClickTrack.OscillatorMode.valueOf(v));
            else if(k.equals("osc2mode"))
                setOsc2mode(NativeClickTrack.OscillatorMode.valueOf(v));
            else if(k.equals("osc1transposition"))
                setOsc1transposition(Float.valueOf(v));
            else if(k.equals("osc2transposition"))
                setOsc1transposition(Float.valueOf(v));
            else if(k.equals("attack"))
                setAttack(Float.valueOf(v));
            else if(k.equals("decay"))
                setDecay(Float.valueOf(v));
            else if(k.equals("sustain"))
                setSustain(Float.valueOf(v));
            else if(k.equals("release"))
                setRelease(Float.valueOf(v));
            else if(k.equals("filterMode"))
                setFilterMode(NativeClickTrack.FilterMode.valueOf(v));
            else if(k.equals("filterCutoff"))
                setFilterCutoff(Float.valueOf(v));
            else if(k.equals("filterGain"))
                setFilterGain(Float.valueOf(v));
            else if(k.equals("filterQ"))
                setFilterQ(Float.valueOf(v));
            else if(k.equals("lfoMode"))
                setLfoMode(NativeClickTrack.OscillatorMode.valueOf(v));
            else if(k.equals("lfoFreq"))
                setLfoFreq(Float.valueOf(v));
            else if(k.equals("lfoVibratoSteps"))
                setLfoVibratoSteps(Float.valueOf(v));
            else if(k.equals("lfoTremeloDb"))
                setLfoTremeloDb(Float.valueOf(v));
            else
                Log.d(TAG, "fromString(): ignoring invalid parameter");
        }
    }
}

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
public class FMSynthController implements InstrumentController {
    protected final String TAG = "ClickTrack_SubSynthController";

    /*
     * Define all our parameters to track
     */
    protected float outputGain;

    protected NativeClickTrack.OscillatorMode carrierMode, modulatorMode;
    protected float carrierTransposition, modulatorTransposition;
    protected float modulatorIntensity;

    protected float attack, decay, sustain, release;

    protected NativeClickTrack.FilterMode filterMode;
    protected float filterCutoff, filterGain, filterQ;

    protected NativeClickTrack.OscillatorMode lfoMode;
    protected float lfoFreq;
    protected float lfoVibratoSteps, lfoTremeloDb;

    /*
     * Constructor and getter for the singleton
     */
    private static FMSynthController instance = null;
    protected FMSynthController() {
        Log.i("ClickTrack", "Creating a new FMSynthController");

        // Set defaults
        outputGain = 0f;

        carrierMode = NativeClickTrack.OscillatorMode.SINE;
        modulatorMode = NativeClickTrack.OscillatorMode.SINE;

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

    public static FMSynthController getInstance() {
        if(instance == null)
            instance = new FMSynthController();
        return instance;
    }

    /*
     * Implement the instrument controller interface
     */
    @Override
    public void noteDown(int note, float velocity) {
        NativeClickTrack.FMSynth.noteDown(note, velocity);
    }

    @Override
    public void noteUp(int note, float velocity) {
        NativeClickTrack.FMSynth.noteUp(note, velocity);
    }

    /*
     * Getters and setters for our parameters
     */
    public float getOutputGain() {
        return outputGain;
    }

    public void setOutputGain(float outputGain) {
        this.outputGain = outputGain;
        NativeClickTrack.FMSynth.setGain(outputGain);
    }

    public NativeClickTrack.OscillatorMode getCarrierMode() {
        return carrierMode;
    }

    public void setCarrierMode(NativeClickTrack.OscillatorMode carrierMode) {
        this.carrierMode = carrierMode;
        NativeClickTrack.FMSynth.setCarrierMode(carrierMode.value);
    }

    public NativeClickTrack.OscillatorMode getModulatorMode() {
        return modulatorMode;
    }

    public void setModulatorMode(NativeClickTrack.OscillatorMode modulatorMode) {
        this.modulatorMode = modulatorMode;
        NativeClickTrack.FMSynth.setModulatorMode(modulatorMode.value);
    }

    public float getCarrierTransposition() {
        return carrierTransposition;
    }

    public void setCarrierTransposition(float carrierTransposition) {
        this.carrierTransposition = carrierTransposition;
        NativeClickTrack.FMSynth.setCarrierTransposition(carrierTransposition);
    }

    public float getModulatorTransposition() {
        return modulatorTransposition;
    }

    public void setModulatorTransposition(float modulatorTransposition) {
        this.modulatorTransposition = modulatorTransposition;
        NativeClickTrack.FMSynth.setModulatorTransposition(modulatorTransposition);
    }

    public float getModulatorIntensity() {
        return modulatorIntensity;
    }

    public void setModulatorIntensity(float modulatorIntensity) {
        this.modulatorIntensity = modulatorIntensity;
        NativeClickTrack.FMSynth.setModulatorIntensity(modulatorIntensity);
    }

    public float getAttack() {
        return attack;
    }

    public void setAttack(float attack) {
        this.attack = attack;
        NativeClickTrack.FMSynth.setAttackTime(attack);
    }

    public float getDecay() {
        return decay;
    }

    public void setDecay(float decay) {
        this.decay = decay;
        NativeClickTrack.FMSynth.setDecayTime(decay);
    }

    public float getSustain() {
        return sustain;
    }

    public void setSustain(float sustain) {
        this.sustain = sustain;
        NativeClickTrack.FMSynth.setSustainLevel(sustain);
    }

    public float getRelease() {
        return release;
    }

    public void setRelease(float release) {
        this.release = release;
        NativeClickTrack.FMSynth.setReleaseTime(release);
    }

    public NativeClickTrack.FilterMode getFilterMode() {
        return filterMode;
    }

    public void setFilterMode(NativeClickTrack.FilterMode filterMode) {
        this.filterMode = filterMode;
        NativeClickTrack.FMSynth.setFilterMode(filterMode.value);
    }

    public float getFilterCutoff() {
        return filterCutoff;
    }

    public void setFilterCutoff(float filterCutoff) {
        this.filterCutoff = filterCutoff;
        NativeClickTrack.FMSynth.setFilterCutoff(filterCutoff);
    }

    public float getFilterGain() {
        return filterGain;
    }

    public void setFilterGain(float filterGain) {
        this.filterGain = filterGain;
        NativeClickTrack.FMSynth.setFilterGain(filterGain);
    }

    public float getFilterQ() {
        return filterQ;
    }

    public void setFilterQ(float filterQ) {
        this.filterQ = filterQ;
        NativeClickTrack.FMSynth.setFilterQ(filterQ);
    }

    public NativeClickTrack.OscillatorMode getLfoMode() {
        return lfoMode;
    }

    public void setLfoMode(NativeClickTrack.OscillatorMode lfoMode) {
        this.lfoMode = lfoMode;
        NativeClickTrack.FMSynth.setLfoMode(lfoMode.value);
    }

    public float getLfoFreq() {
        return lfoFreq;
    }

    public void setLfoFreq(float lfoFreq) {
        this.lfoFreq = lfoFreq;
        NativeClickTrack.FMSynth.setLfoFreq(lfoFreq);
    }

    public float getLfoVibratoSteps() {
        return lfoVibratoSteps;
    }

    public void setLfoVibratoSteps(float lfoVibratoSteps) {
        this.lfoVibratoSteps = lfoVibratoSteps;
        NativeClickTrack.FMSynth.setLfoVibrato(lfoVibratoSteps);
    }

    public float getLfoTremeloDb() {
        return lfoTremeloDb;
    }

    public void setLfoTremeloDb(float lfoTremeloDb) {
        this.lfoTremeloDb = lfoTremeloDb;
        NativeClickTrack.FMSynth.setLfoTremelo(lfoTremeloDb);
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
            else if(k.equals("carrierMode"))
                setCarrierMode(NativeClickTrack.OscillatorMode.valueOf(v));
            else if(k.equals("modulatorMode"))
                setModulatorMode(NativeClickTrack.OscillatorMode.valueOf(v));
            else if(k.equals("carrierTransposition"))
                setCarrierTransposition(Float.valueOf(v));
            else if(k.equals("modulatorTransposition"))
                setModulatorTransposition(Float.valueOf(v));
            else if(k.equals("modulatorIntensity"))
                setModulatorIntensity(Float.valueOf(v));
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

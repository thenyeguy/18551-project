package edu.cmu.ece.ece551.clicktrack;

import android.util.Log;

/**
 * Created by michael on 3/25/14.
 */
public class SubtractiveSynthController {
    /*
     * Define all our parameters to track
     */
    protected float outputGain;
    protected NativeClickTrack.SubtractiveSynth.OscillatorMode osc1mode, osc2mode;
    protected float attack, decay, sustain, release;

    /*
     * Constructor and getter for the singleton
     */
    private static SubtractiveSynthController instance = null;
    protected SubtractiveSynthController() {
        Log.d("Controller", "Creating a new controller");
        // Set defaults
        outputGain = 1f;

        osc1mode = NativeClickTrack.SubtractiveSynth.OscillatorMode.SINE;
        osc2mode = NativeClickTrack.SubtractiveSynth.OscillatorMode.SINE;

        attack = 0.05f;
        decay = 0.1f;
        sustain = 0.5f;
        release = 0.3f;
    }

    public static SubtractiveSynthController getInstance() {
        if(instance == null)
            instance = new SubtractiveSynthController();
        return instance;
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

    public NativeClickTrack.SubtractiveSynth.OscillatorMode getOsc1mode() {
        return osc1mode;
    }

    public void setOsc1mode(NativeClickTrack.SubtractiveSynth.OscillatorMode osc1mode) {
        this.osc1mode = osc1mode;
        NativeClickTrack.SubtractiveSynth.setOsc1Mode(osc1mode.value);
    }

    public NativeClickTrack.SubtractiveSynth.OscillatorMode getOsc2mode() {
        return osc2mode;
    }

    public void setOsc2mode(NativeClickTrack.SubtractiveSynth.OscillatorMode osc2mode) {
        this.osc2mode = osc2mode;
        NativeClickTrack.SubtractiveSynth.setOsc2Mode(osc2mode.value);
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
}
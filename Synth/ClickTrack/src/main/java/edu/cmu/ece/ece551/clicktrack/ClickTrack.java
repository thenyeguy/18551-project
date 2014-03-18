package edu.cmu.ece.ece551.clicktrack;

import android.util.Log;

/**
 * The ClickTrack is a Java wrapper into the native libclicktrack. It contains only static
 * methods, and is used as a container for the native functions.
 *
 * All native functions reference a singleton element in C++ that contains the signal chain
 */
public class ClickTrack {
    /* First the library must be loaded
     */
    public static void loadLibray(){
        // Load the library
        Log.i("LibClickTrack", "Loading libclicktrack native library...");
        System.loadLibrary("clicktrack");
        Log.i("LibClickTrack", "libclicktrack loaded.");
    }

    /*
     * This function will trigger audio playback in a separate thread. It gracefully handles
     * redundant play commands.
     */
    public static native void play();

    /*
     * This function will pause the audio playback. It gracefully handles redundant pause commands.
     */
    public static native void pause();

    /*
     * These functions will start/stop the processing backend entirely.
     *
     * Note that the library is not started by default
     * Note that unless the library is stopped, OpenSL ES will hold a wakelock
     */
    public static native void start();
    public static native void stop();

    /*
     * These functions will set the gain on our different components
     */
    public static native void setMicGain(float gain);
    public static native void setOscGain(float gain);


    /*
     * This class provides a clean Java interface for the ClickTrack subtractive synthesizer
     */
    public static class SubtractiveSynth {
        /* Note events
         */
        public static native void noteDown(int note, float velocity);
        public static native void noteUp(int note, float velocity);

        /* Oscillator modes. Please use the provided constants only
         */
        public static final int SINE = 0;
        public static final int SAW = 1;
        public static final int SQUARE = 2;
        public static final int TRI = 3;
        public static final int WHITENOISE = 4;
        public static final int BLEPSAW = 5;
        public static final int BLEPSQUARE = 6;
        public static final int BLEPTRI = 7;
        public static native void setOsc1Mode(int mode);
        public static native void setOsc2Mode(int mode);

        /* ADSR envelope
         */
        public static native void set_attack_time(float attack_time);
        public static native void set_decay_time(float decay_time);
        public static native void set_sustain_level(float sustain_level);
        public static native void set_release_time(float release_time);

        /* A 4 point equalizer as follows:
         *
         *     Point 1: Either a lowpass(12dB or 24dB/octave) or a low shelf
         *     Points 2&3: Peak filters with adjustable Q
         *     Point 4: Either a highpass(12dB or 24dB/octae) or a high shelf
         *
         * Please use provided constants for the modes on points 1 & 4
         */
        public static final int PASS12DB = 0;
        public static final int PASS24DB = 1;
        public static final int SHELF = 2;

        public static native void setPoint1(int mode, float cutoff, float gain);
        public static native void setPoint4(int mode, float cutoff, float gain);

        public static native void setPoint2(float cutoff, float Q, float gain);
        public static native void setPoint3(float cutoff, float Q, float gain);

        /* Output gain of the synth
         */
        public static native void setGain(float gain);
    }
}

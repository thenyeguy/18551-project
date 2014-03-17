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
        public static native void setGain(float gain);
        public static native void noteDown(int note, float velocity);
        public static native void noteUp(int note, float velocity);
    }
}

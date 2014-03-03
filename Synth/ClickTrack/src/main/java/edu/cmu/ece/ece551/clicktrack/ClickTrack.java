package edu.cmu.ece.ece551.clicktrack;

import android.util.Log;

/**
 * The ClickTrack is a Java wrapper into the native libclicktrack
 */
public class ClickTrack {
    /*
     * This function will trigger audio playback in a separate thread. It gracefully handles
     * redundant play commands.
     */
    private native void nativePlay(long obj);
    public void play() {
        nativePlay(master);
    }

    /*
     * This function will pause the audio playback. It gracefully handles redundant pause commands.
     */
    private native void nativePause(long obj);
    public void pause() {
        nativePause(master);
    }

    /*
     * These functions will start/stop the processing backend entirely.
     *
     * Note that the library is not started by default
     * Note that unless the library is stopped, OpenSL ES will hold a wakelock
     */
    private native void nativeStart(long obj);
    public void start() {
        nativeStart(master);
    }

    private native void nativeStop(long obj);
    public void stop() {
        nativeStop(master);
    }

    /*
     * These functions will set the gain on our different components
     */
    private native void nativeMicSetGain(long obj, float gain);
    public void setMicGain(float gain) {
        nativeMicSetGain(master, gain);
    }

    private native void nativeOscSetGain(long obj, float gain);
    public void setOscGain(float gain) {
        nativeOscSetGain(master, gain);
    }


    /*
     * This class provides a clean Java interface for the ClickTrack subtractive synthesizer
     */
    public SubtractiveSynth subtractiveSynth;

    private native void nativeSubtractiveSynthSetGain(long obj, float gain);
    private native void nativeSubtractiveSynthNoteDown(long obj, int note, float velocity);
    private native void nativeSubtractiveSynthNoteUp(long obj, int note, float velocity);

    public class SubtractiveSynth {
        public void setGain(float gain) {
            nativeSubtractiveSynthSetGain(master, gain);
        }

        public void noteDown(int note, float velocity) {
            nativeSubtractiveSynthNoteDown(master, note, velocity);
        }

        public void noteUp(int note, float velocity) {
            nativeSubtractiveSynthNoteUp(master, note, velocity);
        }
    }

    /*
     * During initialization, we load the native library and initialize the C++ manager class,
     * storing its pointer as a long.
     */
    private native long nativeInitClickTrackMaster();
    private long master;
    public ClickTrack() {
        // Load the library
        Log.i("LibClickTrack", "Loading libclicktrack native library...");
        System.loadLibrary("clicktrack");
        Log.i("LibClickTrack", "libclicktrack loaded.");

        // Create our master object
        master = nativeInitClickTrackMaster();

        subtractiveSynth = new SubtractiveSynth();
    }

    /*
     * During object destruction, we must explicitly free the C++ master class
     */
    private native void nativeFreeClickTrackMaster(long obj);
    @Override
    protected void finalize() throws Throwable {
        // Free our native master object
        nativeFreeClickTrackMaster(master);
        super.finalize();
    }

}

package edu.cmu.ece.ece551.clicktrack;

import android.util.Log;

/**
 * The ClickTrack is a Java wrapper into the native libclicktrack
 */
public class ClickTrack {
    public ClickTrack()
    {
        // Load the library
        Log.i("LibClickTrack", "Loading libclicktrack native library...");
        System.loadLibrary("clicktrack");
        Log.i("LibClickTrack", "libclicktrack loaded.");

        // Create our master object
        master = initClickTrackMaster();
    }

    @Override
    protected void finalize() throws Throwable
    {
        // Free our native master object
        freeClickTrackMaster(master);

        super.finalize();
    }

    /*
     * This function will trigger audio playback in a separate thread. It gracefully handles
     * redundant play commands.
     */
    public void play()
    {
        Log.i("LibClickTrack", "Playing audio.");
        ClickTrackMasterPlay(master);
    }

    /*
     * This function will pause the audio playback. It gracefully handles redundant pause commands.
     */
    public void pause()
    {
        Log.i("LibClickTrack", "Pausing audio.");
        ClickTrackMasterPause(master);
    }


    /* This is our pointer to the native C++ object
     */
    private long master;

    /*
     * The following bindings are wrappers for the C++ ClickTrackMaster class. They all
     * deal with a long type. This is the pointer to the master class and cast to
     * a long. This long is casted back to a pointer within the C++ code to access our object.
     */

    /* This function will create a new ClickTrackMaster object and return
     * a pointer to it.
     */
    private native long initClickTrackMaster();

    /* This frees the ClickTrackMaster object passed into it
     */
    private native void freeClickTrackMaster(long obj);

    /* Tells the library to begin playing audio. This call spawns another thread and returns after
     */
    private native void ClickTrackMasterPlay(long obj);

    /* Tells the library to stop playing audio.
     */
    private native void ClickTrackMasterPause(long obj);
}

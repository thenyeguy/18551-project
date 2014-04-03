package edu.cmu.ece.ece551.clicktrack;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The NativeClickTrack is a Java wrapper into the native libclicktrack. It contains only static
 * methods, and is used as a container for the native functions.
 *
 * All native functions reference a singleton element in C++ that contains the signal chain
 */
public class NativeClickTrack {
    protected static final String TAG = "LibClickTrack";
    /*
     * First the library must be loaded
     */
    private static boolean loaded = false;
    private static native void setBufferSize(int bufferSize);
    public static void loadLibray(){
        // Check for loaded
        if(loaded)
            return;
        loaded = true;

        // Load the library
        Log.i(TAG, "Loading libclicktrack native library...");
        System.loadLibrary("clicktrack");

        // Get the buffer size
        int bufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        NativeClickTrack.setBufferSize(bufferSize);
        Log.i(TAG, "libclicktrack loaded with buffer size: " + bufferSize);
    }

    /*
     * Reference counting for the library, to shut down the sound when no activity is using it
     */
    private static int references = 0;
    public static void addReference() {
        // If this is a first reference, restart playback
        if(references == 0) {
            start();
            play();
        }

        // Update the reference count
        references++;
        Log.i(TAG, "Reference added, now at: " + references);
    }
    public static void removeReference() {
        // Update the reference count
        references--;
        Log.i(TAG, "Reference removed, now at: " + references);

        // Stop audio in one second
        if(references == 0) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    checkForEnd();
                }
            }, 1000);
        }
    }
    private static void checkForEnd() {
        if(references == 0) {
            pause();
            stop();
            Log.i(TAG, "Shutting down audio playback");
        }
    }

    /*
     * Convenience function to between dB and amplitude
     */
    public static float dbToAmplitude(float db) {
        return (float)Math.pow(10, db/10);
    }
    public static float amplitudeToDb(float amp) {
        return (float)(10*Math.log10(amp));
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
     * This class provides a clean Java interface for the master channel reverb
     */
    public static class Reverb {
        public static native void setRevTime(float revTime);
        public static native void setGain(float gain);
        public static native void setWetness(float wetness);
    }


    /*
     * This class provides a clean Java interface for the NativeClickTrack subtractive synthesizer
     */
    public static class SubtractiveSynth {
        /* Note events
         */
        public static native void noteDown(int note, float velocity);
        public static native void noteUp(int note, float velocity);

        /* Oscillator modes. Please use the provided enum constants
         */
        public enum OscillatorMode {
            SINE(0), SAW(1), SQUARE(2), TRI(3), WHITENOISE(4), BLEPSAW(5), BLEPSQUARE(6), BLEPTRI(7);

            public int value;
            private OscillatorMode(int value) {
                this.value = value;
            }
        }
        public static native void setOsc1Mode(int mode);
        public static native void setOsc2Mode(int mode);

        /* Oscillator transpositions. Takes a number of steps; these can be fractional
         */
        public static native void setOsc1Transposition(float steps);
        public static native void setOsc2Transposition(float steps);

        /* ADSR envelope
         */
        public static native void setAttackTime(float attack_time);
        public static native void setDecayTime(float decay_time);
        public static native void setSustainLevel(float sustain_level);
        public static native void setReleaseTime(float release_time);

        /* Change the filter's mode and coefficients. Please use the provided enum contants.
         *
         * Low/high pass filters use only the cutoff
         *
         * Shelf filters ignore the Q factor
         *
         * Peak filters place a peak with a gain at the cutoff, and no change everywhere else.
         * Q determines how sharp the peak is
         *
         * Cutoff is in Hz, gain is in dB
         */
        public enum FilterMode {
            LOWPASS(0), LOWSHELF(1), HIGHPASS(2), HIGHSHELF(3), PEAK(4);

            public int value;
            private FilterMode(int value) {
                this.value = value;
            }
        }
        public static native void setFilterMode(int mode);
        public static native void setFilterCutoff(float cutoff);
        public static native void setFilterGain(float gain);
        public static native void setFilterQ(float q);

        /* LFO settings
         *
         * The mode is the same set of mode available to oscillators.
         *
         * Vibrato is measured in the number of steps to waver (can be fractional)
         *
         * Tremelo is measured in the maximum +/- gain in decibels
         */
        public static native void setLfoMode(int mode);
        public static native void setLfoFreq(float freq);
        public static native void setLfoVibrato(float steps);
        public static native void setLfoTremelo(float db);

        /* Output gain of the synth
         */
        public static native void setGain(float gain);
    }


    /*
     * This class provides a clean Java interface for the NativeClickTrack subtractive synthesizer
     */
    public static class DrumMachine {
        /* Note down to trigger. The drum machine follows the standard MIDI numbering for drums.
         * Note that a voice may not contain all drum types
         *
         * The following enum gives a list of the standard MIDI drum note mappings
         */
        public enum DrumNotes {
            BASSDRUM2(35), BASSDRUM1(36), RIMSHOT(37), SNAREDRUM1(38), HANDCLAP(39),
            SNAREDRUM2(40), LOWTOM2(41), CLOSEDHIHAT(42), LOWTOM1(43), PEDALHIHAT(44), MIDTOM2(45),
            OPENHIHAT(46), MIDTOM1(47), HIGHTOM2(48), CRASHCYMBAL1(49), HIGHTOM1(50),
            RIDECYMBAL1(51), CHINESECYMBAL(52), RIDEBELL(53), TAMBOURINE(54), SPLASHCYMBAL(55),
            COWBELL(56), CRASHCYMBAL2(57), VIBRASLAP(58), RIDECYMBAL2(59), HIGHBONGO(60),
            LOWBONGO(61), MUTEHIGHCONGA(62), OPENHIGHCONGA(63), LOWCONGA(64), HIGHTIMBALE(65),
            LOWTIMBALE(66), HIGHAGOGO(67), LOWAGOGO(68), CABASA(69), MARACAS(70), SHORTWHISTLE(71),
            LONGWHISTLE(72), SHORTGUIRO(73), LONGGUIRO(74), CLAVES(75), HIGHWOODBLOCK(76),
            LOWWOODBLOCK(77), MUTECUICA(78), OPENCUICA(79), MUTETRIANGLE(80), OPENTRIANGLE(81);

            public int value;
            private DrumNotes(int value) {
                this.value = value;
            }
        }
        public static native void noteDown(int note, float velocity);

        /* Output gain of the machine
         */
        public static native void setGain(float gain);

        /* Tells the drum machine to load a set of voices in the specified path. The path must
         * contain a keymap.txt that outlines the noises for each note. See the NativeClickTrack
         * documentation for further details
         */
        public static native void setVoice(String path);
    }
}

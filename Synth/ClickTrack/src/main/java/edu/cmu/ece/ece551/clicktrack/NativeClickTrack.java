package edu.cmu.ece.ece551.clicktrack;

import android.util.Log;

/**
 * The NativeClickTrack is a Java wrapper into the native libclicktrack. It contains only static
 * methods, and is used as a container for the native functions.
 *
 * All native functions reference a singleton element in C++ that contains the signal chain
 */
public class NativeClickTrack {
    /*
     * First the library must be loaded
     */
    public static void loadLibray(){
        // Load the library
        Log.i("LibClickTrack", "Loading libclicktrack native library...");
        System.loadLibrary("clicktrack");
        Log.i("LibClickTrack", "libclicktrack loaded.");
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

        /* Oscillator modes. Please use the provided constants only
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

        /* ADSR envelope
         */
        public static native void setAttackTime(float attack_time);
        public static native void setDecayTime(float decay_time);
        public static native void setSustainLevel(float sustain_level);
        public static native void setReleaseTime(float release_time);

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

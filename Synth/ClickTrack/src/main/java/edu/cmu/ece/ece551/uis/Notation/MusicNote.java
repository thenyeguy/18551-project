package edu.cmu.ece.ece551.uis.Notation;

/**
 * Created by michaelryan on 3/30/14.
 */
public enum MusicNote {

    A(33, "A"),
    A_SHARP(34, "A#"),
    B(35, "B"),
    C(24, "C"),
    C_SHARP(25, "C#"),
    D(26, "D"),
    D_SHARP(27, "D#"),
    E(28, "E"),
    F(29, "F"),
    F_SHARP(30, "F#"),
    G(31, "G"),
    G_SHARP(32, "G#"),

    BassDrum(36, "BassDrum"),
    SnareDrum(38, "SnareDrum"),
    ClsdHihat(42, "ClsdHihat"),
    LowTom(45, "LowTom"),
    OpenHihat(46, "OpenHihat"),
    MidTom(47, "MidTom"),
    CYmbal(49, "CYmbal"),
    HiTom(50, "HiTom");


    private static MusicNote[] goodVals = {A, A_SHARP, B, C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP};

    public int midi;
    public String english;

    private MusicNote(int midi, String english) {
        this.midi = midi;
        this.english = english;
    }

    @Override
    public String toString() {
        return english;
    }

    public static MusicNote getFromString(String english) {
        if (english.equals("C"))
            return C;
        else if (english.equals("C#"))
            return C_SHARP;
        else if (english.equals("D"))
            return D;
        else if (english.equals("D#"))
            return D_SHARP;
        else if (english.equals("E"))
            return E;
        else if (english.equals("F"))
            return F;
        else if (english.equals("F#"))
            return F_SHARP;
        else if (english.equals("G"))
            return G;
        else if (english.equals("G#"))
            return G_SHARP;
        else if (english.equals("A"))
            return A;
        else if (english.equals("A#"))
            return A_SHARP;
        else if (english.equals("B"))
            return B;

        return C;
    }

    public static MusicNote[] myValues() {
        return goodVals;
    }
}

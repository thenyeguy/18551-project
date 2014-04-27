package edu.cmu.ece.ece551.uis.scale;

import com.google.common.collect.Lists;

import java.util.List;

import edu.cmu.ece.ece551.uis.Notation.Interval;
import edu.cmu.ece.ece551.uis.Notation.MusicNote;

public class DrumScale implements Scale{

    // 36, 38, 42, 45, 46, 47, 49, 50

    private static MusicNote[] notes = {MusicNote.BassDrum, MusicNote.SnareDrum,
            MusicNote.ClsdHihat, MusicNote.LowTom, MusicNote.OpenHihat, MusicNote.MidTom,
            MusicNote.CYmbal, MusicNote.HiTom};

    private static Interval[] ints = {Interval.FOURDRUM, Interval.FOURDRUM,
            Interval.THREETONES, Interval.SEMITONE, Interval.TONE, Interval.SEMITONE };

    @Override
    public int getNotesPerOctave() {
        return 8;
    }

    @Override
    public List<MusicNote> getNoteNames() {

        return Lists.newArrayList(notes);
    }
}

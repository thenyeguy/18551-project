package edu.cmu.ece.ece551.uis;

import java.util.List;

/**
 * Created by michaelryan on 3/26/14.
 */
public class BluesScale extends AbstractScale {

    private static Interval[] majInts = {Interval.TONE, Interval.SEMITONE, Interval.SEMITONE,
            Interval.THREETONES, Interval.TONE, Interval.THREETONES};
    private static Interval[] minInts = {Interval.THREETONES,
            Interval.TONE, Interval.SEMITONE, Interval.SEMITONE, Interval.THREETONES,
            Interval.TONE};



    public BluesScale(String startingNote, Tonality tonality) {
        this.tonality = tonality;
        this.tonic = startingNote;
        if (tonality == Tonality.MINOR)
            intervals = minInts;
        else {
            intervals = majInts;
        }
    }

}

package edu.cmu.ece.ece551.uis;

import java.util.List;

/**
 * Created by michaelryan on 3/25/14.
 */
public class DiatonicScale extends AbstractScale{

    private static Interval[] majInts = {Interval.TONE, Interval.TONE, Interval.SEMITONE,
                                        Interval.TONE, Interval.TONE, Interval.TONE,
                                        Interval.SEMITONE};
    private static Interval[] minInts = {Interval.TONE,
            Interval.SEMITONE, Interval.TONE, Interval.TONE, Interval.SEMITONE,
            Interval.TONE, Interval.TONE};


    public DiatonicScale(String startingNote, Tonality tonality) {
        this.tonality = tonality;
        this.tonic = startingNote;
        if (tonality == Tonality.MINOR)
            intervals = minInts;
        else {
            intervals = majInts;
        }
    }

}
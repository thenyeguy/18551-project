package edu.cmu.ece.ece551.uis.scale;

import edu.cmu.ece.ece551.uis.Notation.Interval;

/**
 * Created by michaelryan on 3/28/14.
 */
public class PentatonicScale extends AbstractScale {

    private static Interval[] majInts = {Interval.TONE, Interval.TONE,
            Interval.THREETONES, Interval.TONE, Interval.THREETONES};
    private static Interval[] minInts = {Interval.THREETONES,
            Interval.TONE, Interval.TONE, Interval.THREETONES,
            Interval.TONE};



    public PentatonicScale(String startingNote, Tonality tonality) {
        this.tonality = tonality;
        this.tonic = startingNote;
        if (tonality == Tonality.MINOR)
            intervals = minInts;
        else {
            intervals = majInts;
        }
    }
}

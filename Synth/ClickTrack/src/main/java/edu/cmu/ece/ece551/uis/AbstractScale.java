package edu.cmu.ece.ece551.uis;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by michaelryan on 3/25/14.
 */
public abstract class AbstractScale implements Scale {

    private static final MusicNote[] noteNames = MusicNote.values();
    protected Interval[] intervals;
    protected Tonality tonality;
    protected String tonic;
    private List<MusicNote> notes = null;


    protected List<MusicNote> getNotes(String tonic, Interval[] intervals) {
        List<MusicNote> result = Lists.newArrayList();

        if (notes == null) {

            int idx = Arrays.binarySearch(noteNames, MusicNote.getFromString(tonic));


            result.add(noteNames[idx]);

            for (int i = 0; i < intervals.length; i++) {
                Interval ival = intervals[i];

                switch (ival) {
                    case SEMITONE:
                        idx += 1;
                        break;
                    case TONE:
                        idx += 2;
                        break;
                    case THREETONES:
                        idx += 3;
                        break;
                }

                // What about octaves...?
                idx = idx % noteNames.length;
                result.add(noteNames[idx]);

            }

            notes = result;
        }

        return notes;
    }


    @Override
    public int getNotesPerOctave() {
        return intervals.length;
    }

    @Override
    public List<MusicNote> getNoteNames() {
        return getNotes(tonic, intervals);
    }

}

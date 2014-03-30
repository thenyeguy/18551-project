package edu.cmu.ece.ece551.uis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by michaelryan on 3/25/14.
 */
public abstract class AbstractScale implements Scale {

    private static final String[] noteNames = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    protected Interval[] intervals;
    protected Tonality tonality;
    protected String tonic;
    private List<String> notes = null;


    protected List<String> getNotes(String tonic, Interval[] intervals) {
        List<String> result = new ArrayList<String>();

        if (notes == null) {

            int idx = Arrays.binarySearch(noteNames, tonic);

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
    public List<String> getNoteNames() {
        return getNotes(tonic, intervals);
    }

}

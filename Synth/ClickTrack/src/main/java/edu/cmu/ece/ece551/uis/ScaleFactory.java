package edu.cmu.ece.ece551.uis;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by michaelryan on 3/26/14.
 */
public class ScaleFactory {


    private static final String[] noteNames = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};


    public static Scale createScale(ScaleType type, String startingNote) {


        int notesPerOctave;
        List<String> noteNames;


        Scale result = new Scale() {
            @Override
            public int getNotesPerOctave() {
                return 0;
            }

            @Override
            public List<String> getNoteNames() {
                return null;
            }
        };

        return result;
    }

    private List<String> getNotes(String tonic, Interval[] intervals){
        List<String> result = new ArrayList<String>();

        int idx = Arrays.binarySearch(noteNames, tonic);

        Log.d("scale", "Length: " + intervals.length);
        result.add(noteNames[idx]);

        for (int i = 0; i < intervals.length; i++) {
            Interval ival = intervals[i];

            switch (ival) {
                case SEMITONE:
                    idx += 1;
                    break;
                case TONE:
                    idx +=  2;
                    break;
                case THREETONES:
                    idx += 3;
                    break;
            }

            // What about octaves...?
            idx = idx % noteNames.length;

            result.add(noteNames[idx]);

        }

        Log.d("scale", result.toString());
        return result;
    }



}

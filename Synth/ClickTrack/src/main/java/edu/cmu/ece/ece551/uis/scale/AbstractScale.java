package edu.cmu.ece.ece551.uis.scale;

import android.util.Log;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import edu.cmu.ece.ece551.uis.Notation.Interval;
import edu.cmu.ece.ece551.uis.Notation.MusicNote;

/**
 * Created by michaelryan on 3/25/14.
 */
public abstract class AbstractScale implements Scale {

    private static final MusicNote[] noteNames = MusicNote.myValues();
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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }


    public void fromString(String s) {
        Gson gson = new Gson();
        Type stringStringMap = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(s, stringStringMap);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();

            if (k.equals("intervals")) {
                intervals = gson.fromJson(v, Interval[].class);
            } else if (k.equals("tonality")) {
                tonality = Tonality.valueOf(v);
            } else if (k.equals("tonic")) {
                tonic = v;
            } else if (k.equals("notes")) {
                notes = gson.fromJson(v, ArrayList.class);
            } else {
                Log.d("scale", "Ignoring invalid parameter" + k);
            }

        }
    }
}
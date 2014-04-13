package edu.cmu.ece.ece551.uis.scale;


import java.util.List;

import edu.cmu.ece.ece551.uis.Notation.MusicNote;

public interface Scale {

    public int getNotesPerOctave();

    public List<MusicNote> getNoteNames();

}

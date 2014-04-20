package edu.cmu.ece.ece551.uis.scale;


import java.io.Serializable;
import java.util.List;

import edu.cmu.ece.ece551.uis.Notation.MusicNote;

public interface Scale extends Serializable {

    public int getNotesPerOctave();

    public List<MusicNote> getNoteNames();

}

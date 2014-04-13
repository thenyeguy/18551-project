package edu.cmu.ece.ece551.uis.scale;

/**
 * Created by michaelryan on 3/26/14.
 */
public enum ScaleType {
    DIATONIC_MAJOR("Major Diatonic"),
    DIATONIC_MINOR("Minor Diatonic"),
    MAJOR_BLUES("Major Blues"),
    MINOR_BLUES("Minor Blues"),
    MAJOR_PENTATONIC("Major Pentatonic"),
    MINOR_PENTATONIC("Minor Pentatonic");

    private String englishName;

    private ScaleType(String name) {
        englishName = name;
    }


    @Override
    public String toString() {
        return englishName;
    }


}

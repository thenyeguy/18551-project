package edu.cmu.ece.ece551.clicktrack;

/**
 * Created by michael on 3/30/14.
 */
public interface InstrumentController {
    public void noteDown(int note, float velocity);
    public void noteUp(int note, float velocity);
}

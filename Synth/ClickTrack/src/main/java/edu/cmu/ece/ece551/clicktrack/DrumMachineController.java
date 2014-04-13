package edu.cmu.ece.ece551.clicktrack;

/**
 * Created by michael on 4/13/14.
 */
public class DrumMachineController implements InstrumentController {
    @Override
    public void noteDown(int note, float velocity) {
        NativeClickTrack.DrumMachine.noteDown(note, velocity);
    }

    @Override
    public void noteUp(int note, float velocity) {}
}

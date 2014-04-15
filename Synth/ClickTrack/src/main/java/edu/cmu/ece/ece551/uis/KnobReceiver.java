package edu.cmu.ece.ece551.uis;

/**
 * Created by michael on 4/13/14.
 * A KnobReciver has to handle states changes on the knob it is assigned to.
 */
public interface KnobReceiver {
    /*
     * onKnobChange is called when a knob value changes, and it must handle the value change. The
     * passed in value will be between 0.0 and 1.0
     */
    public void onKnobChange(float value);

    /*
     * formatValue should convert the value to a string representation
     */
    public String formatValue(float value);

    /* Given the value your knob is displaying, convert it back to range 0.0 to 1.0
     */
    public float getValue(float value);
}

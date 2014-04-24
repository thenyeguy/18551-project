package edu.cmu.ece.ece551.clicktrack;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by michael on 4/13/14.
 */
public class DrumMachineController implements InstrumentController {
    protected final String TAG = "ClickTrack_DrumMachineController";

    float outputGain;

    float ringFreq;
    float ringWetness;

    float compressionThreshold;
    float compressionRatio;

    /*
     * Constructor and getter for the singleton
     */
    private static DrumMachineController instance = null;
    protected DrumMachineController() {
        Log.i("ClickTrack", "Creating a new DrumMachineController");

        // Set defaults
        outputGain = 0f;

        ringFreq = 1;
        ringWetness = 0;

        compressionThreshold = 0;
        compressionRatio = 1;
    }

    public static DrumMachineController getInstance() {
        if(instance == null)
            instance = new DrumMachineController();
        return instance;
    }

    @Override
    public void noteDown(int note, float velocity) {
        NativeClickTrack.DrumMachine.noteDown(note, velocity);
    }

    @Override
    public void noteUp(int note, float velocity) {}

    public float getOutputGain() {
        return outputGain;
    }

    public void setOutputGain(float outputGain) {
        this.outputGain = outputGain;
        NativeClickTrack.DrumMachine.setGain(outputGain);
    }


    public float getRingFreq() {
        return ringFreq;
    }

    public void setRingFreq(float ringFreq) {
        this.ringFreq = ringFreq;
        NativeClickTrack.DrumMachine.setRingFreq(ringFreq);
    }

    public float getRingWetness() {
        return ringWetness;
    }

    public void setRingWetness(float ringWetness) {
        this.ringWetness = ringWetness;
        NativeClickTrack.DrumMachine.setRingWetness(ringWetness);
    }

    public float getCompressionThreshold() {
        return compressionThreshold;
    }

    public void setCompressionThreshold(float compressionThreshold) {
        this.compressionThreshold = compressionThreshold;
        NativeClickTrack.DrumMachine.setCompressionThreshold(compressionThreshold);
    }

    public float getCompressionRatio() {
        return compressionRatio;
    }

    public void setCompressionRatio(float compressionRatio) {
        this.compressionRatio = compressionRatio;
        NativeClickTrack.DrumMachine.setCompressionRatio(compressionRatio);
    }

    /*
     * Code for serialization. To serialize the object, we convert it to JSON
     */
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void fromString(String s) {
        Gson gson = new Gson();
        Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
        Map<String,String> map = gson.fromJson(s,stringStringMap);

        for (Map.Entry<String,String> entry : map.entrySet())
        {
            String k = entry.getKey();
            String v = entry.getValue();

            if(k.equals("outputGain"))
                setOutputGain(Float.valueOf(v));
            else if(k.equals("ringFreq"))
                setRingFreq(Float.valueOf(v));
            else if(k.equals("ringWetness"))
                setRingWetness(Float.valueOf(v));
            else if(k.equals("compressionRatio"))
                setCompressionRatio(Float.valueOf(v));
            else if(k.equals("compressionThreshold"))
                setCompressionThreshold(Float.valueOf(v));
            else
                Log.d(TAG, "fromString(): ignoring invalid parameter");
        }
    }
}

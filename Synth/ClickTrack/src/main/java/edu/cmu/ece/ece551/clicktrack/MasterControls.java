package edu.cmu.ece.ece551.clicktrack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;

import edu.cmu.ece.ece551.synth.R;
import edu.cmu.ece.ece551.uis.KnobReceiver;
import edu.cmu.ece.ece551.uis.KnobView;

public class MasterControls extends Fragment {
    /* EQ state
     */
    private KnobView lowCutoffKnob, midCutoffKnob, midGainKnob, midQKnob, highCutoffKnob;
    private static float lowCutoff = 20;
    private static float midCutoff = 2000;
    private static float midGain = 0.0f;
    private static float midQ = 1.0f;
    private static float highCutoff = 20000;

    /* Reverb state
     */
    private KnobView reverbGainKnob, reverbWetnessKnob, reverbTimeKnob;
    private static float reverbGain = 0.0f;
    private static float reverbWetness = 0.0f;
    private static float reverbTime = 1.0f;

    /* Limiter state
     */
    private KnobView limiterGainKnob, limiterThresholdKnob;
    private static float limiterGain = 0.0f;
    private static float limiterThreshold = 0.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master_controls, container, false);

        // Configure EQ knobs
        lowCutoffKnob = (KnobView) rootView.findViewById(R.id.eqLowCutoffKnob);
        lowCutoffKnob.setName("Cutoff");
        lowCutoffKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return ((float) Math.pow(10, value) - 1) / 9 * 19980 + 20;
            }

            @Override
            public void onKnobChange(float value) {
                lowCutoff = adjustValue(value);
                NativeClickTrack.Equalizer.setLowCutoff(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "Hz";
            }

            @Override
            public float getValue(float value) {
                return (float) Math.log10((value - 20) / 19980 * 9 + 1);
            }
        });

        highCutoffKnob = (KnobView) rootView.findViewById(R.id.eqHighCutoffKnob);
        highCutoffKnob.setName("Cutoff");
        highCutoffKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return ((float) Math.pow(10, value) - 1) / 9 * 19980 + 20;
            }

            @Override
            public void onKnobChange(float value) {
                highCutoff = adjustValue(value);
                NativeClickTrack.Equalizer.setHighCutoff(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "Hz";
            }

            @Override
            public float getValue(float value) {
                return (float) Math.log10((value - 20) / 19980 * 9 + 1);
            }
        });

        midCutoffKnob = (KnobView) rootView.findViewById(R.id.eqPeakCutoffKnob);
        midCutoffKnob.setName("Cutoff");
        midCutoffKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return ((float) Math.pow(10, value) - 1) / 9 * 19980 + 20;
            }

            @Override
            public void onKnobChange(float value) {
                midCutoff = adjustValue(value);
                NativeClickTrack.Equalizer.setMidCutoff(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "Hz";
            }

            @Override
            public float getValue(float value) {
                return (float) Math.log10((value - 20) / 19980 * 9 + 1);
            }
        });

        midGainKnob = (KnobView) rootView.findViewById(R.id.eqPeakGainKnob);
        midGainKnob.setName("Gain");
        midGainKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");

            private float adjustValue(float value) {
                return (2 * value - 1) * 20;
            }

            @Override
            public void onKnobChange(float value) {
                midGain = adjustValue(value);
                NativeClickTrack.Equalizer.setMidGain(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "dB";
            }

            @Override
            public float getValue(float value) {
                return (value / 20 + 1) / 2;
            }
        });

        midQKnob = (KnobView) rootView.findViewById(R.id.eqPeakQKnob);
        midQKnob.setName("Q");
        midQKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");

            private float adjustValue(float value) {
                return value * 9.5f + 0.5f;
            }

            @Override
            public void onKnobChange(float value) {
                midQ = adjustValue(value);
                NativeClickTrack.Equalizer.setMidQ(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value));
            }

            @Override
            public float getValue(float value) {
                return (value - 0.5f) / 9.5f;
            }
        });

        // Configure reverb knobs
        reverbGainKnob = (KnobView) rootView.findViewById(R.id.reverbGainKnob);
        reverbGainKnob.setName("Gain");
        reverbGainKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");
            private float adjustValue(float value) {
                return (value - 1.0f) * 20f;
            }

            @Override
            public void onKnobChange(float value) {
                reverbGain = adjustValue(value);
                NativeClickTrack.Reverb.setGain(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "dB";
            }

            @Override
            public float getValue(float value) {
                return value/20 + 1;
            }
        });

        reverbWetnessKnob = (KnobView) rootView.findViewById(R.id.reverbWetnessKnob);
        reverbWetnessKnob.setName("Wetness");
        reverbWetnessKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.00");

            @Override
            public void onKnobChange(float value) {
                reverbWetness = (value);
                NativeClickTrack.Reverb.setWetness(value);
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(value);
            }

            @Override
            public float getValue(float value) {
                return value;
            }
        });

        reverbTimeKnob = (KnobView) rootView.findViewById(R.id.reverbTimeKnob);
        reverbTimeKnob.setName("Decay");
        reverbTimeKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");
            private float adjustValue(float value) {
                return value * 3;
            }

            @Override
            public void onKnobChange(float value) {
                reverbTime = adjustValue(value);
                NativeClickTrack.Reverb.setRevTime(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "s";
            }

            @Override
            public float getValue(float value) {
                return value/3;
            }
        });


        // Configure limiter knobs
        limiterGainKnob = (KnobView) rootView.findViewById(R.id.limiterGainKnob);
        limiterGainKnob.setName("Gain");
        limiterGainKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");
            private float adjustValue(float value) {
                return (value - 1.0f) * 20f;
            }

            @Override
            public void onKnobChange(float value) {
                limiterGain = adjustValue(value);
                NativeClickTrack.Limiter.setGain(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "dB";
            }

            @Override
            public float getValue(float value) {
                return value/20 + 1;
            }
        });

        limiterThresholdKnob = (KnobView) rootView.findViewById(R.id.limiterThresholdKnob);
        limiterThresholdKnob.setName("Threshold");
        limiterThresholdKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");
            private float adjustValue(float value) {
                return (value - 1.0f) * 20f;
            }

            @Override
            public void onKnobChange(float value) {
                limiterThreshold = adjustValue(value);
                NativeClickTrack.Limiter.setThreshold(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "dB";
            }

            @Override
            public float getValue(float value) {
                return value/20 + 1;
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setControlValues();
    }

    private void setControlValues() {
        // EQ
        lowCutoffKnob.setValue(lowCutoff);
        midCutoffKnob.setValue(midCutoff);
        midGainKnob.setValue(midGain);
        midQKnob.setValue(midQ);
        highCutoffKnob.setValue(highCutoff);

        // Reverb
        reverbGainKnob.setValue(reverbGain);
        reverbWetnessKnob.setValue(reverbWetness);
        reverbTimeKnob.setValue(reverbTime);

        // Limiter
        limiterGainKnob.setValue(limiterGain);
        limiterThresholdKnob.setValue(limiterThreshold);
    }
}

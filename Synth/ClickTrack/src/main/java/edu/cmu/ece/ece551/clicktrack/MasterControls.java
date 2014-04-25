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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master_controls, container, false);

        // Configure EQ knobs
        KnobView lowCutoffKnob = (KnobView) rootView.findViewById(R.id.eqLowCutoffKnob);
        lowCutoffKnob.setName("Cutoff");
        lowCutoffKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return ((float) Math.pow(10, value) - 1) / 9 * 19980 + 20;
            }

            @Override
            public void onKnobChange(float value) {
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
        lowCutoffKnob.setValue(20);

        KnobView highCutoffKnob = (KnobView) rootView.findViewById(R.id.eqHighCutoffKnob);
        highCutoffKnob.setName("Cutoff");
        highCutoffKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return ((float) Math.pow(10, value) - 1) / 9 * 19980 + 20;
            }

            @Override
            public void onKnobChange(float value) {
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
        highCutoffKnob.setValue(20000);

        KnobView midCutoffKnob = (KnobView) rootView.findViewById(R.id.eqPeakCutoffKnob);
        midCutoffKnob.setName("Cutoff");
        midCutoffKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return ((float) Math.pow(10, value) - 1) / 9 * 19980 + 20;
            }

            @Override
            public void onKnobChange(float value) {
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
        midCutoffKnob.setValue(2000);

        KnobView midGainKnob = (KnobView) rootView.findViewById(R.id.eqPeakGainKnob);
        midGainKnob.setName("Gain");
        midGainKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");

            private float adjustValue(float value) {
                return (2 * value - 1) * 20;
            }

            @Override
            public void onKnobChange(float value) {
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
        midGainKnob.setValue(0.0f);

        KnobView midQKnob = (KnobView) rootView.findViewById(R.id.eqPeakQKnob);
        midQKnob.setName("Q");
        midQKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");

            private float adjustValue(float value) {
                return value * 9.5f + 0.5f;
            }

            @Override
            public void onKnobChange(float value) {
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
        midQKnob.setValue(1.0f);

        // Configure reverb knobs
        KnobView reverbGain = (KnobView) rootView.findViewById(R.id.reverbGainKnob);
        reverbGain.setName("Gain");
        reverbGain.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");
            private float adjustValue(float value) {
                return (value - 1.0f) * 20f;
            }

            @Override
            public void onKnobChange(float value) {
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
        reverbGain.setValue(0.0f);

        KnobView reverbWetness = (KnobView) rootView.findViewById(R.id.reverbWetnessKnob);
        reverbWetness.setName("Wetness");
        reverbWetness.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.00");

            @Override
            public void onKnobChange(float value) {
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
        reverbWetness.setValue(0.0f);

        KnobView reverbTime = (KnobView) rootView.findViewById(R.id.reverbTimeKnob);
        reverbTime.setName("Decay");
        reverbTime.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");
            private float adjustValue(float value) {
                return value * 3;
            }

            @Override
            public void onKnobChange(float value) {
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
        reverbTime.setValue(1.0f);


        // Configure limiter knobs
        KnobView limiterGain = (KnobView) rootView.findViewById(R.id.limiterGainKnob);
        limiterGain.setName("Gain");
        limiterGain.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");
            private float adjustValue(float value) {
                return (value - 1.0f) * 20f;
            }

            @Override
            public void onKnobChange(float value) {
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
        limiterGain.setValue(0.0f);

        KnobView limiterThreshold = (KnobView) rootView.findViewById(R.id.limiterThresholdKnob);
        limiterThreshold.setName("Threshold");
        limiterThreshold.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");
            private float adjustValue(float value) {
                return (value - 1.0f) * 20f;
            }

            @Override
            public void onKnobChange(float value) {
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
        limiterThreshold.setValue(-3.0f);

        return rootView;
    }
}

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

        // Configure ring modulator
        KnobView ringmodFreq = (KnobView) rootView.findViewById(R.id.ringmodFreqKnob);
        ringmodFreq.setName("Freq");
        ringmodFreq.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return ((float) Math.pow(10, value) - 1) / 9 * 999 + 1;
            }

            @Override
            public void onKnobChange(float value) {
                NativeClickTrack.RingModulator.setFreq(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "Hz";
            }

            @Override
            public float getValue(float value) {
                return (float) Math.log10((value - 1) / 999 * 9 + 1);
            }
        });
        ringmodFreq.setValue(1);

        KnobView ringmodWetness = (KnobView) rootView.findViewById(R.id.ringmodWetnessKnob);
        ringmodWetness.setName("Wetness");
        ringmodWetness.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.00");

            @Override
            public void onKnobChange(float value) {
                NativeClickTrack.RingModulator.setWetness(value);
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
        ringmodWetness.setValue(0.0f);

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

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

        // Configure reverb knobs
        KnobView reverbGain = (KnobView) rootView.findViewById(R.id.reverbGainKnob);
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
                return dfor.format(adjustValue(value));
            }
        });

        KnobView reverbWetness = (KnobView) rootView.findViewById(R.id.reverbWetnessKnob);
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
        });

        KnobView reverbTime = (KnobView) rootView.findViewById(R.id.reverbTimeKnob);
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
                return dfor.format(adjustValue(value));
            }
        });

        return rootView;
    }
}

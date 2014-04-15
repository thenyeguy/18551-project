package edu.cmu.ece.ece551.clicktrack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.text.DecimalFormat;

import edu.cmu.ece.ece551.synth.R;
import edu.cmu.ece.ece551.uis.KnobReceiver;
import edu.cmu.ece.ece551.uis.KnobView;

public class SubtractiveSynthToneControls extends Fragment {
    protected final String TAG = "ClickTrack_SubSynthToneControl";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_subtractive_synth_tone_control,
                container, false);

        // Get our controller
        final SubtractiveSynthController controller = SubtractiveSynthController.getInstance();

        // Configure our gain
        final KnobView gainKnob = (KnobView) rootView.findViewById(R.id.ssGainKnob);
        gainKnob.setName("Gain");
        gainKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");
            private float adjustValue(float value) {
                return (value-1) * 20;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setOutputGain(adjustValue(value));
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
        gainKnob.setValue(controller.getOutputGain());

        // Place a test button
        ToggleButton c = (ToggleButton) rootView.findViewById(R.id.ssCButton);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    NativeClickTrack.SubtractiveSynth.noteDown(60, 0.5f);
                } else {
                    NativeClickTrack.SubtractiveSynth.noteUp(60, 0.0f);
                }
            }
        });

        ToggleButton e = (ToggleButton) rootView.findViewById(R.id.ssEButton);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    NativeClickTrack.SubtractiveSynth.noteDown(64, 0.5f);
                } else {
                    NativeClickTrack.SubtractiveSynth.noteUp(64, 0.0f);
                }
            }
        });

        ToggleButton g = (ToggleButton) rootView.findViewById(R.id.ssGButton);
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    NativeClickTrack.SubtractiveSynth.noteDown(67, 0.5f);
                } else {
                    NativeClickTrack.SubtractiveSynth.noteUp(67, 0.0f);
                }
            }
        });


        // Configure LFO
        final KnobView lfoFreqKnob = (KnobView) rootView.findViewById(R.id.ssLfoFreqKnob);
        lfoFreqKnob.setName("Frequency");
        lfoFreqKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return value * 20;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setLfoFreq(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "Hz";
            }

            @Override
            public float getValue(float value) {
                return value/20;
            }
        });
        lfoFreqKnob.setValue(controller.getLfoFreq());

        final KnobView tremeloKnob = (KnobView) rootView.findViewById(R.id.ssTremeloKnob);
        tremeloKnob.setName("Tremelo");
        tremeloKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");
            private float adjustValue(float value) {
                return value * 10;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setLfoTremeloDb(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "dB";
            }

            @Override
            public float getValue(float value) {
                return value/10;
            }
        });
        tremeloKnob.setValue(controller.getLfoTremeloDb());

        final KnobView vibratoKnob = (KnobView) rootView.findViewById(R.id.ssVibratoKnob);
        vibratoKnob.setName("Vibrato");
        vibratoKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.00");
            private float adjustValue(float value) {
                return value * 2;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setLfoVibratoSteps(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value));
            }

            @Override
            public float getValue(float value) {
                return value/2;
            }
        });
        vibratoKnob.setValue(controller.getLfoVibratoSteps());


        // Set up our oscillator modes
        ArrayAdapter<CharSequence> oscModes = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.oscillator_modes, android.R.layout.simple_spinner_item);
        oscModes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner osc1Spinner = (Spinner) rootView.findViewById(R.id.ssOsc1mode);
        osc1Spinner.setAdapter(oscModes);
        osc1Spinner.setSelection(oscModeToIndex(controller.getOsc1mode()));
        osc1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String modeText = (String) adapterView.getItemAtPosition(i);
                controller.setOsc1mode(stringToOscMode(modeText));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Spinner osc2Spinner = (Spinner) rootView.findViewById(R.id.ssOsc2mode);
        osc2Spinner.setAdapter(oscModes);
        osc2Spinner.setSelection(oscModeToIndex(controller.getOsc2mode()));
        osc2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String modeText = (String) adapterView.getItemAtPosition(i);
                controller.setOsc2mode(stringToOscMode(modeText));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        // Set up oscillator transpositions
        final KnobView osc1Transpose = (KnobView) rootView.findViewById(R.id.ssOsc1TransposeKnob);
        osc1Transpose.setName("Transpose");
        osc1Transpose.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");
            private float adjustValue(float value) {
                return Math.round((value - 0.5f) * 2 * 24);
            }

            @Override
            public void onKnobChange(float value) {
                controller.setOsc1transposition(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value));
            }

            @Override
            public float getValue(float value) {
                return value/2/24 + 0.5f;
            }
        });
        osc1Transpose.setValue(controller.getOsc1transposition());

        final KnobView osc2Transpose = (KnobView) rootView.findViewById(R.id.ssOsc2TransposeKnob);
        osc2Transpose.setName("Transpose");
        osc2Transpose.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");
            private float adjustValue(float value) {
                return Math.round((value - 0.5f) * 2 * 24);
            }

            @Override
            public void onKnobChange(float value) {
                controller.setOsc2transposition(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value));
            }

            @Override
            public float getValue(float value) {
                return value/2/24 + 0.5f;
            }
        });
        osc2Transpose.setValue(controller.getOsc2transposition());



        // Set up our ADSR envelope
        final KnobView attackKnob = (KnobView) rootView.findViewById(R.id.ssAttackKnob);
        attackKnob.setName("Attack");
        attackKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.00");
            private float adjustValue(float value) {
                return value * 2;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setAttack(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "s";
            }

            @Override
            public float getValue(float value) {
                return value/2;
            }
        });
        attackKnob.setValue(controller.getAttack());

        final KnobView decayKnob = (KnobView) rootView.findViewById(R.id.ssDecayKnob);
        decayKnob.setName("Decay");
        decayKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.00");

            private float adjustValue(float value) {
                return value * 2;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setDecay(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "s";
            }

            @Override
            public float getValue(float value) {
                return value/2;
            }
        });
        decayKnob.setValue(controller.getDecay());

        final KnobView sustainKnob = (KnobView) rootView.findViewById(R.id.ssSustainKnob);
        sustainKnob.setName("Sustain");
        sustainKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.00");

            private float adjustValue(float value) {
                return value;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setSustain(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value));
            }

            @Override
            public float getValue(float value) {
                return value;
            }
        });
        sustainKnob.setValue(controller.getSustain());

        final KnobView releaseKnob = (KnobView) rootView.findViewById(R.id.ssReleaseKnob);
        releaseKnob.setName("Release");
        releaseKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.00");
            private float adjustValue(float value) {
                return value * 2;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setRelease(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "s";
            }

            @Override
            public float getValue(float value) {
                return value/2;
            }
        });
        releaseKnob.setValue(controller.getRelease());



        // Set filter mode
        ArrayAdapter<CharSequence> filterModes = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.filter_modes, android.R.layout.simple_spinner_item);
        filterModes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner filterModeSpinner = (Spinner) rootView.findViewById(R.id.ssFilterModeSpinner);
        filterModeSpinner.setAdapter(filterModes);
        filterModeSpinner.setSelection(controller.getFilterMode().value);
        filterModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String modeText = (String) adapterView.getItemAtPosition(i);
                controller.setFilterMode(stringToFilterMode(modeText));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Set filter sliders
        final KnobView filterCutoffKnob = (KnobView) rootView.findViewById(R.id.ssFilterCutoffKnob);
        filterCutoffKnob.setName("Cutoff");
        filterCutoffKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return ((float) Math.pow(10, value) - 1)/9 * 19980 + 20;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setFilterCutoff(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "Hz";
            }

            @Override
            public float getValue(float value) {
                return (float) Math.log10((value-20)/19980*9 + 1);
            }
        });
        filterCutoffKnob.setValue(controller.getFilterCutoff());

        final KnobView filterGainKnob = (KnobView) rootView.findViewById(R.id.ssFilterGainKnob);
        filterGainKnob.setName("Gain");
        filterGainKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");

            private float adjustValue(float value) {
                return (2 * value - 1) * 20;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setFilterGain(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value)) + "dB";
            }

            @Override
            public float getValue(float value) {
                return (value/20 + 1)/2;
            }
        });
        filterGainKnob.setValue(controller.getFilterGain());

        final KnobView filterQKnob = (KnobView) rootView.findViewById(R.id.ssFilterQKnob);
        filterQKnob.setName("Q");
        filterQKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");

            private float adjustValue(float value) {
                return value * 9.5f + 0.5f;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setFilterQ(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value));
            }

            @Override
            public float getValue(float value) {
                return (value - 0.5f)/9.5f;
            }
        });
        filterQKnob.setValue(controller.getFilterQ());


        // Unfocus the spinner on launch
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return rootView;
    }


    private NativeClickTrack.SubtractiveSynth.OscillatorMode stringToOscMode(String modeText)
    {
        NativeClickTrack.SubtractiveSynth.OscillatorMode mode = NativeClickTrack.SubtractiveSynth
                .OscillatorMode.SINE;

        if(modeText.equals("Saw"))
            mode = NativeClickTrack.SubtractiveSynth.OscillatorMode.BLEPSAW;
        else if(modeText.equals("Square"))
            mode = NativeClickTrack.SubtractiveSynth.OscillatorMode.BLEPSQUARE;
        else if(modeText.equals("Triangle"))
            mode = NativeClickTrack.SubtractiveSynth.OscillatorMode.BLEPTRI;
        else if(modeText.equals("White Noise"))
            mode = NativeClickTrack.SubtractiveSynth.OscillatorMode.WHITENOISE;

        return mode;
    }

    private int oscModeToIndex(NativeClickTrack.SubtractiveSynth.OscillatorMode mode) {
        switch(mode) {
            case SINE:
                return 0;
            case SAW:
            case BLEPSAW:
                return 1;
            case SQUARE:
            case BLEPSQUARE:
                return 2;
            case TRI:
            case BLEPTRI:
                return 3;
            case WHITENOISE:
                return 4;
        }
        return 0;
    }

    private NativeClickTrack.SubtractiveSynth.FilterMode stringToFilterMode(String modeText)
    {
        NativeClickTrack.SubtractiveSynth.FilterMode mode = NativeClickTrack.SubtractiveSynth
                .FilterMode.LOWPASS;

        if(modeText.equals("Low shelf"))
            mode = NativeClickTrack.SubtractiveSynth.FilterMode.LOWSHELF;
        else if(modeText.equals("High pass"))
            mode = NativeClickTrack.SubtractiveSynth.FilterMode.HIGHPASS;
        else if(modeText.equals("High shelf"))
            mode = NativeClickTrack.SubtractiveSynth.FilterMode.HIGHSHELF;
        else if(modeText.equals("Peak"))
            mode = NativeClickTrack.SubtractiveSynth.FilterMode.PEAK;

        return mode;
    }
}

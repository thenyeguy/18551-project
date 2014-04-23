package edu.cmu.ece.ece551.clicktrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import edu.cmu.ece.ece551.synth.R;
import edu.cmu.ece.ece551.uis.KnobReceiver;
import edu.cmu.ece.ece551.uis.KnobView;

public class FMSynthToneControls extends Fragment {
    private static final int PICKFILE_RESULT_CODE = 1;
    protected final String TAG = "ClickTrack_FMSynthToneControl";
    protected final String TONEPATH = "/sdcard/ClickTrack/FMSynthTones/";

    final FMSynthController controller = FMSynthController.getInstance();

    // Master controls
    KnobView gainKnob;
    Button saveButton, loadButton;
    ToggleButton c, e, g;

    // LFO
    KnobView lfoFreqKnob, tremeloKnob, vibratoKnob;

    // Oscillators
    Spinner carrierSpinner, modulatorSpinner;
    KnobView carrierTranspose, modulatorTranspose;
    KnobView modulatorIntensity;

    // ADSR
    KnobView attackKnob, decayKnob, sustainKnob, releaseKnob;

    // Filter
    Spinner filterModeSpinner;
    KnobView filterCutoffKnob, filterGainKnob, filterQKnob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_fm_synth_tone_control, container, 
                false);

        // Configure our gain
        gainKnob = (KnobView) rootView.findViewById(R.id.fmGainKnob);
        gainKnob.setName("Gain");
        gainKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");

            private float adjustValue(float value) {
                return (value - 1) * 20;
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
                return value / 20 + 1;
            }
        });

        // Configure our save/load code
        saveButton = (Button) rootView.findViewById(R.id.saveFMSynthButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ask for a name in a dialog box
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Enter a tone name");

                final EditText input = new EditText(getActivity());
                alert.setView(input);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Get the file to save to
                        String name = input.getText().toString();
                        File f = new File(TONEPATH + name + ".json");

                        Log.i(TAG, "Saving fm synth tone: " + f.toString());

                        // Write out the tone file
                        try {
                            Files.write(controller.toString(), f, Charsets.UTF_8);
                        } catch(IOException e1) {
                            Log.e(TAG, e1.toString());
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Failed to save tone with name: " + name,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Toast.makeText(getActivity().getApplicationContext(),
                                "Tone saved with name: " + name,
                                Toast.LENGTH_LONG).show();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Save aborted.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                alert.show();
            }
        });

        loadButton = (Button) rootView.findViewById(R.id.loadFMSynthButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Loading fm synth tone");

                // Launch file picker intent
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(Uri.parse(TONEPATH), "file/*");
                try {
                    startActivityForResult(intent, PICKFILE_RESULT_CODE);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "No file picker available");
                    Toast.makeText(getActivity().getApplicationContext(),
                            "No file picker available.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Place a test button
        c = (ToggleButton) rootView.findViewById(R.id.fmCButton);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    NativeClickTrack.FMSynth.noteDown(60, 0.5f);
                } else {
                    NativeClickTrack.FMSynth.noteUp(60, 0.0f);
                }
            }
        });

        e = (ToggleButton) rootView.findViewById(R.id.fmEButton);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    NativeClickTrack.FMSynth.noteDown(64, 0.5f);
                } else {
                    NativeClickTrack.FMSynth.noteUp(64, 0.0f);
                }
            }
        });

        g = (ToggleButton) rootView.findViewById(R.id.fmGButton);
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    NativeClickTrack.FMSynth.noteDown(67, 0.5f);
                } else {
                    NativeClickTrack.FMSynth.noteUp(67, 0.0f);
                }
            }
        });


        // Configure LFO
        lfoFreqKnob = (KnobView) rootView.findViewById(R.id.fmLfoFreqKnob);
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
                return value / 20;
            }
        });

        tremeloKnob = (KnobView) rootView.findViewById(R.id.fmTremeloKnob);
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
                return value / 10;
            }
        });

        vibratoKnob = (KnobView) rootView.findViewById(R.id.fmVibratoKnob);
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
                return value / 2;
            }
        });


        // Set up our oscillator modes
        ArrayAdapter<CharSequence> oscModes = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.oscillator_modes, android.R.layout.simple_spinner_item);
        oscModes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        carrierSpinner = (Spinner) rootView.findViewById(R.id.fmCarrierode);
        carrierSpinner.setAdapter(oscModes);
        carrierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String modeText = (String) adapterView.getItemAtPosition(i);
                controller.setCarrierMode(stringToOscMode(modeText));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        modulatorSpinner = (Spinner) rootView.findViewById(R.id.fmModulatorMode);
        modulatorSpinner.setAdapter(oscModes);
        modulatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String modeText = (String) adapterView.getItemAtPosition(i);
                controller.setModulatorMode(stringToOscMode(modeText));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        // Set up oscillator transpositions
        carrierTranspose = (KnobView) rootView.findViewById(R.id.fmCarrierTransposeKnob);
        carrierTranspose.setName("Transpose");
        carrierTranspose.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return Math.round((value - 0.5f) * 2 * 24);
            }

            @Override
            public void onKnobChange(float value) {
                controller.setCarrierTransposition(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value));
            }

            @Override
            public float getValue(float value) {
                return value / 2 / 24 + 0.5f;
            }
        });

        modulatorTranspose = (KnobView) rootView.findViewById(R.id.fmModulatorTransposeKnob);
        modulatorTranspose.setName("Transpose");
        modulatorTranspose.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return Math.round((value - 0.5f) * 2 * 24);
            }

            @Override
            public void onKnobChange(float value) {
                controller.setModulatorTransposition(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value));
            }

            @Override
            public float getValue(float value) {
                return value / 2 / 24 + 0.5f;
            }
        });


        modulatorIntensity = (KnobView) rootView.findViewById(R.id.fmModulatorIntensityKnob);
        modulatorIntensity.setName("Intensity");
        modulatorIntensity.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");

            private float adjustValue(float value) {
                return Math.round(value * 10);
            }

            @Override
            public void onKnobChange(float value) {
                controller.setModulatorIntensity(adjustValue(value));
            }

            @Override
            public String formatValue(float value) {
                return dfor.format(adjustValue(value));
            }

            @Override
            public float getValue(float value) {
                return value / 10;
            }
        });



        // Set up our ADSR envelope
        attackKnob = (KnobView) rootView.findViewById(R.id.fmAttackKnob);
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
                return value / 2;
            }
        });

        decayKnob = (KnobView) rootView.findViewById(R.id.fmDecayKnob);
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
                return value / 2;
            }
        });

        sustainKnob = (KnobView) rootView.findViewById(R.id.fmSustainKnob);
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

        releaseKnob = (KnobView) rootView.findViewById(R.id.fmReleaseKnob);
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
                return value / 2;
            }
        });



        // Set filter mode
        ArrayAdapter<CharSequence> filterModes = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.filter_modes, android.R.layout.simple_spinner_item);
        filterModes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filterModeSpinner = (Spinner) rootView.findViewById(R.id.fmFilterModeSpinner);
        filterModeSpinner.setAdapter(filterModes);
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
        filterCutoffKnob = (KnobView) rootView.findViewById(R.id.fmFilterCutoffKnob);
        filterCutoffKnob.setName("Cutoff");
        filterCutoffKnob.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return ((float) Math.pow(10, value) - 1) / 9 * 19980 + 20;
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
                return (float) Math.log10((value - 20) / 19980 * 9 + 1);
            }
        });

        filterGainKnob = (KnobView) rootView.findViewById(R.id.fmFilterGainKnob);
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
                return (value / 20 + 1) / 2;
            }
        });

        filterQKnob = (KnobView) rootView.findViewById(R.id.fmFilterQKnob);
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
                return (value - 0.5f) / 9.5f;
            }
        });


        // Unfocus the spinner on launch
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setControlValues();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null)
            return;

        switch(requestCode) {
            case PICKFILE_RESULT_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    String filePath = data.getData().getPath();
                    Log.i(TAG, "Loading from path: " + filePath);

                    try {
                        String file = Files.toString(new File(filePath), Charsets.UTF_8);
                        controller.fromString(file);
                        setControlValues();
                    } catch(IOException e1) {
                        Log.e(TAG, "Failed to read file");
                        return;
                    }
                }
                else {
                    Log.e(TAG, "Failed to get file for loading");
                }
                break;
            default:
        }
    }


    private void setControlValues()
    {
        // MASTER
        gainKnob.setValue(controller.getOutputGain());

        // LFO
        lfoFreqKnob.setValue(controller.getLfoFreq());
        tremeloKnob.setValue(controller.getLfoTremeloDb());
        vibratoKnob.setValue(controller.getLfoVibratoSteps());

        // Oscillators
        carrierSpinner.setSelection(oscModeToIndex(controller.getCarrierMode()));
        modulatorSpinner.setSelection(oscModeToIndex(controller.getModulatorMode()));
        carrierTranspose.setValue(controller.getCarrierTransposition());
        modulatorTranspose.setValue(controller.getModulatorTransposition());
        modulatorIntensity.setValue(controller.getModulatorIntensity());

        // ADSR
        attackKnob.setValue(controller.getAttack());
        decayKnob.setValue(controller.getDecay());
        sustainKnob.setValue(controller.getSustain());
        releaseKnob.setValue(controller.getRelease());

        // Filter
        filterModeSpinner.setSelection(controller.getFilterMode().value);
        filterCutoffKnob.setValue(controller.getFilterCutoff());
        filterGainKnob.setValue(controller.getFilterGain());
        filterQKnob.setValue(controller.getFilterQ());
    }


    private NativeClickTrack.FMSynth.OscillatorMode stringToOscMode(String modeText)
    {
        NativeClickTrack.FMSynth.OscillatorMode mode = NativeClickTrack.FMSynth
                .OscillatorMode.SINE;

        if(modeText.equals("Saw"))
            mode = NativeClickTrack.FMSynth.OscillatorMode.BLEPSAW;
        else if(modeText.equals("Square"))
            mode = NativeClickTrack.FMSynth.OscillatorMode.BLEPSQUARE;
        else if(modeText.equals("Triangle"))
            mode = NativeClickTrack.FMSynth.OscillatorMode.BLEPTRI;
        else if(modeText.equals("White Noise"))
            mode = NativeClickTrack.FMSynth.OscillatorMode.WHITENOISE;

        return mode;
    }

    private int oscModeToIndex(NativeClickTrack.FMSynth.OscillatorMode mode) {
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

    private NativeClickTrack.FMSynth.FilterMode stringToFilterMode(String modeText)
    {
        NativeClickTrack.FMSynth.FilterMode mode = NativeClickTrack.FMSynth
                .FilterMode.LOWPASS;

        if(modeText.equals("Low shelf"))
            mode = NativeClickTrack.FMSynth.FilterMode.LOWSHELF;
        else if(modeText.equals("High pass"))
            mode = NativeClickTrack.FMSynth.FilterMode.HIGHPASS;
        else if(modeText.equals("High shelf"))
            mode = NativeClickTrack.FMSynth.FilterMode.HIGHSHELF;
        else if(modeText.equals("Peak"))
            mode = NativeClickTrack.FMSynth.FilterMode.PEAK;

        return mode;
    }
}

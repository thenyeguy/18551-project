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

public class DrumMachineToneControls extends Fragment {
    private static final int PICKFILE_RESULT_CODE = 1;
    protected final String TAG = "ClickTrack_SubSynthToneControl";
    protected final String TONEPATH = "/sdcard/ClickTrack/DrumMachineTones/";

    final DrumMachineController controller = DrumMachineController.getInstance();

    // Master controls
    KnobView gainKnob;
    Button saveButton, loadButton;
    Button snare, bass, hihat;

    // Ring Mod
    KnobView ringFreq, ringWetness;
    
    // Compressor
    KnobView compressionThreshold, compressionRatio;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_drum_machine_controls,
                container, false);

        // Configure our gain
        gainKnob = (KnobView) rootView.findViewById(R.id.drumMachineGainKnob);
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
        saveButton = (Button) rootView.findViewById(R.id.saveDrumMachineButton);
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

                        Log.i(TAG, "Saving drum machine tone: " + f.toString());

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

        loadButton = (Button) rootView.findViewById(R.id.loadDrumMachineButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Loading drum machine tone");

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
        snare = (Button) rootView.findViewById(R.id.drumMachineSnareButton);
        snare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NativeClickTrack.DrumMachine.noteDown(NativeClickTrack.DrumMachine.DrumNotes
                                .SNAREDRUM1.value, 0.5f);
            }
        });

        bass = (Button) rootView.findViewById(R.id.drumMachineBassButton);
        bass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NativeClickTrack.DrumMachine.noteDown(NativeClickTrack.DrumMachine.DrumNotes
                        .BASSDRUM1.value, 0.5f);
            }
        });

        hihat = (Button) rootView.findViewById(R.id.drumMachineHihatButton);
        hihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NativeClickTrack.DrumMachine.noteDown(NativeClickTrack.DrumMachine.DrumNotes
                        .CLOSEDHIHAT.value, 0.5f);
            }
        });

        // Configure ring modulator
        ringFreq = (KnobView) rootView.findViewById(R.id.ringmodFreqKnob);
        ringFreq.setName("Freq");
        ringFreq.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0");

            private float adjustValue(float value) {
                return ((float) Math.pow(10, value) - 1) / 9 * 999 + 1;
            }

            @Override
            public void onKnobChange(float value) {
                controller.setRingFreq(adjustValue(value));
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

        ringWetness = (KnobView) rootView.findViewById(R.id.ringmodWetnessKnob);
        ringWetness.setName("Wetness");
        ringWetness.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.00");

            @Override
            public void onKnobChange(float value) {
                controller.setRingWetness(value);
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

        // Configure limiter knobs
        compressionRatio = (KnobView) rootView.findViewById(R.id.compressionRatioKnob);
        compressionRatio.setName("Ratio");
        compressionRatio.registerKnobReceiver(new KnobReceiver() {
            private DecimalFormat dfor = new DecimalFormat("0.0");

            private float adjustValue(float value) {
                return value;
            }

            @Override
            public void onKnobChange(float value) {
                NativeClickTrack.Limiter.setGain(adjustValue(value));
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

        compressionThreshold = (KnobView) rootView.findViewById(R.id.compressionThresholdKnob);
        compressionThreshold.setName("Threshold");
        compressionThreshold.registerKnobReceiver(new KnobReceiver() {
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
                return value / 20 + 1;
            }
        });

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

        // RINGMOD
        ringFreq.setValue(controller.getRingFreq());
        ringWetness.setValue(controller.getRingWetness());

        // COMPRESSOR
        compressionRatio.setValue(controller.getCompressionRatio());
        compressionThreshold.setValue(controller.getCompressionThreshold());
    }
}

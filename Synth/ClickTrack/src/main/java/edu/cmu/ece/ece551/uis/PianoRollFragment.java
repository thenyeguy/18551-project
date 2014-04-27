package edu.cmu.ece.ece551.uis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.cmu.ece.ece551.clicktrack.DrumMachineController;
import edu.cmu.ece.ece551.clicktrack.FMSynthController;
import edu.cmu.ece.ece551.clicktrack.InstrumentController;
import edu.cmu.ece.ece551.clicktrack.SubtractiveSynthController;
import edu.cmu.ece.ece551.synth.R;
import edu.cmu.ece.ece551.uis.scale.ScaleType;
import edu.cmu.ece.ece551.uis.scale.TimingManager;

/**
 * Created by michaelryan on 2/23/14.
 */
public class PianoRollFragment extends Fragment {


    String startingNote = "C";
    ScaleType scaleType = ScaleType.DIATONIC_MAJOR;
    private static String TAG = "piano";
    private PianoRollView prv;

    public static final String ROLLPATH = Environment.getExternalStorageDirectory() + "/ClickTrack/PianoRoll/";

    InstrumentController instrument;

    public PianoRollFragment() {
        instrument = SubtractiveSynthController.getInstance();
    }

    private TimingManager tm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.roll_fragment, container, false);

        Button clearButton = (Button) rootView.findViewById(R.id.clearPianoButton);

        Button playButton = (Button) rootView.findViewById(R.id.playPianoButton);

        Button stopButton = (Button) rootView.findViewById(R.id.stopPianoButton);
        Button octUpButton = (Button) rootView.findViewById(R.id.octaveUp);
        Button octDownButton = (Button) rootView.findViewById(R.id.octaveDown);

        Button saveButton = (Button) rootView.findViewById(R.id.savePianoButton);

        final TextView octaveText = (TextView) rootView.findViewById(R.id.octaveText);
        final TextView tempoNum = (TextView) rootView.findViewById(R.id.tempoBox);
        prv = (PianoRollView) rootView.findViewById(R.id.pianoRoll);

        // TODO: Instrument spinner

        prv.setInstrument(instrument);

        Spinner scaleSpinner = (Spinner) rootView.findViewById(R.id.scaleSpinner);
        ArrayAdapter<ScaleType> adapter = new ArrayAdapter<ScaleType>(rootView.getContext(),
                android.R.layout.simple_spinner_item, ScaleType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scaleSpinner.setAdapter(adapter);

        Spinner tonicSpinner = (Spinner) rootView.findViewById(R.id.tonicSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.tonics, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tonicSpinner.setAdapter(adapter2);

        final Spinner instrumentSpinner = (Spinner) rootView.findViewById(R.id.instrumentSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.instruments, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instrumentSpinner.setAdapter(adapter3);


        tm = new TimingManager(prv.getState(), prv);
        tm.setInst(SubtractiveSynthController.getInstance());




        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ask for a name in a dialog box
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Enter a name for this measure");

                final EditText input = new EditText(getActivity());

                alert.setView(input);

                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Get the file to save to
                        String name = input.getText().toString();
                        File f = new File(ROLLPATH + name + ".json");

                        File folder = new File(ROLLPATH);
                        if (!folder.exists()) {
                            folder.mkdir();
                        }


                        ObjectOutputStream oos = null;
                        try {
                            oos = new ObjectOutputStream(new FileOutputStream(f));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.i("piano", "Saving subtractive synth tone: " + f.toString());

                        // Write out the tone file
                        try {
                            oos.writeObject(prv.getState());
                            //Files.write(prv.getState().toString(), f, Charsets.UTF_8);
                        } catch(IOException e1) {
                            Log.e("piano", e1.toString());
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


        Button loadButton = (Button) rootView.findViewById(R.id.loadPianoButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("piano", "Loading subtractive synth tone");

                // Launch file picker intent
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(Uri.parse(ROLLPATH), "file/*");
                try {
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException e) {
                    Log.e("piano", "No file picker available");
                    Toast.makeText(getActivity().getApplicationContext(),
                            "No file picker available.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });



        instrumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Log.d(TAG, "switch inst with position " + pos);
                switch(pos) {
                    case 0:
                        tm.setInst(SubtractiveSynthController.getInstance());
                        break;
                    case 1:
                        tm.setInst(FMSynthController.getInstance());
                        break;
                    case 2:
                        tm.setInst(DrumMachineController.getInstance());
                        // Do drum stuff.

                        // Drums need their own kind of SequencerState?

                        prv.prepForDrums();
                        octaveText.setText("0");

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                ScaleType scale = (ScaleType) adapterView.getItemAtPosition(pos);

                scaleType = scale;
                prv.setScale(scaleType, startingNote);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing?
            }
        });

        tonicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String selection = adapterView.getItemAtPosition(pos).toString();
                startingNote = selection;

                prv.setScale(scaleType, startingNote);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing?
            }
        });


        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tm != null) {
                    tm.stopPlaying();
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!tm.isPlaying()) {
                    tm.setTempo(Integer.parseInt(tempoNum.getText().toString()));
                    tm.playMeasure();
                }

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tm == null || !tm.isPlaying()) {
                    prv.clearGrid();
                }
            }
        });

        octDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                octaveText.setText(Integer.toString(prv.octaveDown()));

            }
        });

        octUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                octaveText.setText(Integer.toString(prv.octaveUp()));
            }
        });

        octaveText.setText(Integer.toString(prv.getOctave()));

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null)
            return;

        switch(requestCode) {
            case 1:
                if(resultCode == Activity.RESULT_OK) {
                    String filePath = data.getData().getPath();
                    Log.i(TAG, "Loading from path: " + filePath);

                    try {
                        String file = Files.toString(new File(filePath), Charsets.UTF_8);
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(filePath)));
                        SequencerState ss = (SequencerState) ois.readObject();

                        prv.setState(ss);

                    } catch(IOException e1) {
                        Log.e(TAG, "Failed to read file");
                        return;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.e(TAG, "Failed to get file for loading");
                }
                break;
            default:
        }
    }




    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of Piano");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of Piano");
        super.onPause();
    }

}

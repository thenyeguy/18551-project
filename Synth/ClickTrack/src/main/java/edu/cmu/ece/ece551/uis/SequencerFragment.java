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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.cmu.ece.ece551.synth.R;

public class SequencerFragment extends Fragment {

    private static String TAG = "sequencerFragment";

    private SequencerView sv;

    private static String SONGPATH = Environment.getExternalStorageDirectory() + "/ClickTrack/Songs/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.seq_fragment, container, false);

        sv = (SequencerView) ((LinearLayout) rootView.findViewById(R.id.InnerRoll)).getChildAt(0);

        Button playButton = (Button) rootView.findViewById(R.id.playSeqButton);

        Button loadButton = (Button) rootView.findViewById(R.id.loadSequencerRollButton);

        Button stopButton = (Button) rootView.findViewById(R.id.stopSeqButton);


        Button saveButton = (Button) rootView.findViewById(R.id.saveSeqButton);

        final ToggleButton loopButton = (ToggleButton) rootView.findViewById(R.id.loopButton);

        final TextView tempoNum = (TextView) rootView.findViewById(R.id.tempoSeq);

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
                        File f = new File(SONGPATH + name + ".json");

                        File folder = new File(SONGPATH);
                        if (!folder.exists()) {
                            folder.mkdir();
                        }


                        ObjectOutputStream oos = null;
                        try {
                            oos = new ObjectOutputStream(new FileOutputStream(f));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.i("piano", "Saving loop: " + f.toString());

                        // Write out the tone file
                        try {
                            oos.writeObject(sv.getMeasures());
                            //Files.write(prv.getState().toString(), f, Charsets.UTF_8);
                        } catch (IOException e1) {
                            Log.e("piano", e1.toString());
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Failed to save loop with name: " + name,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Toast.makeText(getActivity().getApplicationContext(),
                                "Loop saved with name: " + name,
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

        stopButton.setOnClickListener(
                new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        SequencerTask.stop();
                        sv.stopRect();
                    }
                }
        );

        loadButton.setOnClickListener(
                new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "Loading sequence");

                        // Launch file picker intent
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setDataAndType(Uri.parse(PianoRollFragment.ROLLPATH), "file/*");
                        try {
                            startActivityForResult(intent, 1);
                        } catch (ActivityNotFoundException e) {
                            Log.e(TAG, "No file picker available");
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No file picker available.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        playButton.setOnClickListener(
                new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        final SequencerState[][] ss = sv.getMeasures();

                        int tempo = Integer.parseInt(tempoNum.getText().toString());
                        sv.startRectMotion(tempo, SequencerTask.isLooping());
                        SequencerTask.startSequencer(ss, tempo);

                    }
                }
        );

        loopButton.setOnClickListener(
                new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        if (!((ToggleButton) v).isChecked()) {
                            SequencerTask.setLooping(false);
                        } else {
                            SequencerTask.setLooping(true);
                        }
                    }
                }
        );

        return rootView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    String filePath = data.getData().getPath();
                    Log.i(TAG, "Loading from path: " + filePath);

                    try {
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(filePath)));

                        Object newObj = ois.readObject();
                        Log.d(TAG, "class " + newObj.getClass());

                        if (newObj instanceof SequencerState) {
                            SequencerState ss = (SequencerState) newObj;

                            // Do something with the specified sequencer state.
                            sv.setNextThing(ss);
                        } else if (newObj instanceof SequencerState[][]) {
                            sv.setMeasures((SequencerState[][]) newObj);
                        }
                    } catch (IOException e1) {
                        Log.e(TAG, "Failed to read file");
                        e1.printStackTrace();
                        return;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Failed to get file for loading");
                }
                break;
            default:
        }
    }
}
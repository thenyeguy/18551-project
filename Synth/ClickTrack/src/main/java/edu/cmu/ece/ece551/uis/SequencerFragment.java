package edu.cmu.ece.ece551.uis;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.ece.ece551.clicktrack.DrumMachineController;
import edu.cmu.ece.ece551.clicktrack.FMSynthController;
import edu.cmu.ece.ece551.clicktrack.InstrumentController;
import edu.cmu.ece.ece551.clicktrack.SubtractiveSynthController;
import edu.cmu.ece.ece551.synth.R;
import edu.cmu.ece.ece551.uis.scale.TimingManager;

public class SequencerFragment extends Fragment {

    private static String TAG = "sequencerFragment";

    private SequencerView sv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.seq_fragment, container, false);

        sv = (SequencerView) ((LinearLayout) rootView.findViewById(R.id.InnerRoll)).getChildAt(0);

        Button playButton = (Button) rootView.findViewById(R.id.playSeqButton);

        Button loadButton = (Button) rootView.findViewById(R.id.loadSequencerRollButton);

        Button stopButton = (Button) rootView.findViewById(R.id.stopSeqButton);

        ToggleButton loopButton = (ToggleButton) rootView.findViewById(R.id.loopButton);

        final TextView tempoNum = (TextView) rootView.findViewById(R.id.tempoSeq);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SequencerTask.stop();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
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
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SequencerState[][] ss = sv.getMeasures();

                SequencerTask.startSequencer(ss, Integer.parseInt(tempoNum.getText().toString()));

            }
        });

        loopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((ToggleButton) v).isChecked()) {
                    SequencerTask.setLooping(false);
                } else {
                    SequencerTask.setLooping(true);
                }
            }
        });

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
                        SequencerState ss = (SequencerState) ois.readObject();

                        // Do something with the specified sequencer state.
                        sv.setNextThing(ss);
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
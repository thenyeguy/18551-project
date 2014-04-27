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
    private Timer timer;

    public SequencerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.seq_fragment, container, false);

        sv = (SequencerView) ((LinearLayout) rootView.findViewById(R.id.InnerRoll)).getChildAt(0);

        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.InnerRoll);

        for (View v : rootView.getTouchables()) {
            Log.d(TAG, "id was " + Integer.toHexString(v.getId()));
        }

        Log.d(TAG, "We have a problem if " + (sv == null));

        Button playButton = (Button) rootView.findViewById(R.id.playSeqButton);

        Button loadButton = (Button) rootView.findViewById(R.id.loadSequencerRollButton);

        final TextView tempoNum = (TextView) rootView.findViewById(R.id.tempoSeq);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Loading subtractive synth tone");

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

                // TODO: Get all sequencerstates, set up three timing managers, then fire all of them
                // when they need to be. Get the tempo, then use a timer that spins off timing manager
                // threads in threes whenever tempo strikes.

                timer = new Timer();

                TimerTask tt = new TimerTask() {

                    int index = -1;

                    @Override
                    public void run() {
                        index++;

                        // We get

                        Log.d(TAG, "Trying to play col " + index);
                        for (int j = 0; j < 3; j++) {
                            SequencerState measure = ss[j][index];
                            if (measure != null) {
                                TimingManager tm = new TimingManager(measure,
                                        Integer.parseInt(tempoNum.getText().toString()),
                                        getInstFromIndex(j));

                                tm.playMeasure();
                            }

                        }

                        if (index >= 7) {
                            index = 0;
                            this.cancel();
                            return;
                        }

                    }
                };


                // Calculate tempo... I have 6 measures. Each measure is 4 beats, and I need to know how many ms per beat.
                // I have beats per minute, so I

                int secondsPerMeasure = (int) (1f / (Float.parseFloat(tempoNum.getText().toString()) / 60f / 4f / 1000f));
                timer.schedule(tt, 0, secondsPerMeasure);

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
                        Log.d(TAG, "I have an input stream");
                        SequencerState ss = (SequencerState) ois.readObject();

                        Log.d(TAG, "got something SS-related");
                        // Do something with the specified sequencer state.
                        Log.d(TAG, "sv is " + (sv != null));
                        Log.d(TAG, "ss is " + (ss != null));
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


    private InstrumentController getInstFromIndex(int i) {
        switch (i) {
            case 0:
                return SubtractiveSynthController.getInstance();
            case 1:
                return FMSynthController.getInstance();
            case 2:
                return DrumMachineController.getInstance();
        }
        return null;
    }

}
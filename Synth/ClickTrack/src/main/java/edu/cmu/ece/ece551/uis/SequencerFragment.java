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
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import edu.cmu.ece.ece551.synth.R;

public class SequencerFragment extends Fragment {

    private static String TAG = "sequencerFragment";

    private SequencerView sv;

    public SequencerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.seq_fragment, container, false);

        sv = (SequencerView) ((LinearLayout) rootView.findViewById(R.id.InnerRoll)).getChildAt(0);

        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.InnerRoll);

        /*if (ll != null) {
            for (int i= 0 ; i < ll.getChildCount(); i++) {
                if (ll.getChildAt(i) instanceof SequencerView)
                    sv = (SequencerView) ll.getChildAt(i);
                Log.d(TAG, "child is" + ll.getChildAt(i));
            }
        }*/


        for ( View v : rootView.getTouchables()) {
            Log.d(TAG, "id was " + Integer.toHexString(v.getId()));
        }

        Log.d(TAG, "We have a problem if " + (sv == null));


        Button loadButton = (Button) rootView.findViewById(R.id.loadSequencerRollButton);

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

}
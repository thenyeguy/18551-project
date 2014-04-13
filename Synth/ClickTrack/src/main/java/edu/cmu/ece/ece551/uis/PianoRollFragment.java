package edu.cmu.ece.ece551.uis;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import edu.cmu.ece.ece551.clicktrack.InstrumentController;
import edu.cmu.ece.ece551.clicktrack.SubtractiveSynthController;
import edu.cmu.ece.ece551.synth.R;
import edu.cmu.ece.ece551.uis.scale.ScaleType;

/**
 * Created by michaelryan on 2/23/14.
 */
public class PianoRollFragment extends Fragment {


    String startingNote = "C";
    ScaleType scaleType = ScaleType.DIATONIC_MAJOR;

    InstrumentController instrument;

    public PianoRollFragment() {
        instrument = SubtractiveSynthController.getInstance();
    }

    public PianoRollFragment(InstrumentController controller) {
        instrument = controller;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.roll_fragment, container, false);

        Button clearButton = (Button) rootView.findViewById(R.id.clearButton);

        Button playButton = (Button) rootView.findViewById(R.id.playButton);

        Button stopButton = (Button) rootView.findViewById(R.id.stopButton);
        Button octUpButton = (Button) rootView.findViewById(R.id.octaveUp);
        Button octDownButton = (Button) rootView.findViewById(R.id.octaveDown);

        final TextView octaveText = (TextView) rootView.findViewById(R.id.octaveText);


        final PianoRollView prv = (PianoRollView) rootView.findViewById(R.id.pianoRoll);
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


                prv.animate().cancel();
                prv.setTranslationX(0);
                prv.clearAnimation();

            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PianoRollView prv = (PianoRollView) rootView.findViewById(R.id.pianoRoll);

                TimerTask tt = new TimerTask() {
                    int x = 0;

                    @Override
                    public void run() {
                        x += 5;
                        if (x < 1589    ) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    prv.moveRect(x);
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    prv.moveRect(0);
                                }

                            });

                            this.cancel();
                        }
                    }
                };
                Timer t = new Timer();
                t.schedule(tt, 12, 12);

            }
        });


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prv.clearGrid();
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

        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Need something that controls playback, sets timer.
            }

        });


        octaveText.setText(Integer.toString(prv.getOctave()));
        return rootView;
    }
}

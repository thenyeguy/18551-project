package edu.cmu.ece.ece551.clicktrack;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.cmu.ece.ece551.synth.R;

public class TestClickTrack extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.test_click_track, container, false);


        // Set up buttons
        Button tone = (Button) rootView.findViewById(R.id.toneButton);
        tone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(),
                        SubtractiveSynthToneControls
                        .class);
                startActivity(intent);
            }
        });

        ToggleButton c = (ToggleButton) rootView.findViewById(R.id.cButton);
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

        ToggleButton e = (ToggleButton) rootView.findViewById(R.id.eButton);
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

        ToggleButton g = (ToggleButton) rootView.findViewById(R.id.gButton);
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

        // Configure timing test
        runningTimingTest = false;
        noteDur = 0;

        ToggleButton arpeggio = (ToggleButton) rootView.findViewById(R.id.scaleButton);
        arpeggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            timingTest();
                        }
                    }).start();
                } else {
                    runningTimingTest = false;
                }
            }
        });

        final TextView tempoText = (TextView) rootView.findViewById(R.id.tempoText);
        SeekBar tempo = (SeekBar) rootView.findViewById(R.id.tempoBar);
        tempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                noteDur = 4 * progress + 50;

                String text = "Tempo: " + String.format("%2f", 1000.0 / (noteDur)) + " notes per " +
                        "second";
                tempoText.setText(text.toCharArray(), 0, text.length());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
        tempo.setProgress(50);

        Button bassDrum = (Button) rootView.findViewById(R.id.bassButton);
        bassDrum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NativeClickTrack.DrumMachine.noteDown(NativeClickTrack.DrumMachine.DrumNotes.BASSDRUM1.value,
                        1.0f);
            }
        });
        
        Button snareDrum = (Button) rootView.findViewById(R.id.snareButton);
        snareDrum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NativeClickTrack.DrumMachine.noteDown(NativeClickTrack.DrumMachine.DrumNotes.SNAREDRUM1.value,
                        1.0f);
            }
        });
        
        Button hihat = (Button) rootView.findViewById(R.id.hihatButton);
        hihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NativeClickTrack.DrumMachine.noteDown(NativeClickTrack.DrumMachine.DrumNotes.CLOSEDHIHAT.value,
                        1.0f);
            }
        });

        return rootView;
    }

    private Boolean runningTimingTest;
    private long noteDur;
    private void timingTest()
    {
        runningTimingTest = true;
        int notes[] = {60, 64, 67, 72};
        while(runningTimingTest) {
            for(int note: notes) {
                try {
                    NativeClickTrack.SubtractiveSynth.noteDown(note, 0.7f);
                    Thread.sleep(noteDur - 9);
                    NativeClickTrack.SubtractiveSynth.noteUp(note, 0.7f);
                    Thread.sleep(9);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

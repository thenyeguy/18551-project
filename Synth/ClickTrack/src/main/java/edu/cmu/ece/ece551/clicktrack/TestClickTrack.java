package edu.cmu.ece.ece551.clicktrack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TestClickTrack extends Activity {

    private enum State { PLAYING, PAUSED }
    State state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_track);

        // Load the library
        Log.i("TestClickTrack", "Creating a new ClickTrack instance.");
        ClickTrack.loadLibray();
        ClickTrack.start();
        ClickTrack.play();
        state = State.PLAYING;

        // Configure the drum machine
        ClickTrack.DrumMachine.setVoice("/sdcard/ClickTrack/roland808/");


        // Set up buttons
        ToggleButton c = (ToggleButton) findViewById(R.id.cButton);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    ClickTrack.SubtractiveSynth.noteDown(60, 0.5f);
                } else {
                    ClickTrack.SubtractiveSynth.noteUp(60, 0.0f);
                }
            }
        });

        ToggleButton e = (ToggleButton) findViewById(R.id.eButton);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    ClickTrack.SubtractiveSynth.noteDown(64, 0.5f);
                } else {
                    ClickTrack.SubtractiveSynth.noteUp(64, 0.0f);
                }
            }
        });

        ToggleButton g = (ToggleButton) findViewById(R.id.gButton);
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    ClickTrack.SubtractiveSynth.noteDown(67, 0.5f);
                } else {
                    ClickTrack.SubtractiveSynth.noteUp(67, 0.0f);
                }
            }
        });

        // Configure timing test
        runningTimingTest = false;
        noteDur = 0;

        ToggleButton arpeggio = (ToggleButton) findViewById(R.id.scaleButton);
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

        final TextView tempoText = (TextView) findViewById(R.id.tempoText);
        SeekBar tempo = (SeekBar) findViewById(R.id.tempoBar);
        tempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                noteDur = 4 * progress + 50;

                String text = "Tempo: " + String.format("%2f",1000.0/(noteDur)) + " notes per " +
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

        Button bassDrum = (Button) findViewById(R.id.bassButton);
        bassDrum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickTrack.DrumMachine.noteDown(ClickTrack.DrumMachine.DrumNotes.BASSDRUM1.value,
                        1.0f);
            }
        });
        
        Button snareDrum = (Button) findViewById(R.id.snareButton);
        snareDrum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickTrack.DrumMachine.noteDown(ClickTrack.DrumMachine.DrumNotes.SNAREDRUM1.value,
                        1.0f);
            }
        });
        
        Button hihat = (Button) findViewById(R.id.hihatButton);
        hihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickTrack.DrumMachine.noteDown(ClickTrack.DrumMachine.DrumNotes.CLOSEDHIHAT.value,
                        1.0f);
            }
        });
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
                    ClickTrack.SubtractiveSynth.noteDown(note, 0.7f);
                    Thread.sleep(noteDur - 9);
                    ClickTrack.SubtractiveSynth.noteUp(note, 0.7f);
                    Thread.sleep(9);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Restart the engine, and if the library was currently playing, start playing again
        ClickTrack.start();
        if(state == State.PLAYING)
            ClickTrack.play();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // Stop the engine
        ClickTrack.stop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.click_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

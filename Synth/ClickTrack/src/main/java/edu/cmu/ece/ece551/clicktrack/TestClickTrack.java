package edu.cmu.ece.ece551.clicktrack;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TestClickTrack extends Activity {

    private ClickTrack master;
    private enum State { PLAYING, PAUSED }
    State state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_track);

        // Create our instance
        Log.i("TestClickTrack", "Creating a new ClickTrack instance.");
        master = new ClickTrack();
        state = State.PAUSED;

        // Register our buttons
        Button play = (Button) findViewById(R.id.playButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = State.PLAYING;
                master.play();
            }
        });

        Button pause = (Button) findViewById(R.id.pauseButton);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = State.PAUSED;
                master.pause();
            }
        });

        Button micOn = (Button) findViewById(R.id.micOnButton);
        micOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                master.setMicGain(0.5f);
            }
        });

        Button micOff = (Button) findViewById(R.id.micOffButton);
        micOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                master.setMicGain(0.0f);
            }
        });

        Button oscOn = (Button) findViewById(R.id.oscOnButton);
        oscOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                master.setOscGain(0.5f);
            }
        });

        Button oscOff = (Button) findViewById(R.id.oscOffButton);
        oscOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                master.setOscGain(0.0f);
            }
        });

        Button subSynthOn = (Button) findViewById(R.id.subSynthOnButton);
        subSynthOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                master.subtractiveSynth.setGain(0.5f);
            }
        });

        Button subSynthOff = (Button) findViewById(R.id.subSynthOffButton);
        subSynthOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                master.subtractiveSynth.setGain(0.0f);
            }
        });

        ToggleButton c = (ToggleButton) findViewById(R.id.cButton);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    master.subtractiveSynth.noteDown(60, 0.5f);
                } else {
                    master.subtractiveSynth.noteUp(60, 0.0f);
                }
            }
        });

        ToggleButton e = (ToggleButton) findViewById(R.id.eButton);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    master.subtractiveSynth.noteDown(64, 0.5f);
                } else {
                    master.subtractiveSynth.noteUp(64, 0.0f);
                }
            }
        });

        ToggleButton g = (ToggleButton) findViewById(R.id.gButton);
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()) {
                    master.subtractiveSynth.noteDown(67, 0.5f);
                } else {
                    master.subtractiveSynth.noteUp(67, 0.0f);
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
                    master.subtractiveSynth.noteDown(note, 0.7f);
                    Thread.sleep(noteDur - 9);
                    master.subtractiveSynth.noteUp(note, 0.7f);
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
        master.start();
        if(state == State.PLAYING)
            master.play();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // Stop the engine
        master.stop();
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

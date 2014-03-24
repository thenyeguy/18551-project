package edu.cmu.ece.ece551.clicktrack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SubtractiveSynthToneControl extends Activity {
    protected final String TAG = "ClickTrack_SubSynthToneControl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtractive_synth_tone_control);



        // Get our controller
        final SubtractiveSynthController controller = SubtractiveSynthController.getInstance();

        // Configure our gain
        final TextView gainText = (TextView) findViewById(R.id.subsynthGainText);
        String text = "Output gain (" + floatToString(NativeClickTrack.amplitudeToDb(controller
                .getOutputGain())) + "):";
        gainText.setText(text.toCharArray(), 0, text.length());

        final SeekBar gainSeekbar = (SeekBar) findViewById(R.id.subsynthGain);
        gainSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float db = (1f * i - 100) * 20 / 100;
                String text = "Output gain (" + floatToString(db) + "):";
                gainText.setText(text.toCharArray(), 0, text.length());
                controller.setOutputGain(NativeClickTrack.dbToAmplitude(db));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        // Set up our oscillator modes
        ArrayAdapter<CharSequence> oscModes = ArrayAdapter.createFromResource(this,
                R.array.oscillator_modes, android.R.layout.simple_spinner_item);
        oscModes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner osc1Spinner = (Spinner) findViewById(R.id.osc1mode);
        osc1Spinner.setAdapter(oscModes);
        osc1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String modeText = (String) adapterView.getItemAtPosition(i);
                controller.setOsc1mode(stringToOscMode(modeText));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Spinner osc2Spinner = (Spinner) findViewById(R.id.osc2mode);
        osc2Spinner.setAdapter(oscModes);
        osc2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String modeText = (String) adapterView.getItemAtPosition(i);
                controller.setOsc2mode(stringToOscMode(modeText));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        // Set up our ADSR envelope
        final TextView attackLabel = (TextView) findViewById(R.id.attackLabel);
        String attackString = "Attack (" + floatToString(controller.getAttack()) + "s)";
        attackLabel.setText(attackString.toCharArray(),0,attackString.length());

        final SeekBar attackSeekbar = (SeekBar) findViewById(R.id.attackSeekbar);
        attackSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float attack = (i/100f) * 5;

                String attackString = "Attack (" + floatToString(attack) + "s)";
                attackLabel.setText(attackString.toCharArray(),0,attackString.length());

                controller.setAttack(attack);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        final TextView decayLabel = (TextView) findViewById(R.id.decayLabel);
        String decayString = "Decay (" + floatToString(controller.getDecay()) + "s)";
        decayLabel.setText(decayString.toCharArray(),0,decayString.length());

        final SeekBar decaySeekbar = (SeekBar) findViewById(R.id.decaySeekbar);
        decaySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float decay = (i/100f) * 5;

                String decayString = "Decay (" + floatToString(decay) + "s)";
                decayLabel.setText(decayString.toCharArray(),0,decayString.length());

                controller.setDecay(decay);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        final TextView sustainLabel = (TextView) findViewById(R.id.sustainLabel);
        String sustainString = "Sustain (" + floatToString(controller.getSustain()) + "s)";
        sustainLabel.setText(sustainString.toCharArray(), 0, sustainString.length());

        final SeekBar sustainSeekbar = (SeekBar) findViewById(R.id.sustainSeekbar);
        sustainSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float sustain = (i / 100f);

                String sustainString = "Sustain (" + floatToString(sustain) + ")";
                sustainLabel.setText(sustainString.toCharArray(), 0, sustainString.length());

                controller.setSustain(sustain);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        final TextView releaseLabel = (TextView) findViewById(R.id.releaseLabel);
        String releaseString = "Release (" + floatToString(controller.getRelease()) + "s)";
        releaseLabel.setText(releaseString.toCharArray(),0,releaseString.length());

        final SeekBar releaseSeekbar = (SeekBar) findViewById(R.id.releaseSeekbar);
        releaseSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float release = (i/100f) * 5;

                String releaseString = "Release (" + floatToString(release) + "s)";
                releaseLabel.setText(releaseString.toCharArray(),0,releaseString.length());

                controller.setRelease(release);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private NativeClickTrack.SubtractiveSynth.OscillatorMode stringToOscMode(String modeText)
    {
        NativeClickTrack.SubtractiveSynth.OscillatorMode mode = NativeClickTrack.SubtractiveSynth
                .OscillatorMode.SINE;

        if(modeText.equals("Saw"))
            mode = NativeClickTrack.SubtractiveSynth.OscillatorMode.BLEPSAW;
        else if(modeText.equals("Square"))
            mode = NativeClickTrack.SubtractiveSynth.OscillatorMode.BLEPSQUARE;
        else if(modeText.equals("Triangle"))
            mode = NativeClickTrack.SubtractiveSynth.OscillatorMode.BLEPTRI;
        else if(modeText.equals("White Noise"))
            mode = NativeClickTrack.SubtractiveSynth.OscillatorMode.WHITENOISE;

        return mode;
    }

    private String floatToString(float f) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subtractive_synth_tone_control, menu);
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

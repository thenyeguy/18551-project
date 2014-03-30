package edu.cmu.ece.ece551.clicktrack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SubtractiveSynthToneControls extends Activity {
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
                .getOutputGain())) + "dB):";
        gainText.setText(text.toCharArray(), 0, text.length());

        final SeekBar gainSeekbar = (SeekBar) findViewById(R.id.subsynthGain);
        gainSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float db = (1f * i - 100) * 20 / 100;
                String text = "Output gain (" + floatToString(db) + "dB):";
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
        osc1Spinner.setSelection(oscModeToIndex(controller.getOsc1mode()));
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
        osc2Spinner.setSelection(oscModeToIndex(controller.getOsc2mode()));
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



        // Set up oscillator transpositions
        final int minTranspose = -24; final int maxTranspose = 24;

        NumberPicker osc1transpose = (NumberPicker) findViewById(R.id.osc1transpose);
        osc1transpose.setMinValue(0); osc1transpose.setMaxValue(maxTranspose-minTranspose);
        osc1transpose.setValue(0 - minTranspose);
        osc1transpose.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int index) {
                return Integer.toString(index + minTranspose);
            }
        });
        osc1transpose.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                controller.setOsc1transposition((float)i2 + minTranspose);
            }
        });


        NumberPicker osc2transpose = (NumberPicker) findViewById(R.id.osc2transpose);
        osc2transpose.setMinValue(0); osc2transpose.setMaxValue(maxTranspose-minTranspose);
        osc2transpose.setValue(0 - minTranspose);
        osc2transpose.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int index) {
                return Integer.toString(index + minTranspose);
            }
        });
        osc2transpose.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                controller.setOsc2transposition((float) i2 + minTranspose);
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


        // Set filter mode
        ArrayAdapter<CharSequence> filterModes = ArrayAdapter.createFromResource(this,
                R.array.filter_modes, android.R.layout.simple_spinner_item);
        filterModes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner filterModeSpinner = (Spinner) findViewById(R.id.filterModeSpinner);
        filterModeSpinner.setAdapter(filterModes);
        filterModeSpinner.setSelection(controller.getFilterMode().value);
        filterModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String modeText = (String) adapterView.getItemAtPosition(i);
                controller.setFilterMode(stringToFilterMode(modeText));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Set filter sliders
        final TextView filterCutoffLabel = (TextView) findViewById(R.id.filterCutoffLabel);
        String cutoffString = "Cutoff (" + floatToString(controller.getFilterCutoff()) + "Hz)";
        filterCutoffLabel.setText(cutoffString.toCharArray(), 0, cutoffString.length());

        final SeekBar cutoffSeekbar = (SeekBar) findViewById(R.id.filterCutoffSeekbar);
        cutoffSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float cutoff = ((float) Math.pow(10, (i / 100f)) - 1)/9 * 19980 + 20;

                String s = "Cutoff (" + floatToString(cutoff) + "Hz)";
                filterCutoffLabel.setText(s.toCharArray(), 0, s.length());

                controller.setFilterCutoff(cutoff);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        final TextView filterGainLabel = (TextView) findViewById(R.id.filterGainLabel);
        String gainString = "Gain (" + floatToString(controller.getFilterGain()) + "dB)";
        filterGainLabel.setText(gainString.toCharArray(), 0, gainString.length());

        final SeekBar filterGainSeekbar = (SeekBar) findViewById(R.id.filterGainSeekbar);
        filterGainSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float gain = (i / 100f)*40 - 20;

                String s = "Gain (" + floatToString(gain) + "dB)";
                filterGainLabel.setText(s.toCharArray(), 0, s.length());

                controller.setFilterGain(gain);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        final TextView filterQLabel = (TextView) findViewById(R.id.filterQLabel);
        String qString = "Q (" + floatToString(controller.getFilterQ()) + "Hz)";
        filterQLabel.setText(qString.toCharArray(), 0, qString.length());

        final SeekBar qSeekbar = (SeekBar) findViewById(R.id.filterQSeekbar);
        qSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float q = (i / 100f)*9.5f + 0.5f;

                String s = "Q (" + floatToString(q) + ")";
                filterQLabel.setText(s.toCharArray(), 0, s.length());

                controller.setFilterQ(q);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // Place a test button
        Button testButton = (Button) findViewById(R.id.toneTestButton);
        testButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN)
                    NativeClickTrack.SubtractiveSynth.noteDown(60, 1.0f);
                else if (action == MotionEvent.ACTION_UP)
                    NativeClickTrack.SubtractiveSynth.noteUp(60, 0.0f);
                return false;   //  the listener has NOT consumed the event, pass it on
            }
        });


        // Unfocus the spinner on launch
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

    private int oscModeToIndex(NativeClickTrack.SubtractiveSynth.OscillatorMode mode) {
        switch(mode) {
            case SINE:
                return 0;
            case SAW:
            case BLEPSAW:
                return 1;
            case SQUARE:
            case BLEPSQUARE:
                return 2;
            case TRI:
            case BLEPTRI:
                return 3;
            case WHITENOISE:
                return 4;
        }
        return 0;
    }

    private NativeClickTrack.SubtractiveSynth.FilterMode stringToFilterMode(String modeText)
    {
        NativeClickTrack.SubtractiveSynth.FilterMode mode = NativeClickTrack.SubtractiveSynth
                .FilterMode.LOWPASS;

        if(modeText.equals("Low shelf"))
            mode = NativeClickTrack.SubtractiveSynth.FilterMode.LOWSHELF;
        else if(modeText.equals("High pass"))
            mode = NativeClickTrack.SubtractiveSynth.FilterMode.HIGHPASS;
        else if(modeText.equals("High shelf"))
            mode = NativeClickTrack.SubtractiveSynth.FilterMode.HIGHSHELF;
        else if(modeText.equals("Peak"))
            mode = NativeClickTrack.SubtractiveSynth.FilterMode.PEAK;

        return mode;
    }

    private String floatToString(float f) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NativeClickTrack.addReference();
    }
    @Override
    protected void onPause() {
        super.onPause();
        NativeClickTrack.removeReference();
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

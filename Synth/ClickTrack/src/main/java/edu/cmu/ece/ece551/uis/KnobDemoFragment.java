package edu.cmu.ece.ece551.uis;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.cmu.ece.ece551.synth.R;

/**
 * Created by michaelryan on 3/28/14.
 */
public class KnobDemoFragment extends Fragment {


    public KnobDemoFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.knob_fragment, container, false);


        return rootView;

    }

}

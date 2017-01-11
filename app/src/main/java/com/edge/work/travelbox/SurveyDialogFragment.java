package com.edge.work.travelbox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SurveyDialogFragment extends DialogFragment {

    Button submit;
    FragmentManager fm;

    public SurveyDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_survey_dialog, container, false);
        fm=getFragmentManager();
        submit = (Button) rootview.findViewById(R.id.survey_submit);
        getDialog().setTitle("Tell us something");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStack();
                getDialog().dismiss();
            }
        });

        return rootview;
    }
}

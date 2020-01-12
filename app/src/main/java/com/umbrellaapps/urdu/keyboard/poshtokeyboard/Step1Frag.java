package com.umbrellaapps.urdu.keyboard.poshtokeyboard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.umbrellaapps.urdu.keyboard.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Step1Frag extends Fragment {


    public Step1Frag() {
        // Required empty public constructor
    }

    Button btn_enable_setting, circle_btn_Step1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step1, container, false);
        btn_enable_setting = (Button) view.findViewById(R.id.btn_step1);
        btn_enable_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS), 0);

            }
        });
        return view;
    }

}

package com.umbrellaapps.urdu.keyboard.poshtokeyboard;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.umbrellaapps.urdu.keyboard.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Step2Frag extends Fragment {


    public Step2Frag() {
        // Required empty public constructor
    }

    Button btn_input_method;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_step2, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("Checking", MODE_PRIVATE);
        String restoredText = prefs.getString("Keyboard", null);
        if (restoredText != null) {
           Intent intent = new Intent(getActivity(),HomeActivity.class);
           getActivity().startActivity(intent);
        }

        btn_input_method = (Button) view.findViewById(R.id.btn_step2);
        btn_input_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInputEnabled()) {
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showInputMethodPicker();
                    Log.d("chk", "onClick:input method ");
                } else {
                    Toast.makeText(getContext(), "Please enable keyboard first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public boolean isInputEnabled() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties = imm.getEnabledInputMethodList();

        final int N = mInputMethodProperties.size();
        boolean isInputEnabled = false;

        for (int i = 0; i < N; i++) {

            InputMethodInfo imi = mInputMethodProperties.get(i);
            Log.d("INPUT ID", String.valueOf(imi.getId()));
            if (imi.getId().contains(getActivity().getPackageName())) {
                Log.d("chk", "isInputEnabled: ");
                isInputEnabled = true;
            }
        }

        if (isInputEnabled) {
            Log.d("chk", "isInputEnabled: ");
            Intent a = new Intent(getContext(), HomeActivity.class);
            startActivity(a);
            return true;
        } else {
            return false;
        }
    }

}

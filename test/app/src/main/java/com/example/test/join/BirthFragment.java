package com.example.test.join;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.test.R;

public class BirthFragment extends Fragment {

    ImageView btn_back;
    EditText edt_pw;
    Button btn_next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_birth, container, false);

        btn_back = rootView.findViewById(R.id.btn_back);
        edt_pw = rootView.findViewById(R.id.edt_pw);
        btn_next = rootView.findViewById(R.id.btn_next);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        return rootView;
    }
}
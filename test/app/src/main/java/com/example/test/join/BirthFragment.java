package com.example.test.join;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.test.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BirthFragment extends Fragment {

    TextView tv_bir;
    LinearLayout linear_bir;
    DatePickerDialog.OnDateSetListener callbackMethod;
    Date born;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_birth, container, false);



        tv_bir = rootView.findViewById(R.id.tv_bir);
        linear_bir = rootView.findViewById(R.id.linear_bir);


        //오늘날짜 받아서
        Calendar today = Calendar.getInstance();
        //세팅함
        tv_bir.setText(today.get(Calendar.YEAR) + "년" + (today.get(Calendar.MONTH)+1) + "월" + today.get(Calendar.DATE) + "일");

        //얘가 날짜값을 받아서 세팅해주는 역할
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                today.set(year, month, dayOfMonth);
                tv_bir.setText(year + "년" + (month + 1) + "월" + dayOfMonth + "일");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    born = format.parse(tv_bir.getText().toString());
                    JoinMainActivity.vo.setBorn(born);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        linear_bir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), callbackMethod, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
                dialog.show();
            }
        });

        return rootView;
    }
}
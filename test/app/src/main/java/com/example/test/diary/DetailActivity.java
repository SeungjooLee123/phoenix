package com.example.test.diary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.test.R;
import com.example.test.common.AskTask;
import com.example.test.common.CommonMethod;
import com.example.test.common.CommonVal;
import com.example.test.join.JoinMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    Button btn_cancel, btn_save, btn_del;
    TextView tv_start,tv_end, tv_date, tv_state;
    EditText edt_memo,edt_amount, edt_temp;
    TimePickerDialog.OnTimeSetListener callbackMethod1, callbackMethod2;
    LinearLayout linear_start, linear_end, linear_amount, linear_temp, linear_many;
    ArrayList<Button> btns = new ArrayList<>();
    Gson gson = new Gson();
    String[] time_arr1;
    String[] time_arr2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);
        btn_del = findViewById(R.id.btn_del);
        tv_start = findViewById(R.id.tv_start);
        tv_end = findViewById(R.id.tv_end);
        tv_state = findViewById(R.id.tv_state);

        btns.add(findViewById(R.id.btn1));
        btns.add(findViewById(R.id.btn2));
        btns.add(findViewById(R.id.btn3));
        btns.add(findViewById(R.id.btn4));

        tv_date = findViewById(R.id.tv_date);
        edt_amount = findViewById(R.id.edt_amount);
        edt_temp = findViewById(R.id.edt_temp);
        edt_memo = findViewById(R.id.edt_memo);

        linear_start = findViewById(R.id.linear_start);
        linear_end = findViewById(R.id.linear_end);
        linear_amount = findViewById(R.id.linear_amount);
        linear_temp = findViewById(R.id.linear_temp);
        linear_many = findViewById(R.id.linear_many);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_memo.getWindowToken(), 0);


        //어댑터에서 dto 받기
        Intent intent = getIntent();
        DiaryVO dto = (DiaryVO) intent.getSerializableExtra("dto");
        boolean is_info=false;
        if(intent.getSerializableExtra("is_info")!=null){
            is_info = (Boolean) intent.getSerializableExtra("is_info");
        }


        String[] amtCategoty = {"분유", "이유식", "물", "투약", "간식"};

        if((dto.getBaby_category()).equals("모유")){
            linear_amount.setVisibility(View.GONE);
            linear_temp.setVisibility(View.GONE);
            btns.get(0).setText("왼쪽");
            btns.get(1).setText("오른쪽");
            btns.get(2).setText("둘다");
            btns.get(3).setText("모름");
        }else if((dto.getBaby_category()).equals("분유")){
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("이유식")){
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("기저귀")){
            linear_amount.setVisibility(View.GONE);
            linear_end.setVisibility(View.GONE);
            linear_temp.setVisibility(View.GONE);
            btns.get(0).setText("대변");
            btns.get(1).setText("소변");
            btns.get(2).setText("둘다");
            btns.get(3).setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("수면")){
            linear_amount.setVisibility(View.GONE);
            linear_temp.setVisibility(View.GONE);
            btns.get(0).setText("낮잠");
            btns.get(1).setText("밤잠");
            btns.get(2).setVisibility(View.GONE);
            btns.get(3).setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("목욕")){
            linear_amount.setVisibility(View.GONE);
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("체온")){
            linear_end.setVisibility(View.GONE);
            linear_amount.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("물")){
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("투약")){
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("간식")){
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }

        //초기 세팅
        tv_state.setText(dto.getBaby_category());
        String time = getNowtime();



        if(is_info){
            time_arr1 = (dto.getStart_time()).split(":");
            if(dto.getEnd_time() == null){
                time_arr2 = (time).split(":");
            }else {
                time_arr2 = (dto.getEnd_time()).split(":");
            }
            edt_temp.setText(dto.getTemperature()+"");
            edt_amount.setText(dto.getAmount()+"");
            edt_memo.setText(dto.getMemo());
        }else{
            time_arr1 = (time).split(":");
            time_arr2 = (time).split(":");
            dto.setStart_time(time);
            dto.setEnd_time(time);
            btn_del.setVisibility(View.GONE);
        }

        tv_date.setText(dto.getDiary_date()+"");
        tv_start.setText(dto.getStart_time()+"");
        tv_end.setText(dto.getEnd_time()+"");

        for(int i=0 ; i<btns.size(); i++){
            if(btns.get(i).getText().equals(dto.getDiary_type())){
                changeBtn(i);
            }
        }



        callbackMethod1 = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                String strdate = "";

                if(hourOfDay<10){
                    strdate += "0" + hourOfDay;
                }else{
                    strdate += hourOfDay;
                }
                if(minute<10){
                    strdate +=":0" + minute;
                }else{
                    strdate +=":" + minute;
                }

                dto.setStart_time(strdate);
                tv_start.setText(strdate);
            }
        };

        callbackMethod2 = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                String strdate = "";

                if(hourOfDay<10){
                    strdate += "0" + hourOfDay;
                }else{
                    strdate += hourOfDay;
                }
                if(minute<10){
                    strdate +=":0" + minute;
                }else{
                    strdate +=":" + minute;
                }

                dto.setEnd_time(strdate);
                tv_end.setText(strdate);
            }
        };

        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(DetailActivity.this, callbackMethod1, Integer.parseInt(time_arr1[0]), Integer.parseInt(time_arr1[1]), true);
                dialog.show();
            }
        });

        tv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(DetailActivity.this, callbackMethod2, Integer.parseInt(time_arr2[0]), Integer.parseInt(time_arr2[1]), true);
                dialog.show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //저장버튼
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dto.setMemo(edt_memo.getText()+"");
                for(int i=0; i<amtCategoty.length; i++){
                    if(dto.getBaby_category().equals(amtCategoty[i])){
                        dto.setAmount(Double.parseDouble(edt_amount.getText()+""));
                    }
                }
                if(dto.getBaby_category().equals("체온")){
                    dto.setTemperature(Double.parseDouble(edt_temp.getText()+""));
                }
                if(intent.getSerializableExtra("is_info") != null){
                    AskTask task = new AskTask(CommonVal.httpip,"update.di");
                    String dtogson = gson.toJson(dto);
                    task.addParam("dto", dtogson);
                    InputStream in = CommonMethod.excuteGet(task);
                    Boolean isSucc = gson.fromJson(new InputStreamReader(in), Boolean.class);
                }else{
                    AskTask task = new AskTask(CommonVal.httpip,"insert.di");
                    String dtogson = gson.toJson(dto);
                    task.addParam("dto", dtogson);
                    InputStream in = CommonMethod.excuteGet(task);
                    Boolean isSucc = gson.fromJson(new InputStreamReader(in), Boolean.class);
                }


                //Log.d("isSucc : ", isSucc+"");
                Intent intent = new Intent();
                intent.putExtra("pageDate",dto.getDiary_date());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder_cancel = new AlertDialog.Builder(DetailActivity.this).setTitle("취소").setMessage("정말 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AskTask task = new AskTask(CommonVal.httpip,"delete.di");
                                String dtogson = gson.toJson(dto);
                                task.addParam("dto", dtogson);
                                InputStream in = CommonMethod.excuteGet(task);
                                Boolean isSucc = gson.fromJson(new InputStreamReader(in), Boolean.class);
                                //Log.d("isSucc : ", isSucc+"");
                                Intent intent = new Intent();
                                intent.putExtra("pageDate",dto.getDiary_date());
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog = builder_cancel.create();
                alertDialog.show();

            }
        });

        btns.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBtn(0);
                dto.setDiary_type(btns.get(0).getText()+"");
            }
        });
        btns.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBtn(1);
                dto.setDiary_type(btns.get(1).getText()+"");
            }
        });
        btns.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBtn(2);
                dto.setDiary_type(btns.get(2).getText()+"");
            }
        });
        btns.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBtn(3);
                dto.setDiary_type(btns.get(3).getText()+"");
            }
        });
    }

    public String getNowtime(){
        //현재 시간
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String getTime = dateFormat.format(date);
        return  getTime;
    }

    public void changeBtn(int num){
        for(int i=0; i<btns.size(); i++){
            if(i==num){
                btns.get(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#44526C")));
                btns.get(i).setTextColor(Color.parseColor("#ffffff"));
            }else{
                btns.get(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2E2E2")));
                btns.get(i).setTextColor(Color.parseColor("#000000"));
            }
        }
    }
}
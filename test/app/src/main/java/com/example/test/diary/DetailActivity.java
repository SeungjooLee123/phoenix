package com.example.test.diary;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.test.LoginActivity;
import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.SplashActivity;
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
import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    Button btn_cancel, btn_save;
    ImageView btn_del;
    TextView tv_start,tv_end, tv_date, tv_state;
    EditText edt_memo,edt_amount, edt_temp;
    TimePickerDialog.OnTimeSetListener callbackMethod1, callbackMethod2;
    LinearLayout linear_start, linear_end, linear_amount, linear_temp, linear_many;
    ArrayList<Button> btns = new ArrayList<>();

    Gson gson = new Gson();
    String[] time_arr1;
    String[] time_arr2;
    int result = 1;

    String[] amtCategoty = {"??????", "?????????", "???", "??????", "??????", "??????"};

    private ActivityResultLauncher<Void> overlayPermissionLauncher;

    NotificationManager manager;

    //????????? ????????? ????????? ????????? ?????????
    String CHANNEL_ID1 = "channer1";
    String CHANNEL_NAME1 = "channer1";
    String CHANNEL_ID2 = "channer2";
    String CHANNEL_NAME2 = "channer2";
    String CHANNEL_ID3 = "channer3";
    String CHANNEL_NAME3 = "channer3";

    @RequiresApi(api = Build.VERSION_CODES.M)
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


        //??????????????? dto ??????
        Intent intent = getIntent();
        DiaryVO dto = (DiaryVO) intent.getSerializableExtra("dto");
        boolean is_info=false;
        if(intent.getSerializableExtra("is_info")!=null){
            is_info = (Boolean) intent.getSerializableExtra("is_info");
        }



        if((dto.getBaby_category()).equals("??????")){
            //linear_amount.setVisibility(View.GONE);
            linear_temp.setVisibility(View.GONE);
            btns.get(0).setText("??????");
            btns.get(1).setText("?????????");
            btns.get(2).setText("??????");
            btns.get(3).setText("??????");
        }else if((dto.getBaby_category()).equals("??????")){
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("?????????")){
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("?????????")){
            linear_amount.setVisibility(View.GONE);
            linear_end.setVisibility(View.GONE);
            linear_temp.setVisibility(View.GONE);
            btns.get(0).setText("??????");
            btns.get(1).setText("??????");
            btns.get(2).setText("??????");
            btns.get(3).setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("??????")){
            linear_amount.setVisibility(View.GONE);
            linear_temp.setVisibility(View.GONE);
            btns.get(0).setText("??????");
            btns.get(1).setText("??????");
            btns.get(2).setVisibility(View.GONE);
            btns.get(3).setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("??????")){
            linear_amount.setVisibility(View.GONE);
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("??????")){
            linear_end.setVisibility(View.GONE);
            linear_amount.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("???")){
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("??????")){
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }else if((dto.getBaby_category()).equals("??????")){
            linear_temp.setVisibility(View.GONE);
            linear_many.setVisibility(View.GONE);
        }

        //?????? ??????
        tv_state.setText(dto.getBaby_category());
        String time = getNowtime();



        if(is_info){
            time_arr1 = (dto.getStart_time()).split(":");
            if(dto.getEnd_time() == null){
                time_arr2 = (time).split(":");
            }else {
                time_arr2 = (dto.getEnd_time()).split(":");
            }
            for(int i=0; i<amtCategoty.length; i++){
                if(dto.getBaby_category().equals(amtCategoty[i])){
                    if(dto.getAmount() == 0){
                        edt_amount.setText("");
                    }else {
                        edt_amount.setText(dto.getAmount() + "");
                    }
                }
            }
            if(dto.getBaby_category().equals("??????")){
                edt_temp.setText(dto.getTemperature()+"");
            }
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
                dto.setStart_time(String.format("%02d", hourOfDay ) +":"+ String.format("%02d", minute ));
                tv_start.setText(String.format("%02d", hourOfDay ) +":"+ String.format("%02d", minute ));
                time_arr1[0] = String.format("%02d", hourOfDay );
                time_arr1[1] = String.format("%02d", minute );
            }
        };

        callbackMethod2 = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                dto.setEnd_time(String.format("%02d", hourOfDay ) +":"+ String.format("%02d", minute ));
                tv_end.setText(String.format("%02d", hourOfDay ) +":"+ String.format("%02d", minute ));
                time_arr2[0] = String.format("%02d", hourOfDay );
                time_arr2[1] = String.format("%02d", minute );
            }
        };

        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(DetailActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, callbackMethod1, Integer.parseInt(time_arr1[0]), Integer.parseInt(time_arr1[1]), false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });

        tv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(DetailActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar, callbackMethod2, Integer.parseInt(time_arr2[0]), Integer.parseInt(time_arr2[1]), false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //????????????
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isSucc = false;
                dto.setMemo(edt_memo.getText() + "");
                //amount??? ????????? ?????????????????? ????????? ??????
                for (int i = 0; i < amtCategoty.length; i++) {
                    if (dto.getBaby_category().equals(amtCategoty[i])) {
                        if (edt_amount.getText().toString().equals("")) {
                        } else {
                            dto.setAmount(Double.parseDouble(edt_amount.getText() + ""));
                        }
                    }
                }
                //???????????? ???????????? ??????????????? ?????? ????????? ???????????? ???????????? ???????????? ????????? ??????
                if (dto.getBaby_category().equals("??????")) {
                    if (edt_temp.getText().toString().equals("")) {
                        result = 0;
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                        builder.setTitle("????????? ??????????????????").setMessage("");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else if(35.5>Double.parseDouble(edt_temp.getText().toString()) || 38<Double.parseDouble(edt_temp.getText().toString())){
                        showCheck();
                        result = 1;
                        dto.setTemperature(Double.parseDouble(edt_temp.getText() + ""));
                    }else {
                        result = 1;
                        dto.setTemperature(Double.parseDouble(edt_temp.getText() + ""));
                    }
                    dto.setEnd_time(dto.getStart_time());
                //??????????????? ???????????? ??????
                } else if (dto.getBaby_category().equals("?????????")) {
                    dto.setEnd_time(dto.getStart_time());
                }
                //???????????? ????????? ???????????????
                long diffMin = 0;
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    Date date1 = dateFormat.parse(dto.getEnd_time());
                    Date date2 = dateFormat.parse(dto.getStart_time());
                    diffMin = (date1.getTime() - date2.getTime()) / 60000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //?????????????????? ???????????? ????????? ???????????? ??????????????? ?????????
                if(diffMin<0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                    builder.setTitle("????????? ?????? ?????????????????????.").setMessage("");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id) { }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                //?????? ????????? ???????????????
                }else{
                    //????????????
                    if (intent.getSerializableExtra("is_info") != null && result == 1) {
                        AskTask task = new AskTask(CommonVal.httpip, "update.di");
                        String dtogson = gson.toJson(dto);
                        task.addParam("dto", dtogson);
                        InputStream in = CommonMethod.excuteGet(task);
                        isSucc = gson.fromJson(new InputStreamReader(in), Boolean.class);
                    //????????? ????????? ????????????
                    } else if (result == 1) {
                        AskTask task = new AskTask(CommonVal.httpip, "insert.di");
                        String dtogson = gson.toJson(dto);
                        task.addParam("dto", dtogson);
                        InputStream in = CommonMethod.excuteGet(task);
                        isSucc = gson.fromJson(new InputStreamReader(in), Boolean.class);
                    }
                    //???????????? ??????????????? ??? ????????????
                    if (isSucc) {
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            //?????? ???????????? ????????? ??????????????? ????????? ????????? ????????? ?????? ?????????
                            Date now = dateFormat.parse(dateFormat.format(new Date()));
                            Date setTime = dateFormat.parse(dto.getDiary_date()+" "+dto.getStart_time());
                            if(setTime.getTime() > now.getTime()){
                                Calendar c = Calendar.getInstance();
                                c.setTime(setTime);
                                if(checkPermissionOverlay(DetailActivity.this)) {
                                    setAlarm(c, dto.getBaby_category());
                                }else{
                                    Toast.makeText(DetailActivity.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //??????????????? ?????? ????????? ??????????????? ????????????
                        Intent intent = new Intent();
                        intent.putExtra("pageDate", dto.getDiary_date());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder_cancel = new AlertDialog.Builder(DetailActivity.this).setTitle("??????").setMessage("?????? ?????????????????????????")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
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
                        }).setNegativeButton("?????????", new DialogInterface.OnClickListener() {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlarm(Calendar calendar, String cate) {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        for(int i=0; i< amtCategoty.length; i++){
            if(cate.equals(amtCategoty[i]))
                intent.putExtra("cate" , cate.equals("??????") ? "???????????? ????????? ???????????????." : cate + "???(???) ?????? ???????????????.");//???????????? ????????? ????????? ?????????
        }
        if(cate.equals("??????"))
            intent.putExtra("cate" , cate + "??? ????????? ???????????????.");//???????????? ????????? ????????? ?????????
        else if(cate.equals("?????????"))
            intent.putExtra("cate" , cate + "??? ????????? ???????????????.");//???????????? ????????? ????????? ?????????
        else if(cate.equals("??????"))
            intent.putExtra("cate" , "????????? ?????? ???????????????.");//???????????? ????????? ????????? ?????????
        else if(cate.equals("??????"))
            intent.putExtra("cate" , "????????? ????????? ???????????????.");//???????????? ????????? ????????? ?????????
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_UPDATE_CURRENT
        );
        // ?????? ??????
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }

    private final int OVERRAY_REQ_CODE = 1111;
    public  boolean checkPermissionOverlay(Context context) {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && Settings.canDrawOverlays(context) ){ //<=????????? ????????????????????? ??????
            return true;
        }else{
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent,  OVERRAY_REQ_CODE);
        }
        return false;
    }

    private void showCheck() {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(manager.getNotificationChannel(CHANNEL_ID2) == null){
                manager.createNotificationChannel(new NotificationChannel(
                        CHANNEL_ID2,CHANNEL_NAME2,NotificationManager.IMPORTANCE_DEFAULT
                ));
            }
            builder = new NotificationCompat.Builder(this,CHANNEL_ID2);
        }else{
            builder = new NotificationCompat.Builder(this);
        }
        Intent intent = new Intent(DetailActivity.this , LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(DetailActivity.this , 1004 ,
                        intent ,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("????????? ??????");
        builder.setContentText("????????? ????????? ??????????????? ??????????????????.");
        builder.setSubText("(?????? ??????: 35.5??? ~ 38???)");
        builder.setSmallIcon(android.R.drawable.ic_menu_view);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        Notification noti = builder.build();

        manager.notify(2,noti);

    }



    public String getNowtime(){
        //?????? ??????
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String getTime = dateFormat.format(date);
        return  getTime;
    }

    public void changeBtn(int num){
        for(int i=0; i<btns.size(); i++){
            if(i==num){
                btns.get(i).setBackground(ContextCompat.getDrawable(this, R.drawable.border_round_gray_fill));
//btns.get(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D1D1D1")));
            }else{
                btns.get(i).setBackground(ContextCompat.getDrawable(this, R.drawable.border_round_gray));
            }
        }
    }
}
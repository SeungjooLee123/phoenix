package com.example.test.my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.LoginActivity;
import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.common.AskTask;
import com.example.test.common.CommonMethod;
import com.example.test.common.CommonVal;
import com.example.test.home.HomeActivity;
import com.example.test.join.UserVO;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SettingActivity extends AppCompatActivity {
    ImageView setting_back;
    TextView set_secession, set_logout;
    SwitchCompat set_bell, set_vibration;
    SeekBar set_bell_volume, set_vibration_volume;
    int bell_volume;
    int vib_volume;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settimg);

        setting_back = findViewById(R.id.setting_back);
        set_secession = findViewById(R.id.set_secession);
        set_logout = findViewById(R.id.set_logout);
        set_bell = findViewById(R.id.set_bell);
        set_vibration = findViewById(R.id.set_vibration);
        set_bell_volume = findViewById(R.id.set_bell_volume);
        set_vibration_volume = findViewById(R.id.set_vibration_volume);

        //????????? ???????????? ????????????
        SharedPreferences preferences = getPreferences(SettingActivity.MODE_PRIVATE);
        boolean save_bell_check = preferences.getBoolean("bell_check", false);
        boolean save_vib_check = preferences.getBoolean("vib_check", false);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //?????? on?????? ????????? ??????
        if(save_bell_check){
            set_bell.setChecked(true);
            bell_volume = preferences.getInt("bell_size", 0);
            set_bell_volume.setProgress(bell_volume);
            set_bell_volume.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        } else{ //?????? off??? ????????? ??????
            set_bell.setChecked(false);
            set_bell_volume.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        //????????? on?????? ????????? ??????
        if(save_vib_check){
            set_vibration.setChecked(true);
            vib_volume = preferences.getInt("vib_size", 0);
            set_vibration_volume.setProgress(vib_volume);
            set_vibration_volume.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        } else{ //????????? off??? ????????? ??????
            set_vibration.setChecked(false);
            set_vibration_volume.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        //????????????
        setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveState();
                finish();
            }
        });

        //?????? ON/OFF
        set_bell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    set_bell_volume.setProgress(50);
                    set_bell_volume.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                } else{
                    set_bell_volume.setProgress(0);
                    set_bell_volume.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                }
            }
        });

        //?????? ON/OFF
        set_vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    set_vibration_volume.setProgress(127);
                    set_vibration_volume.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                } else{
                    set_vibration_volume.setProgress(0);
                    set_vibration_volume.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                }
            }
        });

        //?????? ?????? ??????
        set_bell_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { //?????? ???
                if(progress > 0) {
                    bell_volume = progress;
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, AudioManager.FLAG_PLAY_SOUND);
                    MediaPlayer mp = new MediaPlayer();
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    try {
                        mp.setDataSource(uri.toString());
                        mp.setAudioStreamType(AudioManager.STREAM_ALARM);
                        mp.setLooping(true);
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                    set_bell.setChecked(false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { //?????? ?????? ???

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { //?????? ???

            }
        });

        //?????? ?????? ??????
        set_vibration_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress > 0) {
                    vib_volume = progress;
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, progress));
                } else{
                    set_vibration.setChecked(false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //?????? ??????
        set_secession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder_secession = new AlertDialog.Builder(SettingActivity.this).setTitle("????????????").setMessage("????????? ????????? ?????? ???????????????.\n?????? ?????????????????????????")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AskTask task = new AskTask(CommonVal.httpip, "secession.bif");
                                task.addParam("id", CommonVal.curuser.getId());
                                InputStream in = CommonMethod.excuteGet(task);
                                if(gson.fromJson(new InputStreamReader(in), Boolean.class)){
                                    Toast.makeText(SettingActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                    CommonVal.curuser = new UserVO();
                                    CommonVal.baby_list.clear();
                                    CommonVal.curbaby = new BabyInfoVO();
                                    CommonVal.family_title.clear();
                                    if(LoginActivity.editor != null) {
                                        LoginActivity.editor.remove("autologin");
                                        LoginActivity.editor.remove("id");
                                        LoginActivity.editor.remove("pw");
                                        LoginActivity.editor.apply();
                                    }
                                    Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                    MainActivity.mainActivity.finish();
                                }
                            }
                        }).setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog = builder_secession.create();
                alertDialog.show();
            }
        });

        //????????????
        set_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder_logout = new AlertDialog.Builder(SettingActivity.this).setTitle("????????????").setMessage("?????? ???????????? ???????????????????")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //????????? ???????????? ??????
                                CommonVal.curuser = new UserVO();
                                CommonVal.baby_list.clear();
                                CommonVal.curbaby = new BabyInfoVO();
                                CommonVal.family_title.clear();
                                if(LoginActivity.editor != null) {
                                    LoginActivity.editor.remove("autologin");
                                    LoginActivity.editor.remove("id");
                                    LoginActivity.editor.remove("pw");
                                    LoginActivity.editor.apply();
                                }
                                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                MainActivity.mainActivity.finish();
                            }
                        }).setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog = builder_logout.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveState();
        finish();
    }

    //?????? ?????? ??????
    void saveState(){
        try{
            SharedPreferences preferences = getPreferences(SettingActivity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            if(set_bell.isChecked()){
                editor.putBoolean("bell_check", true);
                editor.putInt("bell_size", bell_volume);
            } else{
                editor.remove("bell_size");
                editor.putBoolean("bell_check", false);
            }
            if(set_vibration.isChecked()){
                editor.putBoolean("vib_check", true);
                editor.putInt("vib_size", vib_volume);
            } else{
                editor.remove("vib_size");
                editor.putBoolean("vib_check", false);
            }
            editor.apply();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
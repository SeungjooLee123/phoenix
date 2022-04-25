package com.example.test;

import static android.Manifest.permission.RECORD_AUDIO;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.test.common.AskTask;
import com.example.test.common.CommonMethod;
import com.example.test.common.CommonVal;
import com.example.test.iot.MusicFragment;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class IotFragment extends Fragment {
    ImageView iot_capture, iot_recode, iot_white_noise;
    WebView iot_cctv;
    Gson gson = new Gson();

    MediaRecorder recorder;
    String filename;

    private static final String LOG_TAG = "AudioRecordTest";
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 200;

    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;

    private boolean isRecording = true;    // 현재 녹음 상태를 확인하기 위함.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_iot, container, false);


        iot_capture = rootView.findViewById(R.id.iot_capture);
        iot_recode = rootView.findViewById(R.id.iot_recode);
        iot_white_noise = rootView.findViewById(R.id.iot_white_noise);
        iot_cctv = rootView.findViewById(R.id.iot_cctv);

        iot_cctv.setWebViewClient(new WebViewClient());
        iot_cctv.getSettings().setLoadWithOverviewMode(true);
        iot_cctv.getSettings().setUseWideViewPort(true);

        int perissionCheck = ContextCompat.checkSelfPermission(getContext(), RECORD_AUDIO);

        if(perissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "오디오 권한 있음", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "이건 없음", Toast.LENGTH_SHORT).show();
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), RECORD_AUDIO)) {
                Toast.makeText(getContext(), "오디오 권한 설명 필요함", Toast.LENGTH_SHORT).show();
            }else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{RECORD_AUDIO}, REQUEST_AUDIO_PERMISSION_CODE);
            }
        }

        WebSettings webSettings = iot_cctv.getSettings();
        webSettings.setJavaScriptEnabled(true);

            File sdcard = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sdcard = ((MainActivity) getActivity()).getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            }else{
                sdcard = Environment.getExternalStorageDirectory();
            }
            String uuid = UUID.randomUUID().toString();
            File file = new File(sdcard, uuid + ".mp3");
            filename = file.getAbsolutePath();
            Log.d("태그","파일명"+filename);


        /*iot_cctv.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} " +
                        "img{width:100%25;} div{overflow: hidden;} </style></head>" +
                        "<body><div><img src='http://192.168.0.92:8000/stream.mjpeg'/></div></body></html>",
                "text/html", "UTF-8");
        iot_cctv.reload();*/


        iot_cctv.loadUrl("http://192.168.0.92:8000");

        iot_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cctv 캡쳐
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AskTask askTask = new AskTask(CommonVal.httpip, "iot_cap.io");
                        askTask.addParam("code", "pic");
                        CommonMethod.excuteGet(askTask);

                        AskTask img_task = new AskTask(CommonVal.httpip, "base_to_img.io");
                        InputStream in = CommonMethod.excuteGet(img_task);
                        String precode = gson.fromJson(new InputStreamReader(in), String.class);

                        byte[] encode = Base64.decode(precode, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encode, 0, encode.length);
                        Log.d("asd", "run: " + bitmap);
                        //iot_white_noise.setImageBitmap(bitmap);
                    }
                }).start();*/
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Date time = new Date();
                String current_tile = sdf.format(time);

                Request_Capture(iot_cctv, current_tile+"_capture");
            }
        });


        iot_recode.setOnClickListener(v -> {
            //음성 녹음
            Toast.makeText(getContext(), "클릭클릭", Toast.LENGTH_SHORT).show();
            if(isRecording) {
                isRecording = false;
                recordAudio();
                iot_recode.setImageResource(R.drawable.iot_mic_stop);
            }else {
                if(checkAudioPermission()) {
                    isRecording = true;
                    stopRecording();
                    iot_recode.setImageResource(R.drawable.icon_rec);
                }
            }
        });


        iot_white_noise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeFrag(new MusicFragment());
            }
        });

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if(grantResults.length >0) {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "Audio 권한을 사용자가 승인함", Toast.LENGTH_SHORT).show();
                    }else if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getContext(), "Audio 권한을 사용자가 거부함", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "아 진짜로~~", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
        }
    }

    private boolean checkAudioPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    public void recordAudio(){
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        recorder.setOutputFile(filename);

        try {
            recorder.prepare();
        }catch (Exception e) {
            e.printStackTrace();
        }
        recorder.start();
        Toast.makeText(getActivity(), "녹음 시작", Toast.LENGTH_SHORT).show();

    }

    public void stopRecording(){
        if(recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;

            Toast.makeText(getContext(), "녹음 중지임", Toast.LENGTH_SHORT).show();
        }
    }

    public void Request_Capture(WebView webView, String title){
        if(webView == null){
            return;
        }
        webView.buildDrawingCache();
        Bitmap bitmap = webView.getDrawingCache();
        FileOutputStream fos;

        File uploadFolder = Environment.getExternalStoragePublicDirectory("/DCIM/Camera/");

        if(!uploadFolder.exists()) uploadFolder.mkdir();

        String str_path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/DCIM/Camera";
        try{
            fos = new FileOutputStream(str_path + title + ".jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
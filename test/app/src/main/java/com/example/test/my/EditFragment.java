package com.example.test.my;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.test.MainActivity;
import com.example.test.OnBackPressedListenser;
import com.example.test.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EditFragment extends Fragment implements OnBackPressedListenser {
    Button my_rels, btn_man, btn_woman;
    LinearLayout edit_birth, cur_kg_btn, cur_cm_btn;
    TextView edit_ok, tv_birth;
    ImageView edit_cancel, edit_photo;
    EditText edit_name;
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
    private  DatePickerDialog.OnDateSetListener callbackMethod;
    private TimePickerDialog.OnTimeSetListener callbackTime;
    int y=0, m=0, d=0, h=0, mi=0;
    String[] span_item = {"카메라", "갤러리"};
    public final int CAMERA_CODE = 1004;
    public final int GELLARY_CODE = 1005;
    File imgFile = null;
    String imgFilePath = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit, container, false);


        my_rels = rootView.findViewById(R.id.my_rels);
        edit_birth = rootView.findViewById(R.id.edit_birth);
        tv_birth = rootView.findViewById(R.id.tv_birth);
        cur_kg_btn = rootView.findViewById(R.id.cur_kg_btn);
        cur_cm_btn = rootView.findViewById(R.id.cur_cm_btn);
        edit_cancel = rootView.findViewById(R.id.edit_cancel);
        edit_ok = rootView.findViewById(R.id.edit_ok);
        edit_name = rootView.findViewById(R.id.edit_name);
        btn_man = rootView.findViewById(R.id.btn_man);
        btn_woman = rootView.findViewById(R.id.btn_woman);
        edit_photo = rootView.findViewById(R.id.edit_photo);

        edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder_cancel = new AlertDialog.Builder(getContext()).setTitle("취소").setMessage("현재 수정하신 내용이 저장되지 않습니다.\n정말 취소하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((MainActivity)getActivity()).changeFrag(new MyFragment());
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


        edit_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dto 수정
                ((MainActivity)getActivity()).changeFrag(new MyFragment());
            }
        });

        my_rels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                RelsDialog dialog = new RelsDialog(getContext());
                //현재 관계 넘기기

                dialog.show();
                dialog.setDialogListener(new RelsDialog.DialogListener() {
                    @Override
                    public void onPositiveClick(String name) {
                        my_rels.setText(name);
                    }
                });
            }
        });

        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                y = year;
                m = monthOfYear + 1;
                d = dayOfMonth;

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                String time = sdf.format(new Date(System.currentTimeMillis()));
                String times[] = new String[2];
                times = time.split(":");
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), callbackTime, Integer.parseInt(times[0]), Integer.parseInt(times[1]), true);
                timePickerDialog.show();
            }
        };

        callbackTime = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                h = hourOfDay;
                mi = minute;
                tv_birth.setText(y + "년 " + m + "월 " + d + "일 " + h + "시 " + mi + "분");
            }
        };

        edit_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), callbackMethod, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        cur_kg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeFrag(new BodyFragment());
            }
        });

        cur_cm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeFrag(new BodyFragment());
            }
        });

        edit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_man.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#44526C")));
                btn_man.setTextColor(getResources().getColorStateList(R.color.white));
                btn_woman.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D6D6D6")));
                btn_woman.setTextColor(getResources().getColorStateList(R.color.black));
            }
        });

        btn_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_woman.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#44526C")));
                btn_woman.setTextColor(getResources().getColorStateList(R.color.white));
                btn_man.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D6D6D6")));
                btn_man.setTextColor(getResources().getColorStateList(R.color.black));
            }
        });
        return rootView;
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("업로드 방법 선택")
                .setSingleChoiceItems(span_item, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        if(span_item[index].equals("카메라")){
                            go_Camera();
                        }else{
                            go_gallery();
                        }
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void go_gallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser( intent , "Select Picture"   ) , GELLARY_CODE );
    }

    public void go_Camera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getContext().getPackageManager() ) != null){
            imgFile = null ;
            imgFile = createFile();

            if(imgFile != null){
                Uri imgUri = FileProvider.getUriForFile(
                        ((MainActivity)getActivity()).getApplicationContext(),
                        ((MainActivity)getActivity()).getApplicationContext().getPackageName()+".fileprovider",
                        imgFile
                );
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    intent.putExtra(MediaStore.EXTRA_OUTPUT , imgUri);
                }else{
                    intent.putExtra(MediaStore.EXTRA_OUTPUT , Uri.fromFile(imgFile));
                }
                startActivityForResult(intent , CAMERA_CODE);
            }
        }
    }

    public String getGalleryRealPath(Uri contentUri){
        String rtnPath = null;
        String[] paths = {MediaStore.Images.Media.DATA};//<=
        Cursor cursor = ((MainActivity)getActivity()).getContentResolver().query(contentUri , paths , null , null , null);
        if(cursor.moveToFirst()){
            int columns_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            rtnPath = cursor.getString(columns_index);
        }
        cursor.close();
        return rtnPath;
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_CODE && resultCode == MainActivity.RESULT_OK){
            Glide.with(getContext()).load(imgFilePath).into(edit_photo);
        }else if(requestCode == GELLARY_CODE && resultCode == MainActivity.RESULT_OK){
            Uri selectImageUri = data.getData();
            imgFilePath = getGalleryRealPath(selectImageUri);
            Glide.with(getContext()).load(imgFilePath).into(edit_photo);
        }
    }*/

    //파일 생성을 위한처리 .
    public File createFile(){
        String timeTemp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "My" + timeTemp ;
        File storageDir = ((MainActivity)getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File rtnFile = null ;
        try {
            rtnFile = File.createTempFile(imageFileName , ".jpg" , storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgFilePath = rtnFile.getAbsolutePath();
        return rtnFile;
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_MEDIA_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(getContext(), permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0])) {
                Toast.makeText(getContext(), "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder_cancel = new AlertDialog.Builder(getContext()).setTitle("취소").setMessage("현재 수정하신 내용이 저장되지 않습니다.\n정말 취소하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getActivity()).changeFrag(new MyFragment());
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alertDialog = builder_cancel.create();
        alertDialog.show();
    }
}
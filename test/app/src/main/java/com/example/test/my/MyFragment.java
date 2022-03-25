package com.example.test.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.common.AskTask;
import com.example.test.common.CommonMethod;
import com.example.test.common.CommonVal;
import com.example.test.diary.DiaryFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MyFragment extends Fragment{
    Button btn_co_parent, delete_baby;
    Spinner my_spinner;
    ImageView my_setting, my_detail, my_main_photo, my_diary_title_edit;
    TextView my_birth_tv, my_name_tv, my_diary_title, my_gender_man, my_gender_woman, baby_body;
    Gson gson = new Gson();
    List<BabyInfoVO> list;
    String[] titlelist = new String[CommonVal.family_title.size()];
    String title = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my, container, false);
        my_setting = rootView.findViewById(R.id.my_setting);
        my_detail = rootView.findViewById(R.id.my_detail);
        my_spinner = rootView.findViewById(R.id.my_spinner);
        my_main_photo = rootView.findViewById(R.id.my_main_photo);
        my_birth_tv = rootView.findViewById(R.id.my_birth_tv);
        my_name_tv = rootView.findViewById(R.id.my_name_tv);
        btn_co_parent = rootView.findViewById(R.id.btn_co_parent);
        delete_baby = rootView.findViewById(R.id.delete_baby);
        my_diary_title = rootView.findViewById(R.id.my_diary_title);
        my_diary_title_edit = rootView.findViewById(R.id.my_diary_title_edit);
        my_gender_man = rootView.findViewById(R.id.my_gender_man);
        my_gender_woman = rootView.findViewById(R.id.my_gender_woman);
        baby_body = rootView.findViewById(R.id.baby_body);

        list = CommonVal.baby_list;
        for(int i=0; i<titlelist.length; i++){
            titlelist[i] = CommonVal.family_title.get(i);
        }

        //육아일기 수정
        my_diary_title_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryTitleDialog dialog = new DiaryTitleDialog(getContext(), CommonVal.curbaby.getTitle());
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                Window window = dialog.getWindow();
                window.setAttributes(lp);
                dialog.setDialogListener(new DiaryTitleDialog.DialogListener() {
                    @Override
                    public void onPositiveClick(String name) {
                        AskTask task = new AskTask(CommonVal.httpip, "chTitle.bif");
                        task.addParam("title", name);
                        task.addParam("baby_id", CommonVal.curbaby.getBaby_id());
                        InputStream in = CommonMethod.excuteGet(task);
                        ((MainActivity)getActivity()).changeFrag(new MyFragment());
                    }
                });
            }
        });

        //아기 선택
        BabySelectAdapter babySelectAdapter = new BabySelectAdapter(list, inflater, getContext());
        my_spinner.setAdapter(babySelectAdapter);
        my_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == list.size()){
                    //아기 추가
                    InsertDialog(view);
                } else {
                    CommonVal.curbaby = list.get(position);

                    AskTask body_task = new AskTask(CommonVal.httpip, "cntbody.stor");
                    body_task.addParam("baby_id", list.get(position).getBaby_id());
                    InputStream in = CommonMethod.excuteGet(body_task);
                    String cntbody = gson.fromJson(new InputStreamReader(in), new TypeToken<String>(){}.getType());
                    baby_body.setText(cntbody);

                    my_diary_title.setText(list.get(position).getTitle());
                    if(list.get(position).getBaby_photo() == null){
                        my_main_photo.setImageResource(R.drawable.bss_logo);
                    } else{
                        Glide.with(getContext()).load(list.get(position).getBaby_photo()).into(my_main_photo);
                    }
                    if(list.get(position).getBaby_gender().equals("남아")){
                        my_gender_man.setBackground(getContext().getDrawable(R.drawable.tv_custom_select));
                        my_gender_man.setTextColor(Color.parseColor("#ffffff"));
                        my_gender_woman.setBackground(getContext().getDrawable(R.drawable.tv_custom));
                        my_gender_woman.setTextColor(Color.parseColor("#000000"));
                    } else if(list.get(position).getBaby_gender().equals("여아")){
                        my_gender_woman.setBackground(getContext().getDrawable(R.drawable.tv_custom_select));
                        my_gender_woman.setTextColor(Color.parseColor("#ffffff"));
                        my_gender_man.setBackground(getContext().getDrawable(R.drawable.tv_custom));
                        my_gender_man.setTextColor(Color.parseColor("#000000"));
                    } else{
                        my_gender_woman.setBackground(getContext().getDrawable(R.drawable.tv_custom));
                        my_gender_woman.setTextColor(Color.parseColor("#000000"));
                        my_gender_man.setBackground(getContext().getDrawable(R.drawable.tv_custom));
                        my_gender_man.setTextColor(Color.parseColor("#000000"));
                    }
                    my_birth_tv.setText(list.get(position).getBaby_birth().toString());
                    my_name_tv.setText(list.get(position).getBaby_name());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                CommonVal.curbaby = list.get(0);

                AskTask body_task = new AskTask(CommonVal.httpip, "cntbody.stor");
                body_task.addParam("baby_id", list.get(0).getBaby_id());
                InputStream in = CommonMethod.excuteGet(body_task);
                String cntbody = gson.fromJson(new InputStreamReader(in), new TypeToken<String>(){}.getType());
                baby_body.setText(cntbody);

                my_diary_title.setText(list.get(0).getTitle());
                if(list.get(0).getBaby_photo() == null){
                    my_main_photo.setImageResource(R.drawable.bss_logo);
                } else{
                    Glide.with(getContext()).load(list.get(0).getBaby_photo()).into(my_main_photo);
                }
                my_birth_tv.setText(list.get(0).getBaby_birth().toString());
                my_name_tv.setText(list.get(0).getBaby_name());
            }
        });

        //알림, 진동 설정
        my_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        //아기 정보 수정
        my_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditFragment(CommonVal.curbaby);
                ((MainActivity)getActivity()).backFrag(new EditFragment(CommonVal.curbaby));
                ((MainActivity)getActivity()).changeFrag(fragment);
            }
        });

        //공동양육자 버튼 선택
        btn_co_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskTask task = new AskTask(CommonVal.httpip, "coparent.bif");
                task.addParam("baby_id", CommonVal.curbaby.getBaby_id());
                InputStream in = CommonMethod.excuteGet(task);
                List<FamilyInfoVO> coparent = gson.fromJson(new InputStreamReader(in), new TypeToken<List<FamilyInfoVO>>(){}.getType());
                ((MainActivity)getActivity()).backFrag(new CoParentFragment(coparent));
                ((MainActivity)getActivity()).changeFrag(new CoParentFragment(coparent));
            }
        });

        //아이 정보 삭제
        delete_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder_delete = new AlertDialog.Builder(getContext()).setTitle("아이 정보 삭제").setMessage("현재까지 기록한 아이 기록이 모두 사라집니다.\n정말 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AskTask task_delete = new AskTask(CommonVal.httpip, "babydel.bif");
                                task_delete.addParam("baby_id", CommonVal.curbaby.getBaby_id());
                                InputStream in = CommonMethod.excuteGet(task_delete);
                                if(gson.fromJson(new InputStreamReader(in), new TypeToken<Boolean>(){}.getType())){
                                    Toast.makeText(getContext(), "아기 정보가 성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    //아기 목록 다시 불러오기
                                    AskTask task_re = new AskTask(CommonVal.httpip, "list.bif");
                                    task_re.addParam("id", CommonVal.curuser.getId());
                                    InputStream in_re = CommonMethod.excuteGet(task_re);
                                    CommonVal.baby_list = gson.fromJson(new InputStreamReader(in_re), new TypeToken<List<BabyInfoVO>>(){}.getType());

                                    AskTask task = new AskTask(CommonVal.httpip, "countbaby.bif");
                                    task.addParam("title", title);
                                    InputStream in_select = CommonMethod.excuteGet(task);
                                    if(gson.fromJson(new InputStreamReader(in_select), new TypeToken<Boolean>(){}.getType())){ //육아일기에 아기가 남아있을 때
                                        CommonVal.curbaby = CommonVal.baby_list.get(0);
                                        ((MainActivity)getActivity()).changeFrag(new MyFragment());
                                    } else{ //육아일기에 아기가 없을 때
                                        //육아일기 지우기
                                        AskTask del_task = new AskTask(CommonVal.httpip, "deltitle.bif");
                                        del_task.addParam("title", title);
                                        InputStream del_in = CommonMethod.excuteGet(del_task);
                                        if(CommonVal.baby_list == null){ //사용자가 다른 육아일기가 없을 때
                                            //육아일기 생성으로 이동
                                            CommonVal.curuser.setId(CommonVal.curuser.getId());
                                            CommonVal.curuser.setPw(CommonVal.curuser.getPw());
                                            Toast.makeText(getContext(), "육아일기 생성으로 이동해야됨", Toast.LENGTH_SHORT).show();
                                            ((MainActivity)getActivity()).changeFrag(new DiaryFragment());
                                        } else{ //사용자가 다른 육아일기가 있을 때
                                            CommonVal.curbaby = CommonVal.baby_list.get(0);
                                            ((MainActivity)getActivity()).changeFrag(new MyFragment());
                                        }
                                    }
                                }
                            }
                        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog = builder_delete.create();
                alertDialog.show();
            }
        });

        return rootView;
    }

    //아기 추가 시 육아 일기 선택 다이얼로그
    public void InsertDialog(View view){
        new AlertDialog.Builder(getContext()).setTitle("육아 일기 선택").setSingleChoiceItems(titlelist, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), titlelist[which], Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonVal.curuser.setTitle(titlelist[which]);
                //아기 정보 입력으로 이동
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}
package com.example.test.join;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.test.MainActivity;
import com.example.test.R;
import com.google.gson.Gson;

import java.util.List;

public class JoinMainActivity extends AppCompatActivity {
    Button btn_next;
    ImageView btn_back;
    FrameLayout container;
    TextView tv_id_check;
    static int go = 0;
    static UserVO vo = new UserVO();
    static int id_chk = 0;
    static String id_chkchk = vo.getId();
    Gson gson = new Gson();
    List<UserVO> list;
    UserFragment userFragment = new UserFragment();
    NewFamilyFragment newFamilyFragment = new NewFamilyFragment();
    RelationFragment relationFragment = new RelationFragment();
    BirthFragment birthFragment = new BirthFragment();
    BabyFragment babyFragment = new BabyFragment();
    GenderFragment genderFragment = new GenderFragment();
    PictureFragment pictureFragment = new PictureFragment();



    String family_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_main);
        changeFrag( userFragment);

        //초대코드로 왔을 때
        Intent intent = getIntent();
        family_id = intent.getStringExtra("family_id");
        if(family_id != null){
            changeFrag( new UserFragment(family_id) );
        }

        btn_next = findViewById(R.id.btn_next);
        btn_back = findViewById(R.id.btn_back);
        container = findViewById(R.id.constraint);
        tv_id_check = findViewById(R.id.tv_id_check);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gogo();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

    }//onCreate()


    public void gogo(){//앞으로
        if( go==1 ){
            emptychk();
        }else if( go==2 ){
            changeFrag( relationFragment );
            go++;
        }else if( go==3 ){
            changeFrag( birthFragment );
            go++;
        }else if( go==4 ){
            changeFrag( babyFragment );
            go++;
        }else if( go==5 ){
            changeFrag( genderFragment );
            go++;
        }else if( go==6 ){
            changeFrag( pictureFragment );
            go++;
        }else if( go==7 ){
            Intent intent = new Intent( JoinMainActivity.this , MainActivity.class );
            startActivity(intent);
        }
    }

    public void back(){//뒤로
        if( go==1 ){
            altDialog();
        }else if( go==2 ){//
            changeFrag( userFragment );
            go--;
        }else if( go==3 ){
            changeFrag( newFamilyFragment );
            go--;
        }else if( go==4 ){
            changeFrag( relationFragment );
            go--;
        }else if( go==5 ){
            changeFrag( birthFragment );
            go--;
        }else if( go==6 ){
            changeFrag( babyFragment );
            go--;
        }else if( go==7 ){
            changeFrag( genderFragment );
            go--;
        }
    }



    @Override
    public void onBackPressed() {
        back();
    }
    public void backFrag(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }



    public void altDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(JoinMainActivity.this);
        builder.setTitle("회원가입을 종료 하시겠습니까?").setMessage("");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            { finish(); }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            { Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show(); }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void changeFrag(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }



    public void emptychk(){
        String aa = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(JoinMainActivity.this);
        if( userFragment.edt_id.getText().toString().equals( "" ) ){
            String aaa = "";
            builder.setTitle("아이디를 입력해주세요").setMessage("");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else if ( id_chk == 0 ){
            String aaa = "";
            builder.setTitle("아이디 중복확인을 해주세요").setMessage("");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else if ( userFragment.edt_pw.getText().toString().equals("") ){
            String aaa = "";
            builder.setTitle("비밀번호를 입력해주세요").setMessage("");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else if ( userFragment.edt_pwchk.getText().toString().equals("") ){
            String aaa = "";
            builder.setTitle("비밀번호를 입력해주세요").setMessage("");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else if( !vo.getPw_chk().equals( vo.getPw() )  ) {
            String aaa = "";
            builder.setTitle("비밀번호가 일치하지않습니다").setMessage("");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else {
            builder.setTitle("입력을 완료하시겠습니까?").setMessage("");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                  //  backFrag(newFamilyFragment);
                    changeFrag(newFamilyFragment);
                    go++;
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Log.d("testt", "onClick: " + vo.getId() + "");
        }

    }


}
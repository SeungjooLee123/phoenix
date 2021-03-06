package com.example.test.join;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.test.R;
import com.example.test.common.AskTask;
import com.example.test.common.CommonMethod;
import com.example.test.common.CommonVal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.oauth.view.NidOAuthLoginButton;
import com.navercorp.nid.profile.NidProfileCallback;
import com.navercorp.nid.profile.data.NidProfileResponse;
import com.nhn.android.naverlogin.OAuthLogin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class UserFragment extends Fragment {
    EditText edt_id, edt_pw, edt_pwchk;
    ImageView join_kakao, imv_naver;
    TextView tv_id_check, auto_id_check, pw_check, pwchk_check;
    String family_id;
    Gson gson = new Gson();
    NidOAuthLoginButton join_naver;
    public static OAuthLogin mOAuthLoginModule;

    public UserFragment() {

    }

    public UserFragment(String family_id) {
        this.family_id = family_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootVIew = (ViewGroup) inflater.inflate(R.layout.fragment_user, container, false);
        JoinMainActivity.go = 1;
        /*new Handler().postDelayed(new Runnable() {        ????????? ?????????
            @Override
            public void run() {
                codechk();
            }
        }, 500);*/


        KakaoSdk.init(getContext() ,"9bb5096013cc3ff738a2ca42f3fd61d1");
        NaverIdLoginSDK.INSTANCE.initialize( getContext() ,"uR4I8FNC11hwqTB3Fr6l","U3LRpxH6Tq","BSS");


        if (family_id != null) {
            JoinMainActivity.go = 7;
        }

        edt_id = rootVIew.findViewById(R.id.edt_id);
        edt_pw = rootVIew.findViewById(R.id.edt_pw);
        edt_pwchk = rootVIew.findViewById(R.id.edt_pwchk);
        //tv_id_check = rootVIew.findViewById(R.id.tv_id_check);
        auto_id_check = rootVIew.findViewById(R.id.id_check);
        pw_check = rootVIew.findViewById(R.id.pw_check);
        pwchk_check = rootVIew.findViewById(R.id.pwchk_check);

        join_kakao = rootVIew.findViewById(R.id.join_kakao);
        imv_naver = rootVIew.findViewById(R.id.imv_naver);
        join_naver = rootVIew.findViewById(R.id.join_naver);


        edt_id.requestFocus();


        Function2<OAuthToken, Throwable , Unit> callBack = new
                Function2<OAuthToken, Throwable, Unit>() {
                    @Override
                    public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                        // ?????? : Token??? ????????? ????????? ?????? ???????????? <=o,
                        // Token??? ????????? ?????? URL??? ??????????????? ????????? ?????? ??????.
                        if(throwable != null){
                            Toast.makeText(getContext(), "?????? ??????!?" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if(oAuthToken != null){
                            Toast.makeText(getContext(), "????????? ????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                            //???????????? ??????
                           /*UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                                @Override
                                public Unit invoke(Throwable error) {
                                    if (error != null) {
                                        Log.e("", "invoke: " , error);
                                        //Log.e(TAG, "?????? ?????? ??????", error);
                                    }
                                    else {
                                        Log.e("", "invoke: " , error);
                                        // Log.i(TAG, "?????? ?????? ??????. SDK?????? ?????? ?????? ???")
                                    }
                                    return null ;
                                }
                            });
                            UserApiClient.getInstance().unlink(new Function1<Throwable, Unit>() {
                                @Override
                                public Unit invoke(Throwable error) {
                                    if (error != null) {
                                        Log.e("", "invoke: " , error);
                                        //Log.e(TAG, "?????? ?????? ??????", error);
                                    }
                                    else {
                                        Log.e("", "invoke: " , error);
                                        // Log.i(TAG, "?????? ?????? ??????. SDK?????? ?????? ?????? ???")
                                    }
                                    return null;
                                }
                            });
*/
                            getKakaoInfo();
                        }
                        return null;
                    }
                };


        join_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "??????", Toast.LENGTH_SHORT).show();
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable( getContext() )){
                    //??????????????? ????????? ???????????? ???????????? ?????? ???????????? ????????? ????????? ???????????? ????????? ????????????.
                    //???????????? ????????? ???????????? ???????????? 2???????????? ????????? ?????????.
                    UserApiClient.getInstance().loginWithKakaoTalk( getContext() ,callBack );
                }else{
                    //??????????????? ????????? ????????? ????????????. Web??? ?????????(Redirect Uri) Activity?
                    UserApiClient.getInstance().loginWithKakaoAccount( getContext() ,callBack );
                }
            }
        });
        imv_naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join_naver.callOnClick();
            }
        });
            

        naverLogin();


        edt_id.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER){
                    String aa= "";
                    return true;
                }
                return false;
            }
        });
        id_nospace();
        pw_nospace();
        pwchk_nospace();
        edt_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                JoinMainActivity.vo.setId(edt_id.getText().toString());
                id_valid();
                id_nospace();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                JoinMainActivity.vo.setPw(edt_pw.getText().toString());
                pw_valid();
                pw_nospace();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_pwchk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                JoinMainActivity.vo.setPw_chk(edt_pwchk.getText().toString());
                pwchk_valid();
                pwchk_nospace();
                valid_result();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return rootVIew;
    }//onCreateView


    //????????????
    public boolean id_check() {
        AskTask task = new AskTask(CommonVal.httpip, "id_check.join");
        task.addParam("id", JoinMainActivity.vo.getId());
        String aa = "";
        InputStream in = CommonMethod.excuteGet(task);
        boolean data = gson.fromJson(new InputStreamReader(in), new TypeToken<Boolean>() {
        }.getType());
        return data;
    }

    //id????????? ^[a-z0-9]*$r1
    public boolean id_valid() {
        if (edt_id.getText().toString().equals("")) {
            auto_id_check.setText("???????????? ??????????????????.");
            auto_id_check.setTextColor(Color.parseColor("#FF7575"));
            return false;
        } else if(edt_id.getText().toString().length() < 5 || edt_id.getText().toString().length() > 10){
            auto_id_check.setText("ID??? ?????? ?????????/????????? 5~10?????? ????????????????????????.");
            auto_id_check.setTextColor(Color.parseColor("#FF7575"));
        }else if (Pattern.matches("^[0-9]{1,28}+[a-z0-9]{1,28}$", JoinMainActivity.vo.getId()) || Pattern.matches("^[a-z]{1,28}+[a-z0-9]{1,28}$", JoinMainActivity.vo.getId())) {
            //"(?:\\w{4,10}[a-z][0-9]+)$"
            auto_id_check.setText("??????????????? ID ???????????????.");
            auto_id_check.setTextColor(Color.parseColor("#3a539f"));
            //                                        ????????? #46FF3E
            if (id_check()) {
                auto_id_check.setText("?????? ????????? ????????? ?????????.");
                auto_id_check.setTextColor(Color.parseColor("#3a539f"));
                return true;
            } else {
                auto_id_check.setText("?????????????????? ????????? ?????????.");
                auto_id_check.setTextColor(Color.parseColor("#FF7575"));
            }
        } else {
            auto_id_check.setText("ID??? ?????? ?????????/????????? 5~10?????? ????????????????????????.");
            auto_id_check.setTextColor(Color.parseColor("#FF7575"));
            return false;
        }

        return false;
    }


    //pw?????????
    public boolean pw_valid() {
        if (edt_pw.getText().toString().equals("")) {
            pw_check.setText("??????????????? ??????????????????.");
            pw_check.setTextColor(Color.parseColor("#FF7575"));
            return false;
        } else if(edt_pw.getText().toString().length() < 5 || edt_pw.getText().toString().length() > 10){
            pw_check.setText("PW??? ?????? ?????????/????????? 5~10?????? ????????????????????????.");
            pw_check.setTextColor(Color.parseColor("#FF7575"));
        } else if (Pattern.matches("^[0-9]{1,28}+[a-z0-9]{1,28}$", JoinMainActivity.vo.getPw()) || Pattern.matches("^[a-z]{1,28}+[a-z0-9]{1,28}$", JoinMainActivity.vo.getPw())) {
            pw_check.setText("?????? ????????? ?????????????????????.");
            pw_check.setTextColor(Color.parseColor("#3a539f"));
            return true;
        }else {
            pw_check.setText("PW??? ?????? ?????????/????????? 5~10?????? ????????????????????????.");
            pw_check.setTextColor(Color.parseColor("#FF7575"));
        }
        return false;
    }
    public boolean pwchk_valid(){
        if( edt_pwchk.getText().toString().equals("")){
            pwchk_check.setText("??????????????? ???????????????.");
            pwchk_check.setTextColor(Color.parseColor("#FF7575"));
        }else if( edt_pwchk.getText().toString().equals(edt_pw.getText().toString())){
            pwchk_check.setText("??????????????? ???????????????.");
            pwchk_check.setTextColor(Color.parseColor("#3a539f"));
            return true;
        }else if( !edt_pw.getText().toString().equals(edt_pwchk.getText().toString())){
            pwchk_check.setText("??????????????? ????????????.");
            pwchk_check.setTextColor(Color.parseColor("#FF7575"));
            return false;
        }
        return false;
    }

    public boolean valid_result(){
        if(  pw_valid() && pwchk_valid() ){
            JoinMainActivity.result = 1;
            return true;
        }else {
            JoinMainActivity.result = 0;
        }
        return false;
    }


    public void id_nospace(){
        edt_id.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_SPACE || keyCode == event.KEYCODE_ENTER){
                    String aa= "";
                    Toast.makeText(getContext(), "????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
    public void pw_nospace(){
        edt_pw.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_SPACE || keyCode == event.KEYCODE_ENTER){
                    String aa= "";
                    Toast.makeText(getContext(), "????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
    public void pwchk_nospace(){
        edt_pwchk.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_SPACE || keyCode == event.KEYCODE_ENTER){
                    String aa= "";
                    Toast.makeText(getContext(), "????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }


    public void naverLogin(){
        NidOAuthLogin authLogin = new NidOAuthLogin();
        join_naver.setOAuthLoginCallback(new OAuthLoginCallback() {
            @Override
            public void onSuccess() {
                Log.d("naver" ,"onSuccess:??????");
                Log.d("naver", NaverIdLoginSDK.INSTANCE.getAccessToken());
                authLogin.callProfileApi(new NidProfileCallback<NidProfileResponse>() {
                    @Override
                    public void onSuccess(NidProfileResponse nidProfileResponse) {
                        Log.d("naver","onSuccess:??????" + nidProfileResponse.getProfile().getEmail());
                        JoinMainActivity.vo.setId( nidProfileResponse.getProfile().getEmail() );
                        JoinMainActivity.vo.setNaver_id( "Y" );
                        //edt_id.setEnabled(false); edit ??????
                        altdialog("?????? ???????????? ????????? ???????????????.",  "????????? : " +JoinMainActivity.vo.getId() );
                    }
                    @Override
                    public void onFailure(int i, @NonNull String s) {
                        Log.d("naver", "OnSuccess:??????" +s);
                        Log.d("naver", NaverIdLoginSDK.INSTANCE.getLastErrorCode().getCode());
                        Log.d("naver", NaverIdLoginSDK.INSTANCE.getLastErrorDescription());
                    }
                    @Override
                    public void onError(int i, @NonNull String s) {
                        Log.d("naver","OnSuccess:??????" + s);

                    }
                });
            }
            @Override
            public void onFailure(int i, @NonNull String s) {
                Log.d("naver", "OnSuccess:??????" +s);
            }
            @Override
            public void onError(int i, @NonNull String s) {
                Log.d("naver", "OnSuccess:??????" +s);
            }
        });

    }

    public void altdialog(String settitle , String setmessage){
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setTitle( settitle ).setMessage( setmessage );
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                JoinMainActivity act = (JoinMainActivity) getActivity();
                act.socialNaver();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void getKakaoInfo(){
        UserApiClient.getInstance().me( (user, throwable) -> {
            if(throwable != null){
                // ?????????. ?????? ???????????? ( Token??? ????????? Token??? ???????????????(Logout)
                // KOE + ??????
                Toast.makeText( getContext() , "?????? ?????? : " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                // [ { } ] json ???????????? ?????? ???????????? ????????? ??????.
                // Account Object?????? List??? ????????? List?????? Object??? ???????????????.
                Account kakaoAcount = user.getKakaoAccount();
                if(kakaoAcount != null){
                    String email = kakaoAcount.getEmail();
                    //String nickName = profile.getNickname();
                    AskTask task = new AskTask( CommonVal.httpip,"kakaoLoginn");
                    task.addParam("id" , email);
                    InputStream in =  CommonMethod.excuteGet(task);
                    Gson gson = new Gson();
                    String data = gson.fromJson(new InputStreamReader(in) , String.class);
                    String aa = "";
                    if(data !=null){
                        //????????? ??? ?????????.
                            //CommonVal.curuser = vo ;
                        //Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                        //startActivity(intent);
                        Toast.makeText(getContext(), "????????? ???????????????.", Toast.LENGTH_SHORT).show();
                        JoinMainActivity.vo.setId( data );
                        JoinMainActivity.vo.setKakao_id( "Y" );
                        altdialog("?????? ???????????? ????????? ???????????????.",  "ID : " +JoinMainActivity.vo.getId() );
                    }else{
                        Toast.makeText(getContext(), "????????? ???????????? ??????", Toast.LENGTH_SHORT).show();
                        //??????????????? ??????.
                        //Intent intent = new Intent(LoginActivity.this , JoinActivity.class);
                        //intent.putExtra("email" , email);
                        //startActivity(intent);
                    }


                    // AsynkTask????????????
                }

            }


            return null;
        });
    }
    






}
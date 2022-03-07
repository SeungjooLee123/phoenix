package com.example.test.sns;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test.MainActivity;
import com.example.test.MyFragment;
import com.example.test.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;


public class SnsFragment extends Fragment {


    ViewPager2 snspager;
    ArrayList<SnsDTO> snslist = new ArrayList<>();
    ImageView sns_plus, sns_more, testImg, sns_profile, sns_view;
    Intent intent;
    DotsIndicator dotsIndicator;
  //  String imgFilePath = null;
    public static ArrayList<String> img_list = new ArrayList<>();
    SnsViewPagerAdapter snsadapter ;

    //public static ArrayList<Uri> uriList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sns, container, false);

        snspager = rootView.findViewById(R.id.sns_view_pager);
        dotsIndicator = rootView.findViewById(R.id.sns_indicator);
        sns_plus = rootView.findViewById(R.id.sns_plus);
        sns_more = rootView.findViewById(R.id.sns_more);
        testImg = rootView.findViewById(R.id.testImg);
        sns_profile = rootView.findViewById(R.id.sns_profile);



       snsadapter = new SnsViewPagerAdapter(inflater,snslist , getContext());
       snspager.setAdapter(snsadapter);
       dotsIndicator.setViewPager2(snspager);

       sns_plus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                intent = new Intent(getContext(), SnsNewActivity.class);
                startActivity(intent);
           }
       });

        sns_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("수정이나 삭제할 게시물이 있으신가요?").setMessage("");
                builder.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), SnsNewActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("삭제하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        for(int i = 0 ; i<img_list.size() ; i++){
            snslist.add(new SnsDTO(img_list.get(i), "테스트1"));
        }
        //쿼리 작성할 부분
        img_list = new ArrayList<>();
        snsadapter.notifyDataSetChanged();
    }
}
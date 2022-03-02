package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    Fragment fragment;
    FrameLayout container;
    TabLayout tab_main;
    TabItem tab_diary, tab_map, tab_iot, tab_sns, tab_my;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        container = findViewById(R.id.container);
        tab_main = findViewById(R.id.tab_main);
        tab_diary = findViewById(R.id.tab_diary);
        tab_map = findViewById(R.id.tab_map);
        tab_iot = findViewById(R.id.tab_iot);
        tab_sns = findViewById(R.id.tab_sns);
        tab_my = findViewById(R.id.tab_my);

        changeFrag(new DiaryFragment());

        tab_main.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    fragment = new DiaryFragment();
                } else if(tab.getPosition()==1){
                    fragment = new MapFragment();
                } else if(tab.getPosition()==2){
                    fragment = new IotFragment();
                } else if(tab.getPosition()==3){
                    fragment = new SnsFragment();
                } else if(tab.getPosition()==4){
                    fragment = new MyFragment();
                }
                changeFrag(fragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void changeFrag(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

}
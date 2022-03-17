package com.example.test.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyAdapter extends FragmentStateAdapter {

    public int mCount;

    public MyAdapter(@NonNull FragmentActivity fragmentActivity, int count) {
        super(fragmentActivity);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index == 0) return new Home_MainFragment();
        else if(index ==1) return new Home_IotFragment();
        else if(index ==2) return new Home_ShareFragment();
        else if(index ==3) return  new Home_DiaryFragment();
        else return new Home_SnsFragment();
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public int getRealPosition(int position) {return position % mCount;}

}

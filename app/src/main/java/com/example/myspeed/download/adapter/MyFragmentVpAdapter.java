package com.example.myspeed.download.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyFragmentVpAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fragments;
    private FragmentManager fm;

    public MyFragmentVpAdapter(FragmentManager fm,Fragment[] fragments) {
        super(fm);


        this.fm=fm;
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}

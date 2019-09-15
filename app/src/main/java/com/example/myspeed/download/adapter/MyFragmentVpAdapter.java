package com.example.myspeed.download.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myspeed.download.fragment.DownlingFragment;
import com.example.myspeed.download.fragment.FinishedFragment;
import com.example.myspeed.download.fragment.PanFragment;

public class MyFragmentVpAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments={DownlingFragment.newInstance(),FinishedFragment.newInstance(),PanFragment.newInstance()};
    private FragmentManager fm;

    private String[] titles={"下载中","已完成","网盘"};


    public MyFragmentVpAdapter(FragmentManager fm) {
        super(fm);

        this.fm=fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}

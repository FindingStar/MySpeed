package com.example.myspeed.ui.message.view;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    private static BaseFragment fragment=null;

    public static BaseFragment newInstance(){
        if (fragment==null){
            fragment=new BaseFragment();
        }
        return fragment;
    }
}

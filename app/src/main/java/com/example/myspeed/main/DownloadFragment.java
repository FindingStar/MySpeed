package com.example.myspeed.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.myspeed.R;
import com.example.myspeed.base.MyFragmentManager;
import com.example.myspeed.base.MyFragmentTag;
import com.example.myspeed.download.adapter.MyFragmentVpAdapter;
import com.example.myspeed.download.fragment.DownlingFragment;
import com.example.myspeed.download.fragment.FabDialogFragment;
import com.example.myspeed.download.fragment.FinishedFragment;
import com.example.myspeed.download.fragment.PanFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadFragment extends Fragment {
    private ViewPager download_vp;
    private FloatingActionButton fab;
    private TabLayout tabLayout;

    private static final String TAG = "DownloadFragment";
    private static DownloadFragment downloadFragment;

    public View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fab:
                    // 打开对话框
                    FabDialogFragment dialogFragment=new FabDialogFragment(getContext());
                    dialogFragment.show(getFragmentManager(),"dialog");

            }
        }
    };

    private DownloadFragment(){

    }

    public static DownloadFragment newInstance(){
        if (downloadFragment==null){
            return new DownloadFragment();
        }
        return downloadFragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download_fragment, container, false);

        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(clickListener);

        download_vp=view.findViewById(R.id.download_vp);
        download_vp.setAdapter(new MyFragmentVpAdapter(getFragmentManager()));
        download_vp.setOffscreenPageLimit(3); // 默认是2= 自己+左右1

        tabLayout=view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(download_vp);

        return view;
    }

}


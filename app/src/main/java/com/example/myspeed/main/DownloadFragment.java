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
import com.example.myspeed.download.fragments.DownlingFragment;
import com.example.myspeed.download.fragments.FinishedFragment;
import com.example.myspeed.download.fragments.PanFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadFragment extends Fragment {
    private ViewPager download_vp;
    private FloatingActionButton fab;

    private static final String TAG = "DownloadFragment";

    private FragmentManager fm=getChildFragmentManager();
    private Fragment[] fragments={new DownlingFragment(),new FinishedFragment(),new PanFragment()};

    public View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fab:
                    // 打开对话框
            }
        }
    };

    public DownloadFragment(){
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

        download_vp.setAdapter(new MyFragmentVpAdapter(fm,fragments));

        editText = view.findViewById(R.id.ordinary_url_text);
        ordinary_bt = view.findViewById(R.id.ordinary_bt);
        ordinary_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url = editText.getText().toString();

                Bundle args=new Bundle();
                args.putString("flag","ordinary_download");
                MyFragmentManager myFm=MyFragmentManager.getInstance();
                ProgressFragment progressFrag= (ProgressFragment) myFm.getFragment(MyFragmentTag.PROGRESS);
                progressFrag.setArguments(args);
                myFm.splide(MyFragmentTag.PROGRESS);

                HttpURLConnection connection= null;
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                progressFrag.download(url,connection,null);

            }
        });

        pan_bt=view.findViewById(R.id.pan_bt);
        pan_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFragmentManager.getInstance().splide(MyFragmentTag.PAN);
            }
        });

        return view;
    }

}


package com.example.myspeed.download;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myspeed.MainActivity;
import com.example.myspeed.R;
import com.example.myspeed.pan.PanFragment;
import com.example.myspeed.progress.FileInfo;
import com.example.myspeed.progress.ProgressFragment;
import com.example.myspeed.progress.ProgressRvAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadFragment extends Fragment {
    private TextInputEditText editText;
    private Button ordinary_bt;
    private Button pan_bt;

    private PanFragment panFragment=new PanFragment();

    private static final String TAG = "DownloadFragment";

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

        editText = view.findViewById(R.id.ordinary_url_text);
        ordinary_bt = view.findViewById(R.id.ordinary_bt);
        ordinary_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editText.getText().toString();

                Bundle args=new Bundle();
                args.putString("flag","ordinary_download");
                ProgressFragment.getInstance().setArguments(args);

                HttpURLConnection connection= null;
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ProgressFragment.getInstance().download(url,connection,null);

                if (!ProgressFragment.getInstance().isAdded()){
                    MainActivity.myFm.push(ProgressFragment.getInstance(),getActivity().getSupportFragmentManager());
                }else {
                    MainActivity.myFm.top(ProgressFragment.getInstance(),getActivity().getSupportFragmentManager());
                }

            }
        });

        pan_bt=view.findViewById(R.id.pan_bt);
        pan_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!panFragment.isAdded()){
                    MainActivity.myFm.push(panFragment,getActivity().getSupportFragmentManager());
                }else{
                    MainActivity.myFm.top(panFragment,getActivity().getSupportFragmentManager());
                }
            }
        });

        return view;
    }



}

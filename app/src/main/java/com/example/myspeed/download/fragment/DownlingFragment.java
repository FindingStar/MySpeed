package com.example.myspeed.download.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myspeed.R;
import com.example.myspeed.download.adapter.DownlingRvAdapter;
import com.example.myspeed.download.entity.FileInfo;
import com.example.myspeed.download.entrance.DownloadEntrance;

public class DownlingFragment extends Fragment {

    private static final String TAG="DownlingFragment";

    private DownlingRvAdapter adapter;

    private static DownlingFragment downlingFragment;

    private DownlingFragment() {

    }

    public static DownlingFragment newInstance() {
        if (downlingFragment == null) {
            downlingFragment=new DownlingFragment();
            return downlingFragment;
        }
        return downlingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            String url = getArguments().getString("url");
            new DownloadEntrance().download(url, null, "ordinary_download");
        }catch (NullPointerException e){

        }

        Log.d(TAG, "onCreate: ");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.downling_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.downling_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DownlingRvAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Log.d(TAG, "onCreateView: ");
        return view;
    }

    public void updateUi(final FileInfo fileInfo) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(fileInfo);
                adapter.notifyDataSetChanged();
            }
        });

    }
}

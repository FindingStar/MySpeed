package com.example.myspeed.download.fragment;

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
import com.example.myspeed.download.adapter.FinishedRvAdapter;
import com.example.myspeed.download.entity.FileInfo;


public class FinishedFragment extends Fragment {

    private static FinishedFragment FinishedFragment;
    private FinishedRvAdapter adapter;

    private static final String TAG="FinishedFragment";

    private FinishedFragment() {

    }

    public static FinishedFragment newInstance() {
        if (FinishedFragment == null) {
            FinishedFragment=new FinishedFragment();
            return FinishedFragment;
        }
        return FinishedFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.finished_fragment,container,false);

        RecyclerView recyclerView=view.findViewById(R.id.finished_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter =new FinishedRvAdapter(getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return  view;
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

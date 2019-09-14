package com.example.myspeed.download.fragments;

import android.content.Context;
import android.os.Bundle;
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

public class DownlingFragment extends Fragment {

    private DownlingRvAdapter adapter;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.downling_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.downling_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new DownlingRvAdapter();

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }
}

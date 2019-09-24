package com.example.myspeed.main;

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

public class MarketFragment extends Fragment {

    private static MarketFragment marketFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.market_fragment,container,false);
        RecyclerView rv=view.findViewById(R.id.market_rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}

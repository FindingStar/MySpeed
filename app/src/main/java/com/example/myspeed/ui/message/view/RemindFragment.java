package com.example.myspeed.ui.message.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myspeed.R;
import com.example.myspeed.databinding.FragmentMessageBinding;
import com.example.myspeed.ui.message.view.adapter.RemindRvAdapter;

public class RemindFragment extends BaseFragment {

    private RemindFragment(){}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentMessageBinding binding= DataBindingUtil
                .inflate(inflater, R.layout.fragment_message,container,false);

        binding.messageRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.messageRv.setAdapter(new RemindRvAdapter(getContext()));

        return binding.getRoot();
    }
}

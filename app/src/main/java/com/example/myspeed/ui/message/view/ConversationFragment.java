package com.example.myspeed.ui.message.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myspeed.R;
import com.example.myspeed.databinding.FragmentMessageBinding;
import com.example.myspeed.ui.message.model.Conversation;
import com.example.myspeed.ui.message.view.adapter.ConversationRvAdapter;
import com.example.myspeed.ui.message.vm.BaseViewModel;
import com.example.myspeed.ui.message.vm.ConversationViewModel;

import java.util.List;

public class ConversationFragment extends BaseFragment {

    private ConversationFragment(){}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentMessageBinding binding= DataBindingUtil
                .inflate(inflater,R.layout.fragment_message,container,false);

        binding.messageRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.messageRv.setAdapter(new ConversationRvAdapter(getContext()));

        return binding.getRoot();
    }

}

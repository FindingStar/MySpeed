package com.example.myspeed.ui.message.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myspeed.R;
import com.example.myspeed.databinding.ItemConverRvBinding;
import com.example.myspeed.ui.message.model.Conversation;
import com.example.myspeed.ui.message.model.ModelGenator;
import com.example.myspeed.ui.message.vm.ConversationViewModel;

import java.util.ArrayList;
import java.util.List;

public class ConversationRvAdapter extends RecyclerView.Adapter<ConversationRvAdapter.ConversationRvHolder> {


    private List<Conversation> lists;
    private Context context;

    public ConversationRvAdapter(Context context){
        this.context=context;
        this.lists= (List<Conversation>) new ModelGenator<>(Conversation.class).fill(new ArrayList<Conversation>(),10);
    }


    @NonNull
    @Override
    public ConversationRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        ItemConverRvBinding binding= DataBindingUtil.inflate(inflater,R.layout.item_conver_rv,parent,false);

        return new ConversationRvHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationRvHolder holder, int position) {
        Conversation conversation=lists.get(position);
        holder.bind(conversation);
    }

    @Override
    public int getItemCount() {
        return lists==null ? 0: lists.size();
    }

    class ConversationRvHolder extends RecyclerView.ViewHolder{

        ItemConverRvBinding binding;

        public ConversationRvHolder(@NonNull ItemConverRvBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.setViewmodel(new ConversationViewModel());
        }

        public void bind(Conversation conversation){
            binding.getViewmodel().setConversation(conversation);
            binding.executePendingBindings();
        }
    }
}

package com.example.myspeed.ui.message.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myspeed.R;
import com.example.myspeed.databinding.ItemRemindRvBinding;
import com.example.myspeed.ui.message.model.RemindModel;

import java.util.ArrayList;
import java.util.List;

public class RemindRvAdapter extends RecyclerView.Adapter<RemindRvAdapter.RemindHolder> {

    private Context context;
    private List<RemindModel> reminds;

    public RemindRvAdapter(Context context) {
        this.context = context;
        this.reminds=new ArrayList<>();
    }

    @NonNull
    @Override
    public RemindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemRemindRvBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_remind_rv, parent, false);
        return new RemindHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindHolder holder, int position) {
        holder.bind(reminds.get(position));
    }


    @Override
    public int getItemCount() {
        return reminds==null ? 0: reminds.size();
    }

    class RemindHolder extends RecyclerView.ViewHolder {

        ItemRemindRvBinding binding;

        public RemindHolder(@NonNull ItemRemindRvBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public void bind(RemindModel remindModel){
            binding.getViewmodel().setRemindModel(remindModel);
            binding.executePendingBindings();
        }
    }

}

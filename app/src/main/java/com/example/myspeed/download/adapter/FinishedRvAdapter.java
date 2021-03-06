package com.example.myspeed.download.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myspeed.R;
import com.example.myspeed.download.entity.FileInfo;
import com.example.myspeed.download.util.IntentFiles;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FinishedRvAdapter extends RecyclerView.Adapter<FinishedRvAdapter.FinishedHoler> {

    private List<FileInfo> fileInfos = new ArrayList<>();
    public static final String TAG = "FinishedRvAdapter";
    private Context context;

    public FinishedRvAdapter(Context context) {
        this.context = context;
    }

    public void add(FileInfo fileInfo) {
        fileInfos.add(fileInfo);
    }

    @NonNull
    @Override
    public FinishedHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_finished_rv, parent, false);
        Log.d(TAG, "onCreateViewHolder:   ");
        return new FinishedHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedHoler holder, int position) {
        FileInfo fileInfo = fileInfos.get(position);
        holder.setName(fileInfo.getFileName());
        holder.setSize(fileInfo.getLength());
        holder.setTime(fileInfo.getEndTime() - fileInfo.getStartTime());
        holder.setSpeed(fileInfo.getLength(), fileInfo.getEndTime() - fileInfo.getStartTime());

    }

    @Override
    public int getItemCount() {
        return fileInfos.size();
    }

    class FinishedHoler extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cardView;
        private ImageView thumbIv;
        private TextView nameTv, speedTv, timeTv, sizeTv;


        public FinishedHoler(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            cardView = itemView.findViewById(R.id.cv_item_finished);
            nameTv = itemView.findViewById(R.id.name_cv_tv);
            speedTv = itemView.findViewById(R.id.avgspeed_cv_tv);
            timeTv = itemView.findViewById(R.id.times_cv_tv);
            sizeTv = itemView.findViewById(R.id.size_cv_tv);
        }

        public void setName(String name) {
            nameTv.setText(name);
        }

        public void setSize(long size) {
            sizeTv.setText(size / 1024 + " kb");
        }

        public void setTime(long time) {
            timeTv.setText((time/1000) + " s ");
        }

        public void setSpeed(long size, long time) {
            double p = (double)size * 1000;
            double t= (double)time* 1024 * 1024;
            double speed = p / t;
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            speed = Double.valueOf(decimalFormat.format(speed));
            speedTv.setText(speed + " m/s");
        }

        @Override
        public void onClick(View v) {
            // open file
            FileInfo fileInfo=fileInfos.get(this.getAdapterPosition());
            File file=new File("/mnt/sdcard/"+fileInfo.getFileName());
            Intent intent= IntentFiles.getVideoFileIntent(file);
            context.startActivity(intent);

        }
    }
}

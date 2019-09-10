package com.example.myspeed.progress;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myspeed.MainActivity;
import com.example.myspeed.R;
import com.example.myspeed.download.Status;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProgressRvAdapter extends RecyclerView.Adapter<ProgressRvAdapter.RecyclerViewHolder> {

    public static final String TAG = "ProgressRvAdapter";


    private Context context;
    private List<FileInfo> files = new ArrayList<>();

    private static final int UPDATE_SEEK = 1;


    public void add(FileInfo fileInfo) {
        files.add(fileInfo);
    }

    public ProgressRvAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_progress_rv, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {

        final FileInfo fileInfo = files.get(position);

        holder.setFileName(fileInfo.getFileName());
        long waitStart=System.currentTimeMillis();
        while (fileInfo.getLength() == 0) {
            if ((System.currentTimeMillis()-waitStart)>3000){
                Log.e(TAG, "onBindViewHolder:   init  slowly" );
            }
        }

        holder.setLength(fileInfo.getLength());
        holder.setProgress(fileInfo.getPosition() / fileInfo.getLength());


        new Thread() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {

                    if (fileInfo.getLength() != 0) {
                        Message message = new Message();
                        message.what = UPDATE_SEEK;
                        message.arg1 = (int) fileInfo.getPosition();
                        message.arg2 = (int) System.currentTimeMillis();
                        if (fileInfo.getPosition()!= fileInfo.getLength()) {
                            holder.getHandler().sendMessage(message);

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            holder.getHandler().sendMessage(message);
                            this.interrupt();
                        }
                    }

                }

                // toast 用时
                Log.d(TAG, "run:   用时 "+(System.currentTimeMillis()-fileInfo.getStartTime()));

            }
        }.start();
    }

    @Override
    public int getItemCount() {

        return files.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView fileNameText;
        private SeekBar seekBar;
        private TextView speedText;
        private TextView lengthText;
        private Button stopBt;

        private long length;

        private View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.stopBt:
                        FileInfo fileInfo=files.get(getAdapterPosition());
                        if (fileInfo.getThreadManager().status==Status.DOWNLOADING){
                            stopBt.setText(R.string.title_continue);
                            fileInfo.getThreadManager().pause();
                        }else if (fileInfo.getThreadManager().status==Status.STOPING){
                            stopBt.setText(R.string.title_stop);
                            fileInfo.getThreadManager().goon();
                        }
                        return;
                }
            }
        };

        private Handler handler = new Handler() {

            private int oldPosition;
            private long oldTime;

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_SEEK:
                        if (length != 0) {
                            seekBar.setProgress((msg.arg1 * 100 / (int) length));
                        }
                        double speed;
                        if (oldTime == 0) {
                            speed = 0;
                        } else {
                            //  细节
                            long p=(msg.arg1 - oldPosition)*1000;
                            long t=(msg.arg2 - oldTime)*1024*1024;
                            speed=(double)p/t;
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("#.00");
                        speed=Double.valueOf(decimalFormat.format(speed));
                        speedText.setText("S : " + speed + " m/s");

                        oldPosition = msg.arg1;
                        oldTime = msg.arg2;

                }
            }
        };

        public Handler getHandler() {
            return handler;
        }

        public RecyclerViewHolder(@NonNull View itemView) {

            super(itemView);

            fileNameText = itemView.findViewById(R.id.fileNameText);
            seekBar = itemView.findViewById(R.id.seekBar);
            speedText = itemView.findViewById(R.id.speedText);
            lengthText = itemView.findViewById(R.id.lengthText);
            stopBt=itemView.findViewById(R.id.stopBt);
            stopBt.setOnClickListener(clickListener);

        }

        private void setProgress(long progerss) {
            seekBar.setProgress((int) progerss);
        }

        private void setFileName(String fileName) {
            fileNameText.setText(fileName);
        }

        private void setLength(long length) {
            this.length = length;
            if (length/1024>1024){
                lengthText.setText("L : " + (length/1024)/1024+" m");
            }else {
                lengthText.setText("L : " + (length/1024)+" kb");
            }

        }

    }
}

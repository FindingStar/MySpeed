package com.example.myspeed.download.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myspeed.R;
import com.example.myspeed.download.constant.Status;
import com.example.myspeed.download.entity.FileInfo;
import com.example.myspeed.download.fragment.FinishedFragment;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DownlingRvAdapter extends RecyclerView.Adapter<DownlingRvAdapter.DownlingHolder> {

    public static final String TAG = "DownlingRvAdapter";


    private Context context;
    private List<FileInfo> files = new ArrayList<>();

    private static final int UPDATE_SEEK = 1;

    public void add(FileInfo fileInfo) {
        files.add(fileInfo);
        notifyItemInserted(files.size()-1);
    }
    public void remove(FileInfo fileInfo) {
        files.remove(fileInfo);
    }

    public DownlingRvAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public DownlingRvAdapter.DownlingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_downling_rv, parent, false);
        return new DownlingRvAdapter.DownlingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DownlingHolder holder, int position) {
        final FileInfo fileInfo = files.get(position);

        holder.setFileName(fileInfo.getFileName());
        long waitStart=System.currentTimeMillis();
        while (fileInfo.getLength() == 0) {
            if ((System.currentTimeMillis()-waitStart)>3000){
            }
        }

        holder.setLength(fileInfo.getLength());
        holder.setProgress(fileInfo.getPosition() / fileInfo.getLength());

        Log.d(TAG, "onBindViewHolder: " +files+"    "+fileInfo+"   "+position);

        new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "run:  times ");
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
                fileInfo.setEndTime(System.currentTimeMillis());
                FinishedFragment.newInstance().updateUi(fileInfo);

            }
        }.start();
    }

    @Override
    public int getItemCount() {

        return files.size();
    }

    public class DownlingHolder extends RecyclerView.ViewHolder {
        private TextView fileNameText;
        private ProgressBar progressBar;
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
                        if (fileInfo.getThreadManager().status== Status.DOWNLOADING){
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
                            long p1=(length-msg.arg1)*100;
                            double p2=p1*1.0/length;
                            double pro=100*1.0-p2;
                            progressBar.setProgress((int) pro);
                        }
                        double speed;
                        if (oldTime == 0) {
                            speed = 0;
                        } else {
                            //  细节
                            double p=((msg.arg1 - oldPosition)*1.0/1024)/1024;

                            double t=(msg.arg2 - oldTime)*1.0/1000;
                            speed=p/t;

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

        public DownlingHolder(@NonNull View itemView) {

            super(itemView);

            fileNameText = itemView.findViewById(R.id.fileNameText);
            progressBar = itemView.findViewById(R.id.progressBar);
            speedText = itemView.findViewById(R.id.speedText);
            lengthText = itemView.findViewById(R.id.lengthText);
            stopBt=itemView.findViewById(R.id.stopBt);
            stopBt.setOnClickListener(clickListener);

        }

        private void setProgress(long progerss) {
            progressBar.setProgress((int) progerss);
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

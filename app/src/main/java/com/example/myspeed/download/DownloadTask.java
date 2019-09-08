package com.example.myspeed.download;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;


public class DownloadTask implements Runnable, Serializable {

    public static final String TAG = "DownloadTask";
    private static final int BSIZE=1024;

    private long progress;

    private String url;
    private Map<String,String> params=new HashMap<>();

    public int id;
    private long start, end;
    private String filePath;

    private Status status = Status.STOPING;


    public DownloadTask(int id, long start, long end, String filePath) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.filePath = filePath;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public void run() {
        try {
            HttpURLConnection connection= (HttpURLConnection) new URL(url).openConnection();

            if (params!=null){
                for (String k:params.keySet()) {
                    connection.setRequestProperty(k,params.get(k));
                }
            }
            connection.setRequestProperty("Range", "bytes=" + start + "-" + end);

            // 这儿感觉 nio 不行， 速度没变， 使用BufferedRandomAccessFile oio
            BufferedRandomAccessFile out=new BufferedRandomAccessFile(filePath, "rw");
            out.overSeek(start);

            synchronized (this) {
                status = Status.DOWNLOADING;
            }

            BufferedInputStream in=new BufferedInputStream(connection.getInputStream());
            byte[] bytes=new byte[1024];
            int i;
            while ((i=in.read(bytes))!=-1) {
                out.write(bytes);
                synchronized (this) {
                    progress=progress+i;
                }
            }
            synchronized (this) {
                status = Status.FINISHED;
            }

            Log.d(TAG, "run: "+id+"  have finised ");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Status getStatus() {
        return status;
    }

    public synchronized long getProgress() {

        return progress;

    }


    @Override
    public String toString() {
        return "Id: " + id + "  Progress : " + progress + " Status :" + status + "  State : " + Thread.currentThread().getState() + "  Name :  " + Thread.currentThread().getName();
    }
}

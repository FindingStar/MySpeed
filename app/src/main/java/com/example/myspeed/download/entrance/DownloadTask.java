package com.example.myspeed.download.entrance;

import android.util.Log;

import com.example.myspeed.download.util.ThreadManager;
import com.example.myspeed.download.constant.Status;
import com.example.myspeed.download.util.BufferedRandomAccessFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class DownloadTask implements Runnable, Serializable {

    public static final String TAG = "DownloadTask";

    private long progress;

    private String url;
    private Map<String,String> params=new HashMap<>();

    public int id;
    private long start, end;
    private String filePath;

    private ThreadManager threadManager;


    public DownloadTask(int id, long start, long end, String filePath,ThreadManager threadManager) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.filePath = filePath;
        this.threadManager=threadManager;
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

            BufferedInputStream in=new BufferedInputStream(connection.getInputStream());
            byte[] bytes=new byte[1024];
            int i;
            while ((i=in.read(bytes))!=-1) {
                synchronized (this) {
                    while(threadManager.status== Status.STOPING){
                        wait();
                    }
                    out.write(bytes);
                    progress=progress+i;
                }
            }


            Log.d(TAG, "run: "+id+"  have finised ");
        } catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    public synchronized long getProgress() {

        return progress;

    }


    @Override
    public String toString() {
        return "Id: " + id + "  Progress : " + progress + "  State : " + Thread.currentThread().getState() + "  Name :  " + Thread.currentThread().getName();
    }
}

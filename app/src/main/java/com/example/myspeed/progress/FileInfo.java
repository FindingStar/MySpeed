package com.example.myspeed.progress;

import com.example.myspeed.download.DownloadTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileInfo implements Serializable {
    public static final String TAG="FileInfo";
    private String fileName;
    private String url;
    private long length;
    private long startTime;


    private List<DownloadTask> tasks= Collections.synchronizedList(new ArrayList<DownloadTask>());



    private String speed;

    private boolean runing;

    public FileInfo(String fileName){
        this.fileName=fileName;

    }
    public FileInfo(){

    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getLength() {
        return length;
    }

    public String getFileName() {
        return fileName;
    }

    public long getPosition() {

        long position = 0;
        for (DownloadTask task:tasks) {
            position=position+task.getProgress();
        }
        return  position;

    }

    public void setTasks(List<DownloadTask> threads) {
        this.tasks=threads;
    }


    @Override
    public String toString() {
        return "File-Name:"+fileName+"  ;   "+"Length:"+length;
    }
}

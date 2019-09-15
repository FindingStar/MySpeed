package com.example.myspeed.download.util;

import com.example.myspeed.download.constant.Status;
import com.example.myspeed.download.entrance.DownloadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  每一个下载 持有 一个ThreadManager
 *   把线程放在这儿管理
 */
public class ThreadManager {
    public ExecutorService exec= Executors.newCachedThreadPool();
    public List<DownloadTask> tasks= Collections.synchronizedList(new ArrayList<DownloadTask>());
    public Status status=Status.DOWNLOADING;

    public ThreadManager(){

    }

    public  void pause(){

        status=Status.STOPING;
    }

    public void goon(){
        status=Status.DOWNLOADING;
        for (DownloadTask task:tasks) {
            synchronized (task){
                task.notifyAll();
            }
        }
    }


}

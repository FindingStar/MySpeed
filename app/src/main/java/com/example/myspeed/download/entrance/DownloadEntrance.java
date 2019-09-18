package com.example.myspeed.download.entrance;

import android.util.Log;

import com.example.myspeed.download.constant.Constrants;
import com.example.myspeed.download.fragment.DownlingFragment;
import com.example.myspeed.download.util.ThreadManager;
import com.example.myspeed.download.entity.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

public class DownloadEntrance {

    private static final String TAG="downloadEntrance";

    // 通用 download  入口
    public void download(String url, final Map<String,String> params, String flag) {

        final FileInfo fileInfo = new FileInfo();
        String name=null;
        if (flag != null) {
            if (flag.equals("ordinary_download")) {
                String[] urls = url.split("/");
                name=urls[urls.length - 1];
            }
            if (flag.equals("pan_download")) {
                int start = url.indexOf("path=");
                int end = url.indexOf("&random");
                String fileName = url.substring(start, end);
                String[] ts = fileName.split("%2F");
                try{
                    name= URLDecoder.decode( ts[ts.length - 1],"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    name = ts[ts.length - 1];
                }

            }
        }
        fileInfo.setFileName(name);
        fileInfo.setUrl(url);

        new Thread(){
            @Override
            public void run() {
                startDownload(fileInfo,params);
            }
        }.start();

        DownlingFragment.newInstance().updateUi(fileInfo,0);

    }

    private void startDownload(FileInfo fileInfo,Map<String,String> params) {
        long length = 0;

        String url=fileInfo.getUrl();

        HttpURLConnection connection= null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            if (params!=null){
                for (String k:params.keySet()) {
                    connection.setRequestProperty(k,params.get(k));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        length = connection.getContentLengthLong();
        fileInfo.setLength(length);

        fileInfo.setStartTime(System.currentTimeMillis());


        long num = length / Constrants.ThreadCount;
        String filePath = "/mnt/sdcard/" + fileInfo.getFileName();

        try {

            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Log.e(TAG, "startDownload:  createFile failed");
                }
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
            randomAccessFile.setLength(length);
        } catch (IOException e) {
            Log.e(TAG, "startDownload: ----------- 创建读取 文件路径 出错");
        }

        ThreadManager threadManager=fileInfo.getThreadManager();

        for (int i = 0; i < Constrants.ThreadCount; i++) {
            DownloadTask downloadTask = new DownloadTask(i, i * num, (i + 1) * num - 1, filePath,threadManager);
            downloadTask.setUrl(fileInfo.getUrl());
            downloadTask.setParams(params);
            threadManager.exec.execute(downloadTask);
            threadManager.tasks.add(downloadTask);

        }
        if (!(Constrants.ThreadCount * num == length)) {
            DownloadTask downloadTask = new DownloadTask(Constrants.ThreadCount, Constrants.ThreadCount * num, length - 1, filePath,threadManager);
            downloadTask.setUrl(fileInfo.getUrl());
            downloadTask.setParams(params);
            threadManager.exec.execute(downloadTask);
            threadManager.tasks.add(downloadTask);
        }

    }

}

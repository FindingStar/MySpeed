package com.example.myspeed.progress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myspeed.R;
import com.example.myspeed.base.MyFragmentManager;
import com.example.myspeed.base.MyFragmentTag;
import com.example.myspeed.download.Constrants;
import com.example.myspeed.download.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProgressFragment extends Fragment {

    private static final String TAG = "ProgressFragment";
    private ProgressRvAdapter adapter;

    private static ProgressFragment progressFragment = new ProgressFragment();

    public ProgressFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.progress_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProgressRvAdapter(getContext());

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

    // 通用 download  入口
    public void download(String url, final HttpURLConnection connection, final Map<String,String> params) {
        if (connection == null) {
            Log.e(TAG, "download: ------ connection is null");
            throw new RuntimeException("connection is null ");
        }

        String flag = null;
        try {
            flag = getArguments().getString("flag");
        } catch (NullPointerException e) {
            Log.d(TAG, "onCreateView: --------无此flag");
        }

        final FileInfo fileInfo = new FileInfo();
        if (flag != null) {
            if (flag.equals("ordinary_download")) {
                String[] urls = url.split("/");
                fileInfo.setFileName(urls[urls.length - 1]);
            }
            if (flag.equals("pan_download")) {
                int start = url.indexOf("path=");
                int end = url.indexOf("&random");
                String fileName = url.substring(start, end);
                String[] ts = fileName.split("%2F");
                fileName = ts[ts.length - 1];
                fileInfo.setFileName(fileName);
            }
        }
        fileInfo.setUrl(url);



        while (adapter == null) {

        }
        try {

            adapter.add(fileInfo);
            adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(TAG, "download: ---------- fileInfo  add  failed");
        } finally {
            new Thread() {
                @Override
                public void run() {

                    startDownload(fileInfo, connection,params);
                }
            }.start();
        }


    }

    private void startDownload(FileInfo fileInfo, HttpURLConnection connection,Map<String,String> params) {
        long length = 0;

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

        ExecutorService exec = Executors.newCachedThreadPool();

        List<DownloadTask> tasks = Collections.synchronizedList(new ArrayList<DownloadTask>());

        for (int i = 0; i < Constrants.ThreadCount; i++) {
            DownloadTask downloadTask = new DownloadTask(i, i * num, (i + 1) * num - 1, filePath);
            downloadTask.setUrl(fileInfo.getUrl());
            downloadTask.setParams(params);
            exec.execute(downloadTask);
            tasks.add(downloadTask);

        }
        if (!(Constrants.ThreadCount * num == length)) {
            DownloadTask downloadTask = new DownloadTask(Constrants.ThreadCount, Constrants.ThreadCount * num, length - 1, filePath);
            downloadTask.setUrl(fileInfo.getUrl());
            downloadTask.setParams(params);
            exec.execute(downloadTask);
            tasks.add(downloadTask);
        }
        fileInfo.setTasks(tasks);

    }

}

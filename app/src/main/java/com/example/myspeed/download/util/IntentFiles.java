package com.example.myspeed.download.util;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class IntentFiles {

    //android获取一个用于打开视频文件的intent
    public static Intent getVideoFileIntent(File file)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //activity 启动模式

        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }
}

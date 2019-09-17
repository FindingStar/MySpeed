package com.example.myspeed;


import com.example.myspeed.download.util.FileUtil;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class FileUtilTest {

    @Test
    public void searchTest(){


        Assert.assertEquals("mp3", FileUtil.search("/mnt/sdcard/","live.mp3"));
    }
}

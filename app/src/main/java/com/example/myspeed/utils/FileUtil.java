package com.example.myspeed.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *   通过内部类 进行 功能分块，多继承
 */
public class FileUtil {

    public static boolean search(){

        return true;
    }

    class MyFileFilter implements FilenameFilter{
        @Override
        public boolean accept(File dir, String name) {
            return false;
        }
    }

}

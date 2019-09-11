package com.example.myspeed.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *   通过内部类 进行 功能分块，多继承
 */
public class FileUtil {

    public static boolean search(String dir,String dstName){
        File file=new File(dir);
        String[] list=file.list(new MyFileFilter("名字后缀"));
        return true;
    }

    static class MyFileFilter implements FilenameFilter{
        private Pattern pattern;
        private MyFileFilter(String regex){
            pattern=Pattern.compile(regex);
        }
        @Override
        public boolean accept(File dir, String name) {
            return pattern.matcher(name).matches();
        }
    }

}

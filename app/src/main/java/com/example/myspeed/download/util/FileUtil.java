package com.example.myspeed.download.util;

import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *   通过内部类 进行 功能分块，多继承
 */
public class FileUtil {


    public static String search(String dir,String dstName){
        File file=new File(dir);
        String[] list=file.list();
        int e=dstName.lastIndexOf(".");
        String es=dstName.substring(e+1,dstName.length());
        list =file.list(new MyFileFilter(es));
        return es;
    }

}
class MyFileFilter implements FilenameFilter{
    private Pattern pattern;
    MyFileFilter(String regex){
        pattern=Pattern.compile(regex);
    }
    @Override
    public boolean accept(File dir, String name) {
        return pattern.matcher(name).matches();
    }
}

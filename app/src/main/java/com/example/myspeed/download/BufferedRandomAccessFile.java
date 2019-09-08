package com.example.myspeed.download;

import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BufferedRandomAccessFile extends RandomAccessFile {

    // read 進去的， 要write出去的 （雙功能）
    private byte[] buff;

    private static final int DEFAULT_BUFFERSIZE = 1024;

    private long curpos, buffstartpos, buffendpos, filepos;
    private int bufferusedsize;

    private long bigpos;
    private long rpos;

    private boolean dirty;


    public BufferedRandomAccessFile(String file, String mode) throws FileNotFoundException {
        super(file, mode);
        // TODO Auto-generated constructor stub

        buff = new byte[DEFAULT_BUFFERSIZE];

    }

    @Override
    public int read() throws IOException {
        if (curpos > buffendpos) {
            seek(curpos);
        }
        byte b=buff[(int) (curpos - buffstartpos)];
        curpos++;
        return  b& 0xff;
    }

    public void overSeek(long pos) throws IOException{
        bigpos=pos;
        super.seek(pos);

    }

    @Override
    public void seek(long pos) throws IOException {
        // 是否 设置buffer 位置，当 pos 在在buffer 区段，就不用了

        if (pos >= buffendpos || pos < buffstartpos) {
            flushBuff();

            // 无 偏移量
            buffstartpos=pos;
            bufferusedsize = fillBuff();  // 只是 负责从 流中取一点
            buffendpos = buffstartpos + bufferusedsize-1;

        }
        curpos = pos;
    }

    private int fillBuff() throws IOException {
        // 准备从文件 读取， 所以 seek +read
        int s=0;
        int l=buff.length;
        while (l>0) {
            int n=super.read(buff,s,l);
            if (n<0) {
                break;  //EOF
            }
            s+=n;
            l-=n;
        }
        return s;

    }

    private void flushBuff() throws IOException {
        if (dirty) {
            int len=(int) (buffendpos-buffstartpos+1);
            if (curpos!=filepos) {
                super.seek(filepos+bigpos);
            }
            super.write(buff,0,len);
            filepos=curpos;
            dirty=false;
        }

    }

    /**
     *  param :
     *  	buffstartpos,buffendpos : buffer映射在文件中的位置，buffendpos 是有效位置
     *  	curpos： 。 （  当更新到buffer 中时  更新curpos）
     *  	filepos： 目标文件 应该的真实指针，（在 read write 时更新）
     *
     */
    @Override
    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        while (len>0) {
            int n=writeMost(b, off, len);
            off+=n;
            len-=n;
            dirty=true;
        }

    }
    private int writeMost(byte[] b, int off, int len) throws IOException {
        if (curpos>=buffendpos) {
            seek(curpos);
        }
        len=Math.min(len, buff.length);
        System.arraycopy(b, off, buff, (int) (curpos-buffstartpos), len);
        buffendpos=buffstartpos+len-1;
        curpos+=len;
        return len;
    }

    @Override
    public void close() throws IOException {
        this.flushBuff();
        this.buff=null;
        super.close();
    }
}

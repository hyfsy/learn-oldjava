package io;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 
 * 管道流 用于线程之间进行数据通信
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月8日]
 */
public class PipedStream
{

    public static void main(String[] args) {
        // 默认是1024
        PipedInputStream pis = new PipedInputStream(32);
        PipedOutputStream pos = new PipedOutputStream();
        try {
            // 将两个管道连接
            pis.connect(pos);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Thread readThread = new Thread(new ReadThread(pis));
        Thread writeThread = new Thread(new WriteThread(pos));
        // 先将管道输入流打开保持监听
        readThread.start();
        writeThread.start();
    }

}

/**
 * 
 * [读取管道流线程]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月8日]
 */
class ReadThread implements Runnable
{
    private PipedInputStream pis;

    // 构造管道输入流
    public ReadThread(PipedInputStream pis) {
        this.pis = pis;
    }

    @Override
    public void run() {
        int len = -1;
        byte[] bytes = new byte[1024];
        try {
            while ((len = pis.read(bytes)) != -1) {
                String str = new String(bytes, 0, len);
                System.out.println("读取到：" + str);
            }
            // 关闭管道输入流
            pis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

/**
 * 
 * [写入管道流线程]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月8日]
 */
class WriteThread implements Runnable
{

    private PipedOutputStream pos;

    // 构造管道输出流
    public WriteThread(PipedOutputStream pos) {
        this.pos = pos;
    }

    @Override
    public void run() {
        int count = 20;
        try {
            // 连续放入20条数据进入管道流
            while (count > 0) {
                pos.write("传入数据".getBytes());
                count--;
            }

            // 关闭管道输出流
            pos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

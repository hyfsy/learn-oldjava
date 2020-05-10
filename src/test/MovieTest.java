package test;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author baB_hyf
 * @date 2020/03/02
 */
public class MovieTest {

    @Test
    public void testFile() throws Exception {
        String movie = "E:\\Youku Files\\test\\001.mp4";
        String dir = "E:\\Youku Files\\test\\temp\\";
//        String movie = "E:\\Youku Files\\download\\01.mp4";
//        String dir = "E:\\Youku Files\\download\\temp\\";

        long start = System.currentTimeMillis();
//        splitFile(movie, dir, 30);
        System.out.println("总耗时：" + (System.currentTimeMillis() - start));
    }


    /**
     * 复制的成功II
     *
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {

        // 2.5 -> 102461-120526
//        String movie = "E:\\Youku Files\\download\\001.kux";
//        String dir = "E:\\Youku Files\\download\\temp\\01-3.kux";

        String movie = "E:\\Youku Files\\test\\01.mp4";
        String dir = "E:\\Youku Files\\test\\001.mp4";
        long start = System.currentTimeMillis();

        FileChannel inChannel = FileChannel.open(Paths.get(movie), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get(dir), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        long size = inChannel.size();
        int max = Integer.MAX_VALUE >> 1;
        byte[] bytes = new byte[max];
        long pos = 0;

        while (size > max) {
            MappedByteBuffer inMappedBuf = inChannel.map(MapMode.READ_ONLY, pos, max);
            MappedByteBuffer outMappedBuf = outChannel.map(MapMode.READ_WRITE, pos, max);
            long mid_2 = System.currentTimeMillis();
            inMappedBuf.get(bytes);
            long mid_3 = System.currentTimeMillis();
            outMappedBuf.put(bytes);
            long mid_4 = System.currentTimeMillis();
            size -= max;
            pos += max;

            System.out.println("*将文件流写入映射缓冲区：" + (mid_3 - mid_2));
            System.out.println("从映射缓冲区将文件流读出来：" + (mid_4 - mid_3));
        }
        bytes = null;

        MappedByteBuffer inMappedBuf = inChannel.map(MapMode.READ_ONLY, pos, size);
        MappedByteBuffer outMappedBuf = outChannel.map(MapMode.READ_WRITE, pos, size);

        bytes = new byte[inMappedBuf.limit()];

        long mid2 = System.currentTimeMillis();
        inMappedBuf.get(bytes);
        long mid3 = System.currentTimeMillis();
        outMappedBuf.put(bytes);
        long mid4 = System.currentTimeMillis();

        inChannel.close();
        outChannel.close();

        System.out.println("写入映射区域：" + (mid3 - mid2));
        System.out.println("从映射区域取出来：" + (mid4 - mid3));
        System.out.println("总耗时：" + (System.currentTimeMillis() - start));
    }

    /**
     * 用NIO读取大文本（1G以上）复制的成功I
     */
    @Test
    public void ts() throws IOException { // 2.5 -> 66065-68668-69828
        long start = System.currentTimeMillis();
        String movie = "E:\\Youku Files\\download\\001.kux";
        String movie2 = "E:\\Youku Files\\download\\01.mp4";
        String dir = "E:\\Youku Files\\download\\temp\\";
        FileInputStream fin = new FileInputStream(movie);
        FileChannel fcin = fin.getChannel();
        int max = Integer.MAX_VALUE >> 1;

        FileOutputStream fout = new FileOutputStream(dir + "1.mp4");
        FileChannel fcout = fout.getChannel();
        long size = fcin.size();
        long pos = 0;
        while (size > max) {
            fcout.transferFrom(fcin, pos, max);
            size -= max;
            pos += max;
        }
        fcout.transferFrom(fcin, pos, size);
        System.out.println(System.currentTimeMillis() - start);
    }


}

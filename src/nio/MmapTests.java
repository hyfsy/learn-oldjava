package nio;

import sun.nio.ch.DirectBuffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author baB_hyf
 * @date 2022/03/29
 */
public class MmapTests {

    public void clean() throws Exception {
        RandomAccessFile raf = new RandomAccessFile("E:\\a.txt", "rw");

        // MappedByteBuffer没有unmap方法，使得这个文件会一直被程序的资源句柄占用着无法释放
        MappedByteBuffer buf = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, raf.length());

        // op...

        if (buf.isDirect()) {
            DirectBuffer directBuffer = (DirectBuffer) buf;
            directBuffer.cleaner().clean();
        }
    }

    public void gatherScatter() throws Exception {
        RandomAccessFile raf = new RandomAccessFile("E:\\a.txt", "rw");

        // MappedByteBuffer没有unmap方法，使得这个文件会一直被程序的资源句柄占用着无法释放
        MappedByteBuffer buf = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, raf.length());

        // op...

        if (buf.isDirect()) {
            DirectBuffer directBuffer = (DirectBuffer) buf;
            directBuffer.cleaner().clean();
        }
    }
}

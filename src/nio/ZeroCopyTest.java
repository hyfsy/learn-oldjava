package nio;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * @author baB_hyf
 * @date 2022/02/02
 */
public class ZeroCopyTest {

    @Test
    public void mmap() {
        File file = new File("E:\\test.txt");
        File file2 = new File("E:\\test2.txt");
        try (FileChannel readChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
             FileChannel writeChannel = FileChannel.open(file2.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            MappedByteBuffer data = readChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            writeChannel.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendfile() {
        File file = new File("E:\\test.txt");
        File file2 = new File("E:\\test2.txt");
        try (FileChannel readChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
             FileChannel writeChannel = FileChannel.open(file2.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

            long len = readChannel.size();
            long position = readChannel.position();
            readChannel.transferTo(position, len, writeChannel);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

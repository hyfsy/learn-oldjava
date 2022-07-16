package nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author baB_hyf
 * @date 2022/02/08
 */
public class NioUseTest {

    @Test
    public void testUse1() throws Exception {
        // 堆内存，占用jvm内存
        ByteBuffer allocate = ByteBuffer.allocate(1);

        // 直接内存，不占用jvm内存，DirectByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1);
        byteBuffer.clear();

        FileChannel channel = new FileInputStream("xxx").getChannel();
        // 直接内存
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 0);
        map.clear();
    }
}

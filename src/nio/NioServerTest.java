package nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author baB_hyf
 * @date 2022/02/08
 */
public class NioServerTest {

    @Test
    public void server() throws IOException {
        // 1. 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        // 2. 切换非阻塞模式
        ssChannel.configureBlocking(false);
        // 3. 绑定连接
        ssChannel.bind(new InetSocketAddress(8081));
        // 4. 获取选择器
        Selector selector = Selector.open();
        // 5. 将通道注册到选择器上，并且指定监听接收事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 6. 轮询式的获取选择器上已经准备就绪的事件
        while (selector.select(0) > 0) {
            // 7. 获取当前选择器中所有注册的选择键（已就绪的监听事件）
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                // 8. 获取准备就绪的事件
                SelectionKey sk = it.next();
                // 9. 判断具体是什么事件准备就绪
                if (sk.isAcceptable()) {
                    // 10. 若接收就绪，获取客户端连接
                    SocketChannel sChannel = ssChannel.accept();
                    // 11. 切换非阻塞模式
                    sChannel.configureBlocking(false);
                    // 12. 将该通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);
                }
                else if (sk.isReadable()) {
                    // 13. 获取当前选择器上读就绪的通道
                    SocketChannel sChannel = (SocketChannel) sk.channel();
                    // 14. 读取数据
                    ByteBuffer buf = ByteBuffer.allocate(4096);
                    int len = 0;
                    while ((len = sChannel.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                    // 15. 客户端添加写事件
                    sChannel.register(selector, SelectionKey.OP_WRITE, buf);
                }
                else if (sk.isWritable()) {
                    // 16. 获取客户端连接
                    SocketChannel sChannel = (SocketChannel) sk.channel();
                    String content = "hello world";
                    byte[] bytes = ("HTTP/1.1 200 OK" +
                            "\r\n" +
                            "\r\n" +
                            content
                    ).getBytes(StandardCharsets.UTF_8);
                    // 17. 写入响应
                    ByteBuffer allocate = ByteBuffer.allocate(bytes.length);
                    allocate.put(bytes);
                    allocate.flip();
                    sChannel.write(allocate);
                    // 18. 关闭连接
                    sChannel.close();
                }
                // 19. 取消选择键SelectionKey
                it.remove();
            }
        }
    }

    @Test
    public void test1() throws IOException {
        // 1. 获取管道
        Pipe pipe = Pipe.open();
        // 2. 将缓冲区中的数据写入管道
        ByteBuffer buf = ByteBuffer.allocate(1024);
        Pipe.SinkChannel sinkChannel = pipe.sink();
        buf.put("通过单向管道发送数据".getBytes());
        buf.flip();
        sinkChannel.write(buf);

        // 额外线程

        // 3. 读取缓冲区中的数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        buf.flip();
        int len = sourceChannel.read(buf);
        System.out.println(new String(buf.array(), 0, len));

        sourceChannel.close();
        sinkChannel.close();
    }

    @Test
    public void test2() throws Exception {
        SelectorProvider provider = SelectorProvider.provider();

        AbstractSelector selector = provider.openSelector();
        ServerSocketChannel serverSocketChannel = provider.openServerSocketChannel();

        // ...
    }
}

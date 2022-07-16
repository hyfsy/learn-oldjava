package nio.model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2022/02/12
 */
public class NONBlockingIO {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080));

        // 服务端 accept 非阻塞
        server.configureBlocking(false); // NONBLOCKING

        // TCP 配置
        // channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

        List<SocketChannel> clients = new ArrayList<>();
        while (true) {

            Thread.sleep(1000L);

            SocketChannel client = server.accept(); // 不会阻塞，但会不断的循环读取判断是否有准备好的客户端

            if (client == null) {
                // no client connected
            }
            else {
                // 客户端 read 非阻塞
                client.configureBlocking(false);
                clients.add(client);
            }

            ByteBuffer buf = ByteBuffer.allocateDirect(4096); // 堆外内存

            for (SocketChannel c : clients) {
                int port = c.socket().getPort();

                int num = c.read(buf); // 不会阻塞，但会不断的循环读取判断是否有准备好的数据

                if (num < 0 /* -1 */) {
                    // client disconnect
                }
                else if (num == 0) {
                    // no data
                }
                else {
                    buf.flip();
                    byte[] bytes = new byte[buf.limit()];
                    buf.get(bytes);
                    String s = new String(bytes);
                    System.out.println(s + " : " + port);
                    buf.clear();
                }
            }

        }
    }
}

package nio;

import org.junit.Test;
import sun.nio.ch.DirectBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author baB_hyf
 * @date 2022/02/10
 */
public class IOModelTests {

    // BLOCKING IO
    // 阻塞
    @Test
    public void bio() throws Exception {
        ServerSocket server = new ServerSocket(8080, 20 /* acceptCount */);

        while (true) {
            Socket client = server.accept(); // block1

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

                        while (true) {
                            String s = br.readLine();// block2
                            if (s == null) {
                                client.close();
                                break;
                            }
                            else {
                                System.out.println("receive message: " + s);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    // NONBLOCKING IO
    // 循环系统调用，资源浪费
    @Test
    public void nio() throws Exception {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080));

        // 服务端 accept 非阻塞
        server.configureBlocking(false); // NONBLOCKING

        // Socket 配置
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
                    ((DirectBuffer) buf).cleaner().clean();
                }
            }

        }
    }

    // MULTIPLEXING IO
    // 每次重复传递数据，重复触发内核遍历
    @Test
    public void multiplexingSingle() throws Exception {
        // 多路复用器形式
        // select poll epoll(linux) kqueue(unix)
        // select 在用户态开辟一块空间，存着 socket fd，epoll在内核态开辟
        // select poll 的区别就在于select只能监控1024个fd（代码写死），poll没有限制，跟着操作系统的配置走

        // select会每次都去一个个询问，参数传递一堆fd
        // epoll 会用 epoll_create 创建一个本子，然后每个客户端的事件准备好后都会通过 epoll_ctl 记入
        // 询问时只要通过 epoll_wait 从本子中获取所有准备好的事件，减少了询问产生的系统调用
        // select需要传入全量fd，使用 epoll，方法不再需要传入fd，减少上下文切换的性能

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080));
        server.configureBlocking(false);

        // epoll_create
        Selector selector = Selector.open(); // 默认epoll

        // epoll_ctl
        server.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {

            Set<SelectionKey> keys = selector.keys(); // 所有注册的fd

            // epoll_wait
            while (selector.select(0/* 可指定timeout，0表示永久阻塞 */) > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys(); // 有状态的fd
                Iterator<SelectionKey> it = selectionKeys.iterator();

                // 只给状态，还是得自己处理R/W
                // 需要对每个fd进行系统调用
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove(); // 不移除会重复处理

                    if (key.isAcceptable()) {
                        // 获取服务端接收新连接
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel client = channel.accept();
                        client.configureBlocking(false);
                        ByteBuffer buf = ByteBuffer.allocateDirect(4096);
                        // epoll_ctl
                        client.register(selector, SelectionKey.OP_READ, buf);
                    }
                    else if (key.isReadable() || key.isWritable()) {
                        if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buf = (ByteBuffer) key.attachment();

                            int len;
                            while ((len = channel.read(buf)) > 0) {
                                byte[] bytes = new byte[len];
                                buf.flip();
                                buf.get(bytes);
                                System.out.println(new String(bytes, 0, len));
                                buf.clear();
                            }
                        }
                        else if (key.isWritable()) {
                            System.out.println("writable");
                        }
                    }
                }
            }
        }
    }
}

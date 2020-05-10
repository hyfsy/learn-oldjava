package socket.mina;

import java.net.InetSocketAddress;
import java.util.Scanner;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaClient
{

    public static void main(String[] args) {
        SocketConnector connector = new NioSocketConnector();
        
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();
        //传递对象
//        new ObjectSerializationCodecFactory();
        chain.addLast("myChain", new ProtocolCodecFilter(new TextLineCodecFactory()));
        
        connector.setHandler(new MinaClientHandler());
        
        //设置连接超时时间
        connector.setConnectTimeoutMillis(10000);
        //连接服务器
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress("localhost",8888));
        connectFuture.awaitUninterruptibly();//等待连接成功
        
        Scanner scanner = new Scanner(System.in);
        
        while(true) {
            System.out.println("请输入消息：");
            String info = scanner.nextLine();
          //发送消息
            connectFuture.getSession().write(info);
        }
        
        //等待服务器连接关闭,结束长连接
//        connectFuture.getSession().getCloseFuture().awaitUninterruptibly();
        //客户端关闭连接
//        connector.dispose();
    }

}

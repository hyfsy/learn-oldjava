package socket.wechat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * 聊天服务器
 * 创建聊天线程
 * @author baB_hyf
 *
 */
public class WeChatServer {
	private static final int SERVER_PORT=8888;//服务器端口
	//获取所有客户端socket，将一个人的所发信息发送给每个客户端
	public static List<Socket> socketList=new ArrayList<>();

	public static void main(String[] args) {
		ServerSocket ss=null;
		try {
			ss=new ServerSocket(SERVER_PORT);
			System.out.println("服务器开启成功！");
			while(true) {
				Socket s = ss.accept();
				System.out.println("S 通过连接："+s.getInetAddress()+" "+s.getLocalPort()+" "+s.getPort());
				socketList.add(s);
				new Thread(new ServerThread(s)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(ss!=null) {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

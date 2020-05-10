package socket.wechat;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientService {

	private static final String SERVER_IP="127.0.0.1";//服务器ip
	private static final int SERVER_PORT=8888;//服务器端口

	public static void chat(String username) {
		try {
			Socket s=new Socket(SERVER_IP,SERVER_PORT);
			System.out.println("服务器:"+SERVER_IP+" 连接成功");
			System.out.println("您可以输入消息了！");
			new Thread(new ClientControlThread(s,username)).start();//开启控制台监听线程
			new Thread(new ClientListenThread(s)).start();//开启服务器返回数据线程
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

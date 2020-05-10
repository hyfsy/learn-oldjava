package socket.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * 监听服务器推送消息线程
 * @author baB_hyf
 *
 */
public class ClientListenThread implements Runnable {
	private Socket s;

	public ClientListenThread() {};
	public ClientListenThread(Socket s) {
		this.s=s;
	}
	@Override
	public void run() {
		try {
			BufferedReader bis = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (true) {
				String msg = bis.readLine();//将信息打印到控制台
				System.out.println(msg);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package socket.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 接受用户输入消息线程
 * 
 * @author baB_hyf
 *
 */
public class ServerThread implements Runnable {

	private Socket s;

	public ServerThread() {
	};

	public ServerThread(Socket socket) {
		this.s = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader bis = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (true) {
				String msg = bis.readLine();// 接受客户端消息
				for (Socket item : WeChatServer.socketList) {
					if (!s.equals(item)) {
						PrintWriter pw = new PrintWriter(item.getOutputStream());
						pw.println(msg);// 将信息发送给客户端监听线程
						pw.flush();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package socket.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 监听用户输入线程
 * @author baB_hyf
 *
 */
public class ClientControlThread implements Runnable {
	private Socket s;
	private String username;
	
	public ClientControlThread() {};
	
	//顺便获取用户名
	public ClientControlThread(Socket s,String username) {
		this.s=s;
		this.username=username;
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter pw=new PrintWriter(s.getOutputStream());
			while (true) {
				String msg = br.readLine();//将信息发送给聊天服务器线程
				pw.println(username+": "+msg);
				pw.flush();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

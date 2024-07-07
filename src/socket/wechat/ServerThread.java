package socket.wechat;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
			BufferedReader bis2 = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (true) {
				String msg = bis2.readLine();// 接受客户端消息
				for (Socket item : WeChatServer.socketList) {
					if (!s.equals(item)) {
						PrintWriter pw = new PrintWriter(item.getOutputStream());
						pw.println(msg);// 将信息发送给客户端监听线程
						pw.flush();
                        // BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8)));
                        // BufferedOutputStream bos = new BufferedOutputStream(item.getOutputStream());
                        // int len;
                        // byte[] bytes = new byte[1024];
                        // while ((len = bis.read(bytes)) != -1) {
                        //     bos.write(bytes, 0, len);
                        // }
                        // bos.flush();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

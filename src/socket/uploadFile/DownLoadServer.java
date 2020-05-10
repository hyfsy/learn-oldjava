package socket.uploadFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DownLoadServer {
	//服务器监听端口
	private static final int SERVER_PORT = 8888;
	//服务器中的文件
	private static final String SRC_FILE = "E:\\cheatsheet.pdf";

	public static void main(String[] args) {
		try {
			upload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void upload() throws Exception {
		System.out.println("S: 开始监听端口连接..." + SERVER_PORT);
		ServerSocket ss = new ServerSocket(SERVER_PORT);
		Socket s = null;// 连接套接字对象
		Integer count = 0;// 连接次数
		OutputStream os = null;// 服务器输出流
		BufferedInputStream bis = null;// 文件输入流
		try {
			while (true) {
				s = ss.accept();
				System.out.println("S: 端口连接中..." + SERVER_PORT);
				count++;
				System.out.println("S: 正在进行" + count + "次连接...");
				os = s.getOutputStream();// 获取服务器输出流
				// 创建文件输入流
				bis = new BufferedInputStream(new FileInputStream(new File(SRC_FILE)));
				System.out.println("S: 开始传输文件...");
				// 开始传输
				int len = 0;
				byte[] bytes = new byte[1024];
				while ((len = bis.read(bytes)) != -1) {
					os.write(bytes, 0, len);
				}
				s.shutdownOutput();
				os.flush();
				System.out.println("S: 传输完成！！！\n");
			}
		} catch (Exception e) {
			System.out.println("S: error (连接对象无需要执行的服务)");
			e.printStackTrace();
		} finally {
			bis.close();
			os.close();
			s.close();
			ss.close();
			System.out.println("S: Done.");
		}
	}

}

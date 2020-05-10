package socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPAccept {
	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(8888);// 创建服务器套接字
		System.out.println("正在监听。。。");
		Socket s = ss.accept();// 阻塞 接受套接字
		InputStream is = s.getInputStream();// 获取输入流，得到服务器发送的信息
		byte[] bytes = new byte[1024];
		int len = is.read(bytes, 0, bytes.length);// 读取信息
		String msg = new String(bytes, 0, len);
		System.out.println(msg);
		System.out.println("接受成功");

		//网页输出
//		OutputStream os = s.getOutputStream();//向输出流写入数据
//		os.write("<font color='red'>网页测试</font>".getBytes("gbk"));
//		os.write("<h1>网页测试</h1>".getBytes("gbk"));
//
//		s.shutdownOutput();// 显示消息
////		s.setKeepAlive(true);

		// 关闭连接
//		os.close();
		s.close();
		ss.close();
	}

}

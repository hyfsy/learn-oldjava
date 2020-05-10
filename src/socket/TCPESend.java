package socket;

import java.io.OutputStream;
import java.net.Socket;

public class TCPESend {

	public static void main(String[] args) throws Exception {
		Socket s=new Socket("localhost", 8888);//创建服务器 IP 端口
		OutputStream os = s.getOutputStream();//获取输出流
		String msg="TCP发送数据";
		os.write(msg.getBytes());//将信息写入输出流，传给接收方（客户端）
		os.close();
	}

}

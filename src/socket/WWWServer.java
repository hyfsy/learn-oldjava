package socket;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class WWWServer {

	public static void main(String[] args) throws Exception {
		Socket s=new Socket("localhost",8080);//tomcat服务器端口
		PrintWriter pw =new PrintWriter(s.getOutputStream());
		
		//消息头
		pw.println("GET /test/test.html HTTP/1.1");
		pw.println("Host: localhost:8888");
		pw.println();//消息体
		pw.println("<font color='red'>网页测试</font>");
		pw.flush();

		//控制台打印返回数据
		InputStream is2=s.getInputStream();
		byte[] bytes2=new byte[1024];
		int read = is2.read(bytes2, 0, bytes2.length);
		System.out.println(new String(bytes2,0,read));
		
		//关闭连接
		pw.close();
		s.close();
	}

}

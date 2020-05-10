package socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPAccept {

	public static void main(String[] args) throws Exception {
		DatagramSocket ds=new DatagramSocket(8888);//本机端口
		byte[] bytes=new byte[1024];
		DatagramPacket dp =new DatagramPacket(bytes, bytes.length);//创建接受数据包
		System.out.println("开始接受");
		ds.receive(dp);//阻塞
		System.out.println("接受成功");
		String msg=new String(dp.getData(),0,dp.getLength());//输出接受数据
		System.out.println(msg);
		ds.close();
	}

}

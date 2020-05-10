package socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSend {

	public static void main(String[] args) throws Exception {
		DatagramSocket ds=new DatagramSocket(8888);//接收方端口
		String s="UDP发送数据";
		InetAddress ia = InetAddress.getByName("192.168.181.28");//获取接收方ip
		//填入接收方 IP 端口
		DatagramPacket dp=new DatagramPacket(s.getBytes(), s.getBytes().length, ia, 10050);//注意length
		ds.send(dp);//发送
		ds.close();
	}

}

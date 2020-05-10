package socket.uploadFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private static final String SERVER_IP = "127.0.0.1";
	//连接的服务器端口
	private static final int SERVER_PORT = 8888;
	// 下载文件的路径
	private static final String DESC_FILE = "C:\\Users\\baB_hyf\\Desktop";
	private static final String suffix=".pdf";
	
	public static void main(String[] args) {
		BufferedOutputStream bos=null;
		Socket s =null;
		try {
			bos=new BufferedOutputStream(new FileOutputStream(new File(DESC_FILE+File.separator+System.currentTimeMillis()+suffix)));
			
			s=new Socket(SERVER_IP, SERVER_PORT);
			BufferedInputStream bis=new BufferedInputStream(s.getInputStream());
//			System.out.println("C: 正在下载文件...");
			byte[] bytes=new byte[1024];
			int len=0;
			while((len=bis.read(bytes))!=-1) {
				bos.write(bytes,0,len);
			}
			bos.flush();
			bis.close();
//			System.out.println("C: 下载完成！！！");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {	
			try {
				bos.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

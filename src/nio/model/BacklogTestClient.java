package nio.model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author baB_hyf
 * @date 2022/02/13
 */
public class BacklogTestClient {

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 10; i++) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("localhost", 8081));
                System.out.println(socket.getLocalAddress() + ":" + socket.getLocalPort());
                System.out.println("connect success: " + i);
            } catch (IOException e) {
                System.out.println("connect failed: " + i);
                // e.printStackTrace();
            }
        }

        synchronized (BacklogTestClient.class) {
            BacklogTestClient.class.wait();
        }
    }
}

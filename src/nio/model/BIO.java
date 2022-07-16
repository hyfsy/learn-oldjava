package nio.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author baB_hyf
 * @date 2022/02/12
 */
public class BIO {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8081);

        while (true) {
            Socket client = server.accept(); // block1

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

                        while (true) {
                            String s = br.readLine();// block2
                            if (s == null) {
                                client.close();
                                break;
                            }
                            else {
                                System.out.println("receive message: " + s);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

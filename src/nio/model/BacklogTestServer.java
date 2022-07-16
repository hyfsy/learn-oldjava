package nio.model;

import java.net.ServerSocket;

/**
 * @author baB_hyf
 * @date 2022/02/13
 */
public class BacklogTestServer {

    public static void main(String[] args) throws Exception {

        int backlog = 4;

        ServerSocket server = new ServerSocket(8081, backlog);
        while (true) {
            // no accept
            Thread.sleep(1);
        }
    }
}

package socket.wechat;

public class Client1 {

	private static final String USER_NAME="张三";

	public static void main(String[] args) {
		ClientService.chat(USER_NAME);
	}

}

package thread.pool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * 线程工厂类
 * 
 * @author baB_hyf
 *
 */
class MyThreadFactory implements ThreadFactory {

	private Integer count;
	private String name;
	private List<String> statusList;

	public MyThreadFactory(String name) {
		count = 0;
		this.name = name;
		statusList = new ArrayList<>();
	}

	// 仅有一个方法就是创建线程
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, name + "-" + count);
		count++;
		// 创建一个线程就向状态列表中添加信息
		statusList.add(String.format("create new Thread %d with name %s on %s", t.getId(), t.getName(), new Date()));
		
		t.setDaemon(false);//设置为不是守护线程
		return t;
	}

	// 获取创建的所有线程信息
	public String getStatus() {
		StringBuffer sb = new StringBuffer();
//		Iterator<String> iterator = statusList.iterator();
//		while(iterator.hasNext()) {
//			sb.append(iterator.next()+"\n");
//		}
		for (String str : statusList) {
			sb.append(str);
			sb.append("\n");
		}
		return sb.toString();
	}

}

/**
 * 线程run方法执行类
 * 
 * @author baB_hyf
 *
 */
class Task implements Runnable {
	int num;

	public Task(int num) {
		this.num = num;
	}

	@Override
	public void run() {
		System.out.println("线程"+num + " 创建成功");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

/**
 * 主函数测试类
 * 
 * @author baB_hyf
 *
 */
public class MyThreadFactoryTest {
	public static void main(String[] args) {
		MyThreadFactory mtf = new MyThreadFactory("my_thread_factory");
		for (int i = 0; i < 10; i++) {
			Thread newThread = mtf.newThread(new Task(i));
			newThread.start();
		}
		System.out.printf("Thread ststus:\n");
		System.out.printf("%s",mtf.getStatus());
		System.out.println("create thread end.");
	}
}

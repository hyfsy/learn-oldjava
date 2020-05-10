package thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 固定数量线程池 可控制最大并发数 剩下的线程会在队列中等待
 * 
 * @author baB_hyf
 *
 */
public class FixedPool {

	public static void main(String[] args) {
		// 创建固定线程池对象
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(4);
		for (int i = 0; i < 10; i++) {
			int index = i;
			newFixedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					System.out.println(index);
					try {
						// 由于最大线程数只有4,所以每次打印4次，等待了两秒，才继续
						System.out.println(index);
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});

		}
		newFixedThreadPool.shutdown();// 关闭
	}

}

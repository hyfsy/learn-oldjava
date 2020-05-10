package thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 单线程化的线程池
 * 唯一的工作线程
 * 保证任务按照指定顺序（FIFO,LIFO,优先级）执行
 * @author baB_hyf
 *
 */
public class SingleExecutor {

	public static void main(String[] args) {
		ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
		for(int i=0;i<10;i++) {
			int count=i;
			newSingleThreadExecutor.execute(new Runnable() {

				@Override
				public void run() {
					System.out.println(count+" 顺序执行");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			});
		}
		newSingleThreadExecutor.shutdown();//关闭
	}

}

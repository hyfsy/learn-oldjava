package thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 缓冲线程池
 * 可自动增大减小
 * @author baB_hyf
 *
 */
public class CachedPool {

	public static void main(String[] args) {
		//创建线程池
		ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
		for(int i=0;i<10;i++) {
			int index=i;//匿名内部类中不能使用非final类型
			
			//循环生产线程执行
			newCachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(index);
				}		
			});
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//停止线程池
		newCachedThreadPool.shutdown();
	}

}

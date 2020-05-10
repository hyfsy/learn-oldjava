package thread.pool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 *  创建默认线程（初始线程数，最大线程数，线程存活时间，消息队列，线程工厂，handler）
 *  
 *  
 *.重点讲解： 
 *.其中比较容易让人误解的是：corePoolSize，maximumPoolSize，workQueue之间关系。 
 *
 *1.当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。 
 *2.当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行 
 *3.当workQueue已满，且maximumPoolSize>corePoolSize时，新提交任务会创建新线程执行任务 
 *4.当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理  (拒绝策略)
 *5.当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程 
 *6.当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭 
 *  
 * @author baB_hyf
 *
 */
public class ThreadObject {
	
	public static void main(String[] args) {
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(10, 50, 5, TimeUnit.SECONDS,
				new LinkedBlockingDeque<Runnable>());
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS,new SynchronousQueue(),
				new ThreadFactory() { // 自定义ThreadFactory
			
					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(r);
						System.out.println("生成线程 " + t);
						return t;
					}
				});
		
		Runnable r = () -> {
			System.out.println(Thread.currentThread().getName() + " 在忙...");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};

		for (int i = 0; i < 10; i++) {
			executor.submit(r);
		}
		executor.shutdown();
	}

}

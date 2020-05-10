package thread.pool;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 从其他队列里窃取任务来执行 充分利用线程并行操作 双端队列，从两端拿，还是可能冲突
 * 
 * @author baB_hyf
 *
 */
public class WorkStealingPool {
	// 总线程数
	private static int threads = 50;

	// 固定每组有四个线程执行，可循环
	static CyclicBarrier cyclicBarrier = new CyclicBarrier(4);

	public static void main(String[] args) throws InterruptedException, ExecutionException, BrokenBarrierException {
		
		// 创建偷窃线程池
		// 设置并行级别为3，即默认每时每刻只有3个线程同时执行
		ExecutorService newWorkStealingPool = Executors.newWorkStealingPool(10);

		for (int i = 0; i < threads; i++) {
			
//			// 未来任务
//			FutureTask<?> futureTask = new FutureTask<>(new Callable<String>() {
//				@Override
//				public String call() throws Exception {
//					return Thread.currentThread().getName() + " 开始执行";
//				}
//			});
//
//			 执行 submit可有返回结果 execute无返回结果
//			newWorkStealingPool.submit(new Thread(futureTask));
//			//输出信息
//			System.out.println(futureTask.get());
			
			
//			System.out.println(i+" 准备中......");
//			//每四个线程执行后，再同步继续下去
//			cyclicBarrier.await();
//			System.out.println(i+" 继续执行");
			
			newWorkStealingPool.submit(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread()+" 正在执行");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			
		}
	
		//观察结果，否则是看不到结果的
		newWorkStealingPool.awaitTermination(20, TimeUnit.SECONDS);
		newWorkStealingPool.shutdown();//关闭
	}

}

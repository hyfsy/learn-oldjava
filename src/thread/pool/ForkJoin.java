package thread.pool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

/**
 * 可以根据CPU的核数并行的执行，适合使用在很耗时的操作，可以充分的利用CPU执行任务
 * 并行执行
 * @author baB_hyf
 *
 */
public class ForkJoin {
	//定义线程数量
	private static int threads=10;
	//定义一个线程计数器
	static CountDownLatch countDownLatch=new CountDownLatch(threads);

	public static void main(String[] args) {
		//线程池
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		
		for(int i=0;i<threads;i++) {
			final int count=i;//匿名内部类中使用
			forkJoinPool.execute(()->{
				System.out.println(count+" cpu使用量："+forkJoinPool.getStealCount()+" 并行数量(cpu总内核线程)："+forkJoinPool.getParallelism());		
				//将计数器减少
				countDownLatch.countDown();
			});	
		}
		try {
			countDownLatch.await();//计数器为0后向下执行
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			forkJoinPool.shutdown();//关闭线程池
		}
	}

}

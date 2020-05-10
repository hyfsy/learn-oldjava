package thread.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * 无限大小的线程池 
 * 周期性执行操作
 * @author baB_hyf
 *
 */
public class ScheduledPool {

	public static void main(String[] args) {
		ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(4);
		for (int i = 0; i < 10; i++) {
			int count=i;
			//schedule(Runnable,延迟秒数,时间单位)

			// 执行schedule方法为每次执行时定时延迟1秒,随后每次执行等待两秒
			newScheduledThreadPool.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					System.out.println(count+"延迟一秒执行，执行完后等待两秒");
				}
				
			}, 1, 2, TimeUnit.SECONDS);//延迟一秒执行，执行完后等待两秒
			
			//scheduleWithFixedDelay(Runnable,延迟秒数,每次任务延迟秒数,时间单位)
			
			/**
			 * 两种区别：
			 * 延迟1秒 每次任务延迟2秒
			 * 1：FixedRate绝对执行，第二次就是第3秒执行
			 * 2：FixedDelay相对执行，第二次是在上一个任务执行完毕后延迟2秒执行
			 * 
			 */
			
		}

		newScheduledThreadPool.shutdown();//关闭
	}

}

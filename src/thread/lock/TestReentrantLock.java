package thread.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * 
 * [可重入互斥锁]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月22日]
 */
public class TestReentrantLock extends Thread
{

    // 传入true为公平锁
    private ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Object o=new Object();
        
        AtomicInteger atomicInteger = new AtomicInteger();
//        atomicInteger.compareAndSet(expect, update);
        Map<String, Object> map=new HashMap<>(10);
        map.put("1", 123);
    }
    
    @Test
    public void test() {
        Condition newCondition = reentrantLock.newCondition();
        
    }

}

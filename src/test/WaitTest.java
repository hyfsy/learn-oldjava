package test;/**
 * 
 * 
 * @author baB_hyf
 * @date 2022/02/25
 */
public class WaitTest {

    static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        assert args.length == 1;

        synchronized (lock) {
            WaitTest.class.wait();
        }
    }
}

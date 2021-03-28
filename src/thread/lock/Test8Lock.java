package thread.lock;

import org.junit.Test;

/**
 * @author baB_hyf
 * @date 2020/11/10
 */
public class Test8Lock {

    @Test
    public void testCommon() {
        // 1.两个普通同步方法,两个线程,标准打印,打印?       // one two
        // 2.新增Thread.sleep()给getOne() ,打印?       // one two
        // 3.新增普通方法getThree() ,打印?              // three one two
        // 4.两个普通同步方法,两个对象,打印?              // two one
        // 5.修改getOne()为静态同步方法,打印?            // two one
        // 6.修改两个方法均为静态同步方法,一个对象?        // one two
        // 7.一个静态同步方法,一个非静态同步方法,两个对象?  // two one
        // 8.两个静态同步方法,两个对象?                  // one two
        //
        // 线程八锁的关键:
        // ①非静态方法的锁默认为this, 静态方法的锁为对应的Class实例
        // ②某一个时刻内，只能有一个线程持有锁，无论几个方法
    }

    public void testOne() {
        Execute execute = new Execute();
        new Thread(() -> execute.getOne()).start();
        new Thread(() -> execute.getTwo()).start();
    }
    public void testTwo() {
        Execute execute = new Execute();
        new Thread(() -> execute.getOne()).start();
        new Thread(() -> execute.getTwo()).start();
    }
    public void testThree() {
        Execute execute = new Execute();
        new Thread(() -> execute.getOne()).start();
        new Thread(() -> execute.getTwo()).start();
        new Thread(() -> execute.getThree()).start();
    }
    public void testFour() {
        Execute execute = new Execute();
        Execute execute2 = new Execute();
        new Thread(() -> execute.getOne()).start();
        new Thread(() -> execute2.getTwo()).start();
    }
    public void testFive() {
        Execute execute = new Execute();
        Execute execute2 = new Execute();
        new Thread(() -> execute.getOne()).start();
        new Thread(() -> execute2.getTwo()).start();
    }
    public void testSix() {
        Execute execute = new Execute();
        new Thread(() -> execute.getOne()).start();
        new Thread(() -> execute.getTwo()).start();
    }
    public void testSeven() {
        Execute execute = new Execute();
        Execute execute2 = new Execute();
        new Thread(() -> execute.getOne()).start();
        new Thread(() -> execute2.getTwo()).start();
    }
    public void testEight() {
        Execute execute = new Execute();
        Execute execute2 = new Execute();
        new Thread(() -> execute.getOne()).start();
        new Thread(() -> execute2.getTwo()).start();
    }

    public static void main(String[] args) {
        Test8Lock test8Lock = new Test8Lock();
        // test8Lock.testOne();
        // test8Lock.testTwo();
        // test8Lock.testThree();
        // test8Lock.testFour();
        // test8Lock.testFive();
        // test8Lock.testSix();
        // test8Lock.testSeven();
        test8Lock.testEight();
    }

}

class Execute {
    public static synchronized void getOne() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    public static synchronized void getTwo() {
        System.out.println("two");
    }

    public void getThree() {
        System.out.println("three");
    }
}

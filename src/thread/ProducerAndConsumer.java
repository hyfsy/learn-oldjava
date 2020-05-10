package thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 *  [生产者消费者]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月22日]
 */
public class ProducerAndConsumer
{

    public static void main(String[] args) {
        Part part = new Part();
        Worker worker = new Worker(part);
        Buyer buyer = new Buyer(part);
        Thread w = new Thread(worker);
        Thread b = new Thread(buyer);
        b.start();
        w.start();
    }

}

/**
 * 
 * [消费者]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月22日]
 */
class Buyer implements Runnable
{
    private Part part;

    public Buyer(Part part) {
        this.part = part;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            part.buyedPart();
        }
    }

}

/**
 * 
 * [生产者]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月22日]
 */
class Worker implements Runnable
{
    int count = 0;

    String[] partKind = new String[] {"老虎钳", "电钻", "开瓶器", "齿轮", "电线" };

    int[] partPrice = new int[] {20, 55, 6, 120, 31 };

    private Part part;

    public Worker(Part part) {
        this.part = part;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            part.markedPart(partKind[count % 5], partPrice[count % 5]);
            count++;

        }
    }

}

/**
 * 
 * [零件类]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月22日]
 */
class Part
{

    private String name;

    private Integer price;

    // 传入true为公平锁
    private ReentrantLock reentrantLock = new ReentrantLock();

    // 零件的数量
    private int partNum;

    /**
     * 
     * [制造零件]
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public synchronized void markedPart(String name, Integer price) {
        // reentrantLock.lock();
        
        //此处没看懂
        if (partNum > 0) {
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                // reentrantLock.unlock();
            }
        }

        this.setName(name);
        this.setPrice(price);
        try {
            Thread.sleep(1000);
            partNum++;
            System.out.println("工人：制造了" + this.getName() + "，价格：" + this.getPrice());
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 将阻塞的线程释放
        this.notifyAll();
        // reentrantLock.unlock();
    }

    /**
     * 
     * [购买零件]
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public synchronized void buyedPart() {
        // 开启锁
        // reentrantLock.lock();

        if (partNum <= 0) {
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                // 此处注意解锁，防止造成死锁
                // reentrantLock.unlock();
            }
        }

        System.out.println("顾客：购买了" + this.getName() + "，用了" + this.getPrice() + "元");
        partNum--;
        this.notifyAll();

        // 取消锁
        // reentrantLock.unlock();
    }

    public Part() {
        super();
    }

    public Part(String name, Integer price) {
        super();
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}

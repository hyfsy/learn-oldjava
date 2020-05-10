package list;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class QueueTest
{

    public static void main(String[] args) {
        queue();
        System.out.println();
        deque();
        System.out.println();
        stack();
    }
    
    /**
     * 
     *  [队列(FIFO)]     
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void queue() {
        Queue<String> queue=new LinkedList<>();
        //增加（相同）
        queue.add("张三1");
        queue.offer("张三2");
        queue.offer("张三3");
        System.out.println(queue.size());
        //获取头部对象但不删除
        //queue.element();
        System.out.println(queue.peek());//偷窥
        
        //删除第一个添加的元素 null不会报错
        queue.poll();
        queue.remove();
        System.out.println(queue.size());
        System.out.println(queue);
    }
    
    /**
     * 
     *  [双端队列]     
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void deque() {
        Deque<String> deque=new LinkedList<>();
        //......方法都有对第一个和最后一个的操作
        deque.add("张三2");
        deque.addFirst("张三1");
        deque.addLast("张三3");
        //相同
        deque.offerFirst("张三0");
        System.out.println(deque);
        
    }
    
    /**
     * 
     *  [堆栈（类）先进后出]     
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void stack() {
        Stack<String> stack=new Stack<>();
        stack.push("张三1");
        stack.push("张三2");
        stack.push("张三3");
        stack.pop();
        System.out.println(stack);
    }

}

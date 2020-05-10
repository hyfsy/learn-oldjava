package designmodel.other.memento;

import java.util.Stack;

/**
 * 备忘录管理类
 * 
 * @author baB_hyf
 */
public class MementoManager
{
    /**
     * 备忘录栈
     */
    Stack<Memento> stack = new Stack<>();

    /**
     * 将备忘录保存到栈
     */
    public void setMemento(Memento memento) {
        stack.push(memento);
    }

    /**
     * 获取最近的存档
     */
    public Memento getMemento() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.pop();
    }

}

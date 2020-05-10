package designmodel.other.mediator;

/**
 * 中介者类
 * 
 * @author baB_hyf
 */
public class ComputerCenter
{

    /**
     * 各个对象之间的交互在此处执行
     */
    public void contact(String message, Computer computer) {
        computer.showMessage(message);
    }

}

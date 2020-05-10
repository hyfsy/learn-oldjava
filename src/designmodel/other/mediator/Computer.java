package designmodel.other.mediator;

/**
 * 抽象计算机类
 * 
 * @author baB_hyf
 */
public abstract class Computer
{
    private ComputerCenter center;

    // 计算机之间联系的方法
    public abstract void contact(String message);

    // 子类共有的方法
    public void showMessage(String message) {
        System.out.println(this.getClass().getSimpleName() + "说了：" + message);
    }

    public ComputerCenter getCenter() {
        return center;
    }

    public void setCenter(ComputerCenter center) {
        this.center = center;
    }

}

package designmodel.other.memento;

/**
 * 备忘录类
 * 
 * @author baB_hyf
 */
public class Memento
{
    /**
     * 和骑士类共有的属性值
     */
    private int blood;

    /**
     * 将会还原的属性值保存在备忘录中
     */
    public Memento(int blood) {
        this.blood = blood;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

}

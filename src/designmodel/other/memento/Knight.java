package designmodel.other.memento;

/**
 * 骑士，原发器
 * 
 * @author baB_hyf
 */
public class Knight
{
    private int blood;
    
    public Knight(int blood) {
        this.blood = blood;
    }

    /**
     * 将当前的状态保存到备忘录中
     */
    public Memento saveMemento(){
        return new Memento(blood);
    }
    
    /**
     * 返回保存前的状态
     */
    public void restoreMemento(Memento memento){
        this.blood = memento.getBlood();
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }
    
    

}

package designmodel.other.builder;

import java.io.Serializable;

/**
 * 房屋对象
 * 
 * @author baB_hyf
 */
public class House implements Serializable//原型克隆要实现序列化接口
{

    private static final long serialVersionUID = 1L;
    
    public String brick;
    public String paint;
    public String people;
    public String getBrick() {
        return brick;
    }
    public void setBrick(String brick) {
        this.brick = brick;
    }
    public String getPaint() {
        return paint;
    }
    public void setPaint(String paint) {
        this.paint = paint;
    }
    public String getPeople() {
        return people;
    }
    public void setPeople(String people) {
        this.people = people;
    }
    @Override
    public String toString() {
        return "House [brick=" + brick + ", paint=" + paint + ", people=" + people + "]";
    }
    
    
}

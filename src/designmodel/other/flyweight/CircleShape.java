package designmodel.other.flyweight;

/**
 * 具体对象
 * 
 * @author baB_hyf
 */
public class CircleShape extends Shape
{
    //具体实现类中的是内部状态
    private String color;

    public CircleShape(String color) {
        this.color = color;
    }

    @Override
    public void getShape() {
        System.out.println("一个" + color + "的圆形");
    }

}

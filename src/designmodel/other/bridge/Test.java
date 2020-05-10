package designmodel.other.bridge;

/**
 * 失败了，理解意思就好
 * 
 * 目的在于把抽象类和实现类解耦，使得二者可以独立变化
 * 
 * @author baB_hyf
 */
public class Test
{

    public static void main(String[] args) {
        Shape shape = new SquareShape();
        shape.setColor(new RedColor());
        shape.setLine(new SolidLine());
        shape.draw();

    }

}

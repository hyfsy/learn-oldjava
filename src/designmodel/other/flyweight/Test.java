package designmodel.other.flyweight;

/**
 * 享元模式
 * 
 * 优点：
 * 能够极大的减少系统中对象的个数
 * 外部状态相对独立，不会影响到内部状态
 * 
 * 缺点：
 * 需要区分外部状态和内部状态，使得应用程序在某种程度上来说更加复杂化
 * 
 * @author baB_hyf
 */
public class Test
{

    public static void main(String[] args) {
          FlyWeightFactory.createCircleShape("白色");
          FlyWeightFactory.createCircleShape("红色");
          FlyWeightFactory.createCircleShape("蓝色");
          FlyWeightFactory.createCircleShape("绿色");
          FlyWeightFactory.createCircleShape("白色");
          FlyWeightFactory.createCircleShape("红色");
          FlyWeightFactory.createCircleShape("蓝色");
          System.out.println(FlyWeightFactory.getShapeNumber());
    }

}

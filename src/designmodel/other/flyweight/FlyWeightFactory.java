package designmodel.other.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 主要的工厂类
 * 
 * @author baB_hyf
 */
public class FlyWeightFactory
{
    //保存相同元素的池
    private static Map<String, Shape> shapeMap = new HashMap<>();

    //工厂获取对象的方法
    public static Shape createCircleShape(String color) {
        if (shapeMap.get(color) != null) {
            return shapeMap.get(color);
        }
        else {
            //池子里不存在对象就创建一个
            Shape shape = new CircleShape(color);
            shapeMap.put(color, shape);
            return shape;
        }
    }

    public static int getShapeNumber() {
        return shapeMap.size();
    }

}

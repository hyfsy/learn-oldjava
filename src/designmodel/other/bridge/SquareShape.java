package designmodel.other.bridge;

public class SquareShape extends Shape{

    @Override
    void draw() {
        System.out.println("正方形 color: " + color.colorType + "line: " + line.lineType);
        
    }

}

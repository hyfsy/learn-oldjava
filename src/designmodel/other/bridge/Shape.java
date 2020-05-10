package designmodel.other.bridge;

/**
 * 桥
 * 
 * @author baB_hyf
 */
public abstract class Shape
{
    protected Color color;
    protected Line line;
    
    abstract void draw();
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public void setLine(Line line) {
        this.line = line;
    }
}

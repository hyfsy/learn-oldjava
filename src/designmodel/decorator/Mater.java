package designmodel.decorator;

/**
 * 
 *  [具体装饰者类] 
 *  
 *  鸡蛋类
 *  
 * @author baB_hyf
 * @version [版本号, 2019年9月15日]
 */
public class Mater extends AbstractMater
{

    public Mater(CakeI cake) {
        super(cake);
    }

    @Override
    public double getPrice() {
        return super.getPrice()+1;
        
    }

    @Override
    public String getMater() {
        return super.getMater()+"+鸡蛋";
        
    }

}

/**
 * 
 *  [火腿类] 
 * @author baB_hyf
 * @version [版本号, 2019年9月15日]
 */
class Ham extends AbstractMater{

    public Ham(CakeI cake) {
        super(cake);
    }
    
    @Override
    public double getPrice() {
        return super.getPrice()+1;
        
    }

    @Override
    public String getMater() {
        return super.getMater()+"+火腿";
        
    }
    
}

/**
 * 
 *  [里脊肉类] 
 * @author baB_hyf
 * @version [版本号, 2019年9月15日]
 */
class Chine extends AbstractMater{

    public Chine(CakeI cake) {
        super(cake);
    }
    
    @Override
    public double getPrice() {
        return super.getPrice()+2.5;
        
    }

    @Override
    public String getMater() {
        return super.getMater()+"+里脊肉";
        
    }
    
}

/**
 * 
 *  [生菜类] 
 * @author baB_hyf
 * @version [版本号, 2019年9月15日]
 */
class Lettuce extends AbstractMater{

    public Lettuce(CakeI cake) {
        super(cake);
    }
    
    @Override
    public double getPrice() {
        return super.getPrice()+0.5;
        
    }

    @Override
    public String getMater() {
        return super.getMater()+"+生菜";
        
    }
    
}
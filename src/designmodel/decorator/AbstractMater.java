package designmodel.decorator;

/**
 * 
 *  [装饰者基类] 
 * @author baB_hyf
 * @version [版本号, 2019年9月15日]
 */
public abstract class AbstractMater implements CakeI
{
 
    //被装饰者接口
    private CakeI cake;
    
    public AbstractMater(CakeI cake) {
        super();
        this.cake = cake;
    }

    @Override
    public double getPrice() {
        return cake.getPrice();
    }

    @Override
    public String getMater() {
        return cake.getMater();
    }

   
    
    

}

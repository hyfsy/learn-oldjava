package designmodel.decorator;

/**
 * 
 *  [被装饰者] 
 * @author baB_hyf
 * @version [版本号, 2019年9月15日]
 */
public class Cake implements CakeI
{

    @Override
    public double getPrice() {
        return 10;
        
    }

    @Override
    public String getMater() {
        return "普通饼";
        
    }

}

package designmodel.other.builder;

/**
 * 建造者接口
 * 
 * @author baB_hyf
 */
public interface HouseBuilderI
{
    //砖块
    void setBrick();
    
    //漆
    void setPaint();
    
    //工人
    void setPeople();
    
    House createHouse();
    
}

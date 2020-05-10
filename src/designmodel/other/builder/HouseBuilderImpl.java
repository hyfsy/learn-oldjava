package designmodel.other.builder;

/**
 * 真正的建造者
 * 
 * @author baB_hyf
 */
public class HouseBuilderImpl implements HouseBuilderI
{
    
    private House house = new House();

    @Override
    public void setBrick() {
        house.setBrick("orangle brick");
    }

    @Override
    public void setPaint() {
        house.setPaint("red paint");
    }

    @Override
    public void setPeople() {
        house.setPeople("worker");
    }

    @Override
    public House createHouse() {
        setBrick();
        setPaint();
        setPeople();
        return this.house;
    }

}

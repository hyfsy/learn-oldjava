package designmodel.other.prototype;

public class Test
{

    public static void main(String[] args) {
        HousePrototype housePrototype = new HousePrototype();
        System.out.println(housePrototype.i);
        System.out.println(housePrototype.s);
        System.out.println(housePrototype.house);
        
       
        
        try {
            Object clone = housePrototype.clone();
            
            //修改克隆前的状态，判断克隆的对象会不会变化，变化说明克隆失败
            housePrototype.i++;
            housePrototype.s +="asdf";
            housePrototype.house.brick = "null";
            
            if(clone instanceof HousePrototype) {
                HousePrototype prototype = (HousePrototype)clone;
                System.out.println(prototype.i);
                System.out.println(prototype.s);
                System.out.println(prototype.house);
            }
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
        

    }

}

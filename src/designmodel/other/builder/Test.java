package designmodel.other.builder;

public class Test
{

    public static void main(String[] args) {
        HouseBuilderI builder = new HouseBuilderImpl();
        House house = builder.createHouse();
        System.out.println(house);

    }

}

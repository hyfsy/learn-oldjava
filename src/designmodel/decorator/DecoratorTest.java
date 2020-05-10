package designmodel.decorator;

/**
 * 
 * [测试装饰]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月15日]
 */
public class DecoratorTest
{

    public static void main(String[] args) {
        // 普通饼
        CakeI cake = new Cake();
        // 鸡蛋
        Mater mater = new Mater(cake);
        // 火腿
        Ham ham = new Ham(mater);
        // 里脊肉
        Chine chine = new Chine(ham);
        // 生菜
        Lettuce lettuce = new Lettuce(chine);
        Mater mater2 = new Mater(lettuce);
        System.out.println("您买了：" + mater2.getMater());
        System.out.println("共花费：" + mater2.getPrice() + " 元");

    }

}

package designmodel.proxy.diyproxy;

import java.lang.reflect.Method;

/**
 * 
 * [处理器接口]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月4日]
 */
public interface InvocationHandler
{
    void invoke(Object o, Method method);
}

/**
 * 
 * [处理器具体实现]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月4日]
 */
class ShopHandler implements InvocationHandler
{

    private Object o;

    public ShopHandler(Object o) {
        this.o = o;
    }

    @Override
    public void invoke(Object o, Method method) {

        System.out.println("开始购物......");

        try {
            method.invoke(this.o);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("结束购物.");

    }

}

/**
 * 
 * [主题对象]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月3日]
 */
interface Subject
{
    void shopping();
}

/**
 * 
 * [被代理对象]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月3日]
 */
class Person implements Subject
{

    @Override
    public void shopping() {
        System.out.println("成功购买了商品");
    }

}

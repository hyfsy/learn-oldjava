package designmodel.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 
 * [动态代理]
 * [只能代理实现了接口的类]
 * [没有实现接口的类不能实现JDK的动态代理]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月3日]
 */
public class DynamicProxy
{
    public static void main(String[] args) {
        Subject p = new Person();
        CreateProxy cp = new CreateProxy();
        
        //该类型实际上不算任何类，只是运行时动态产生的类
        Subject proxy = (Subject) cp.create(p);
        //实际调用了invocationHandler的invoke方法
        proxy.shopping();
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

/**
 * 
 * [创建代理对象类]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月3日]
 */
class CreateProxy implements InvocationHandler
{

    /**
     * 被代理对象
     */
    Object object;

    /**
     * 
     * [创建代理对象]
     * 
     * @param o
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Object create(Object o) {
        this.object = o;
        //该接口将方法调用分派给指定的调用处理程序
        Object proxy = Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), this);
        return proxy;
    }

    /**
     * 
     * [代理对象调用方法]
     * 
     * @param proxy
     *            代理类对象
     * @param method
     *            代理对象方法
     * @param args
     *            方法参数
     * @return 方法返回参数
     *
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("正在寻找商品");
        
        // 调用被代理对象方法
        method.invoke(object, args);

        System.out.println("购买结束");
        return null;
    }

}

package designmodel.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 *  [针对类来实现代理的] 
 *  [对指定目标类产生一个子类,通过方法拦截技术拦截所有父类方法的调用]
 *  [jar : cglib-nodep]
 * @author baB_hyf
 * @version [版本号, 2019年10月4日]
 */
public class CglibProxy
{

    public static void main(String[] args) {
        MoveProxy proxy = new MoveProxy();
        Car t = (Car)proxy.getProxy(Train.class);
        t.move();
        System.out.println(System.getProperty("user.dir"));
    }

}

interface Car{
    void move();
}

class Train implements Car{

    @Override
    public void move() {
           System.out.println("火车正在行驶...");
    }
    
}

/**
 * 
 *  [代理行驶方法] 
 *  [MethodInterceptor拦截所有子类调用父类的方法，执行intercept方法]
 * @author baB_hyf
 * @version [版本号, 2019年10月4日]
 */
class MoveProxy implements MethodInterceptor{
    
    private Enhancer enhancer = new Enhancer();
    
    public Object getProxy(Class<?> clazz) {
        //设置创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        
        return enhancer.create();
    }

    /**
     * 拦截所有目标类方法的调用
     * arg0 目标类的实例 
     * arg1 目标方法的反射对象
     * arg2 方法的参数
     * arg3 代理类的实例
     */
    @Override
    public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
        
        System.out.println("开始启动...");
        
        //代理类调用父类的方法
        arg3.invokeSuper(arg0, arg2);
        
        System.out.println("到达终点站");
        
        return null;
    }
    
}

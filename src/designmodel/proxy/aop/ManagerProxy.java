package designmodel.proxy.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 
 *  [经纪人 类] 
 * @author baB_hyf
 * @version [版本号, 2019年10月6日]
 */
public class ManagerProxy implements InvocationHandler
{
    /**
     * 被代理对象
     */
    private Object handler;
    /**
     * 通知对象
     */
    private Advice advice;

    
    /**
     * 
     *  [创建代理对象] 
     *  @param clazz
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Object createProxy() {
        //创建代理对象
        return Proxy.newProxyInstance(handler.getClass().getClassLoader(),handler.getClass().getInterfaces(), this);
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
        
        //前置通知
        advice.beforeAdvice();
        
        //调用被代理对象的方法
        Object rtn = method.invoke(handler, args);
        
        //后置通知
        advice.afterAdvice();
        
        return rtn;
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }
    
    
}

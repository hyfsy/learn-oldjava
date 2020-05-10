package designmodel.proxy.aop;

import java.io.InputStream;

/**
 * [DIY 简易 AOP]
 *  properties操作、内省、简单工厂、动态代理模式
 * @author baB_hyf
 * @version [版本号, 2019年10月6日]
 */
public class Test
{
    
    public static void main(String[] args) throws Exception {
        //获取配置文件输入流
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("designmodel/proxy/aop/bean.properties");
        
        //通过配置文件获取工厂对象
        ManagerFactory factory = new ManagerFactory(in);
        
        //通过工厂创建对应经纪人对象
        ManagerProxy manager = (ManagerProxy)factory.createManger("ManagerProxy");
        
        //获取代理对象
        Singer proxy = (Singer)manager.createProxy();
        proxy.sing();

    }

}

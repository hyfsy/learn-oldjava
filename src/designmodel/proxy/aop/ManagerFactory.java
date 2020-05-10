package designmodel.proxy.aop;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

public class ManagerFactory
{

    /**
     * 配置文件数据
     */
    private Properties prop;

    /**
     * 初始化配置文件
     * 
     * @param in
     */
    public ManagerFactory(InputStream in) {
        if (prop == null) {
            prop = new Properties();
        }
        try {
            prop.load(in);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object createManger(String className) throws Exception {
        // 获取 获取代理对象的类对象
        String name = (String) prop.get("bean." + className.toLowerCase());
        if (name == null) {
            return null;
        }
        Class<?> managerClass = Class.forName(name);
        Object manager = managerClass.getConstructor().newInstance();
        // 获取具体唱歌实现
        Singer singer = (Singer) Class.forName((String) prop.get("bean.beethoven")).getConstructor().newInstance();
        // 获取具体通知实现
        Advice advice = (Advice) Class.forName((String) prop.get("bean.signeradvice")).getConstructor().newInstance();

        // 为经纪人填充数据
        BeanInfo beanInfo = Introspector.getBeanInfo(managerClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Method method = pd.getWriteMethod();
            switch (pd.getName()) {
                case "handler":
                {
                    method.invoke(manager, singer);
                    break;
                }
                case "advice":
                {
                    method.invoke(manager, advice);
                    break;
                }
            }
        }
        return manager;
    }

}

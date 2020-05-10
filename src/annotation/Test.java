package annotation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * 
 * [注解测试类]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月22日]
 */
public class Test
{
    public static void main(String[] args) throws Exception{
        setValueByAnnotation();
    }

    /**
     * 
     *  [通过注解装配对象] 
     *  @throws Exception    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void setValueByAnnotation() throws Exception {
        Bean uaBean = new Bean();
        Class<? extends Bean> clazz = uaBean.getClass();

        // 获取类中的注解对象
        MyAnnotation annotation = clazz.getAnnotation(MyAnnotation.class);

        // 验证该类中存在该注解
        if (annotation == null) {
            return;
        }

        String name = annotation.name();
        int age = annotation.age();
        String[] like = annotation.like();
        try {
            // 获取实体类的所有信息
            BeanInfo beanInfo = Introspector.getBeanInfo(Bean.class);
            // 获取所有属性描述
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            // 获取所有枚举实例
            BeanProperty[] values = BeanProperty.values();
            for (PropertyDescriptor pd : pds) {
                Method method = pd.getWriteMethod();
                switch (pd.getName()) {
                    case "name":
                    {
                        method.invoke(uaBean, name);
                        break;
                    }
                    case "age":
                    {
                        method.invoke(uaBean, age);
                        break;
                    }
                }

//                for (BeanProperty beanProperty : values) {
//                    if (beanProperty.getProperty().equals(pd.getName())) {
//                        //获取set方法
//                         Method method = pd.getWriteMethod();
//                         //设值
//                         method.invoke(uaBean,);
//                    }
//                }
            }
        }
        catch (IntrospectionException e) {
            e.printStackTrace();
        }
        System.out.println(uaBean);
    }

}


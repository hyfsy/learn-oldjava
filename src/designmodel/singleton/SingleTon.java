package designmodel.singleton;

import java.io.Serializable;

/**
 * 
 * [单例设计模式]
 * 1、保证线程安全
 * 2、volatile保证变量一致
 * 3、防止反射调用私有构造方法
 * 4、防止序列化+反序列化生成新对象
 * 5、防止克隆创建对象
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月4日]
 */
public class SingleTon implements Serializable, Cloneable
{

    private static final long serialVersionUID = -7347473076280687958L;

    private volatile static SingleTon singleTon;// 禁止指令重排 保证对象初始化 单一性

    private static boolean flag = true;

    /**
     * 其实最好的办法就是不要事先克隆接口
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return singleTon;// 防止克隆创建对象
    }

    private Object readResolve() {// 创建此方法防止序列化反序列化创建对象
        return singleTon;
    }

    private SingleTon() {
        if (flag) {// 依然双重检查锁
            synchronized (SingleTon.class) {
                if (flag) {
                    flag = false;// 防止反射创建对象
                }
            }
        }
        else {
            throw new RuntimeException("反射无效");
        }
    }

    public static SingleTon getInstance() {
        if (singleTon == null) {// 双重校验锁
            synchronized (SingleTon.class) {// 线程安全
                if (singleTon != null) {
                    singleTon = new SingleTon();// 懒加载
                }
            }
        }
        return singleTon;
    }

}

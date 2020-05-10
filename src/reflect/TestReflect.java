package reflect;

import java.lang.reflect.Array;

/**
 * Declared包括本身所有的，包括接口的，不包括继承的 无Declared包括所有公共的 [一句话功能简述]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月6日]
 */
public class TestReflect
{

    public static void main(String[] args) {

    }

//  复制数组 输出新数组中最大的数
    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> void copyArray(T[] a) {
//        T[] =new T[];//无效语句 编译不通过
        T[] o = (T[]) Array.newInstance(a.getClass().getComponentType(), a.length);// 利用反射,动态创建泛型数组
        System.arraycopy(a, 0, o, 0, a.length);// 复制数组
        boolean flag = true;
        T max = null;

        for (int i = 0; i < o.length; i++) {
            if (flag) {// 只初始化max一次
                max = o[i];
                flag = false;
            }
            if (max.compareTo(o[i]) < 0) {// 小于 值小于0
                max = o[i];
            }
        }
        System.out.println(max);
    }

}

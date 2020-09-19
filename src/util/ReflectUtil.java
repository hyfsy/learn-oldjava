package util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtil
{

    /**
     * 获取类中方法的返回值类型
     * 
     * @param clazz 指定类
     * @param methodName 指定类中存在的方法名称
     * @return 类中方法的返回值类型
     */
    public static Class<?> getReturnType(Class<?> clazz, String methodName, Class<?>... parameterType) {
        Class<?> argumentType = null;
        try {
            Method method = clazz.getMethod(methodName, parameterType);
            Type genericReturnType = method.getGenericReturnType();
            // 判断是否为参数化类型
            if (genericReturnType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                argumentType = (Class<?>) actualTypeArguments[0];
            }
            else {
                argumentType = (Class<?>) genericReturnType;
            }
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return argumentType;
    }
}

package test;

import util.ReflectUtil;

public class Test1 {

    public static void main(String[] args) {
        Class unicodeToString = ReflectUtil.getReturnType(ReflectUtil.class, "getReturnType", Class.class, String.class, Class[].class);
        System.out.println(unicodeToString);
    }
}

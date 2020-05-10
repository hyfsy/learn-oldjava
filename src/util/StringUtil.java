package util;

public class StringUtil
{

    /**
     * 判断对象为空
     * 
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        if (o instanceof String) {
            return o == null || "".equals(o.toString().trim()) || "null".equalsIgnoreCase(o.toString().trim());
        }
        else {
            return o == null;
        }
    }

    /**
     * 首字母大写
     */
    public static String upperCase(String str) {
        char[] chars = str.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char) (chars[0] - 32);
        }
        return chars.toString();
    }
}

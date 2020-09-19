package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UuidUtil {

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public static String generateUUID() {//得到主键uuid   时间+随机8位
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(now) + generateShortUUID();
    }

    private static String generateShortUUID() {//将uuid转换成8位字符
        StringBuilder stringBuilder = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            //parseInt : 将Sting类型的数转换成十进制的int类型，第二个参数为String类型的进制
            int x = Integer.parseInt(str, 16);
            stringBuilder.append(chars[x % 0x3E]);
        }
        return stringBuilder.toString();
    }

    public static String getUUID() {//得到普通uuid
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.substring(0, 8) +
                str.substring(9, 13) +
                str.substring(14, 18) +
                str.substring(19, 23) +
                str.substring(24);
    }

//    public static void main(String[] args) {
//        System.out.println("普通uuid: " + getUUID());
//        System.out.println("主键uuid: " + generateUUID());
//    }
}

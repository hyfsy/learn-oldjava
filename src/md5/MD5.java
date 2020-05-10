package md5;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class MD5
{

    private static String savePassword = "ISMvKXpXpadDiUoOSoAfww==";

    public static void main(String[] args) {

        String password = "admin";
        String md5Encode = md5Encode(password);
        if (savePassword.equals(md5Encode)) {
            System.out.println("成功");
        }
        else {
            System.out.println("失败");
        }

    }

    // 加密1.8后
    public static String md5Encode(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");// 获得消息摘要
            byte[] digest = md.digest(password.getBytes("utf-8"));// 使用md5加密 密码

            // a-z A-Z 0-9 / * base64编码算法
            // 1.8版本
            String saveString = Base64.getEncoder().encodeToString(digest);// 转换成base64编码算法

            byte[] decode = Base64.getDecoder().decode(saveString);// 解码

            return saveString;
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void md5Decode(String password) {

    }

    // 加密、解密1.8前
    // 设置BASE64（Encoder/Decoder） 两个类的访问权限
    // 项目 -> jar -> access rules -> rt.jar -> sun.misc
    public static void EnDe_Code(byte[] bytes) {// md5加密后的字节数组

        try {

            // 加密
            BASE64Encoder be = new BASE64Encoder();
            String encode = be.encode(bytes);
            System.out.println("1.8前加密后：" + encode);

            // 解密
            BASE64Decoder bd = new BASE64Decoder();
            byte[] decodeBuffer = bd.decodeBuffer(encode);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

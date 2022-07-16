package test;

import org.junit.Test;
import util.ReflectUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Test1 {

    public static void main(String[] args) {
        Class unicodeToString = ReflectUtil.getReturnType(ReflectUtil.class, "getReturnType", Class.class, String.class, Class[].class);
        System.out.println(unicodeToString);
    }

    @Test
    public void test3() throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        byte[] digest = md5.digest(new byte[]{});
    }

    @Test
    public void testDouble() {

        String s = Double.toHexString(16488.0);
        System.out.println(s);
        // double i = 0x4068d;
        // System.out.println(i);
    }

    @Test
    public void testIMD() throws IOException {

    }
}

package test.music;

import com.google.common.base.Strings;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

/**
 * @author baB_hyf
 * @date 2022/04/28
 */
public class TTT {

    @Test
    public void testBin() {
        // String s = "99 a3";
        // String s = "d9 26";
        String s = "91 95 a6 e5 a4 a7 e5 b8 88 a6";
        // String s = "d1 02 64 0a d9 2e";
        // String s = "c0 d9 24";
        final String[] s1 = s.split(" ");
        for (int i = 0; i < s1.length; i++) {
            s1[i] = "0x" + s1[i];
        }

        // final byte[] bytes = new byte[s1.length];
        // for (int i = 0; i < s1.length; i++) {
        //     bytes[i] = Byte.valueOf(s1[i], 16);
        // }
        final int[] bytes = new int[s1.length];
        for (int i = 0; i < s1.length; i++) {
            bytes[i] = Integer.decode(s1[i]);
        }

        System.out.println(Arrays.toString(bytes));
    }

    @Test
    public void testUUID() {
        final String s = UUID.randomUUID().toString();
        System.out.println(s.length());
        System.out.println(s);
        System.out.println(s.getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    public void testBase64() throws IOException {

        String base64 = "maMxNTjZJmZlaWh1YWx1b3llX21fNzk5NjRfNDI2NDcyMDBfY292ZXIucG5nkZWm5aSn5biIpk1hc3RlctECZArZLmZlaWh1YWx1b3llXzRrX2hkX21fNzk5NjVfNTE2Mjc2NTRfbXMuaW1kLmpzb27A2SQ1YTQwZmI2MS00NWYzLTQ1MDEtYTNkZS1iNmFmZTQ0ZGFjNDLZIGZlaWh1YWx1b3llX21fNzk5NjRfMjQ0OTA1MjIubXAzozIyMqMxMTGjMzMz";
        byte[] decode = Base64.getDecoder().decode(base64);
        File file = new File("C:\\Users\\baB_hyf\\Desktop\\base64.bin");
        file.createNewFile();

        try (ByteArrayInputStream bais = new ByteArrayInputStream(decode);
             FileOutputStream fos = new FileOutputStream(file)) {
            final byte[] buf = new byte[1024];
            int len;
            while ((len = bais.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
        }
    }
}

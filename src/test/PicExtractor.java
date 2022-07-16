package test;

import com.aspose.slides.p2cbca448.fos;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author baB_hyf
 * @date 2022/02/07
 */
public class PicExtractor {

    // 下载图片
    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\baB_hyf\\Desktop\\2020-10-27-ansi-output-bug.md";
        FileInputStream fis = new FileInputStream(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int len;
        while ((len = fis.read(buf)) > 0) {
            baos.write(buf, 0, len);
        }
        fis.close();
        String content = baos.toString();

        int count = 0;
        String storePath = "C:\\Users\\baB_hyf\\Desktop\\pic\\";

        Pattern pattern = Pattern.compile("!\\[.*?]\\((.*?)\\)");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            count++;
            String url = matcher.group(1);
            System.out.println(url);

            int idx = url.indexOf("?");
            if (idx != -1) {
                url = url.substring(0, idx);
            }

            String name = count < 10 ? "00" + count : count < 100 ? "0" + count : String.valueOf(count);
            String suffix = url.substring(url.lastIndexOf("."));

            InputStream is = getInputStream(url);
            FileOutputStream fos = new FileOutputStream(storePath + name + suffix);
            buf = new byte[2048];
            while ((len = is.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            is.close();
            fos.close();
        }
    }

    private static InputStream getInputStream(String u) throws Exception {
        URL url = new URL(u);
        return url.openStream();
    }

    // 替换图片链接
    public static void main2(String[] args) throws Exception {
        String filePath = "C:\\Users\\baB_hyf\\Desktop\\2020-10-27-ansi-output-bug.md";
        FileInputStream fis = new FileInputStream(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int len;
        while ((len = fis.read(buf)) > 0) {
            baos.write(buf, 0, len);
        }
        fis.close();
        String content = baos.toString();

        Pattern pattern = Pattern.compile("!\\[.*?]\\((.*?)\\)");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer(content.length());
        int count = 0;
        while (matcher.find()) {
            count++;
            String name = count < 10 ? "00" + count : count < 100 ? "0" + count : String.valueOf(count);
            String newUrl = "![](../images/2020/10/27/" + name + ".png)";
            matcher.appendReplacement(sb, newUrl);
        }
        matcher.appendTail(sb);

        String savePath = "C:\\Users\\baB_hyf\\Desktop\\test.md";
        FileWriter fw = new FileWriter(savePath);
        fw.write(sb.toString());
        fw.close();
    }
}

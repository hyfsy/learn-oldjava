package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

/**
 * 通过url 或 文件路径 获取json map 调用 iteratorJson2方法 添加jar包 fastjson
 * 
 * @see bug 单个数组获取内容不正确，如：["a"]
 * 
 * @author baB_hyf
 *
 */
public class JSONParseUtil
{
    // private static String link = "http://t.weather.sojson.com/api/weather/city/101191101";
    // private static String path = "C:\\Users\\baB_hyf\\Desktop\\json.txt";
    private static String time1 = "[\\d]{4}-[\\d]{2}-[\\d]{2}[\\d]{2}:[\\d]{2}:[\\d]{2}";
    private static String time2 = "[\\d]{2}:[\\d]{2}";
    private static Pattern patTime1 = Pattern.compile(time1);
    private static Pattern patTime2 = Pattern.compile(time2);

    // 文件路径获取json 字符串
    public static String getJsonFile(String path) {
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            StringBuffer sb = new StringBuffer();
            int len = 0;
            byte[] bytes = new byte[128];
            while ((len = fis.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len, "utf-8"));
            }
            fis.close();
            String string = sb.toString();
            String str = string.replace("\r\n", "").replace(" ", "").replace("null", "\"无\"");
            return str;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // url获取json字符串
    public static String getJsonUrl(String link) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(link);
            URLConnection openConnection = url.openConnection();
            InputStream inputStream = openConnection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int len = 0;
            byte[] bytes = new byte[128];
            while ((len = bis.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            bis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    // 正则匹配 v存在冒号
    private static boolean matchMaoHao(String test) {
        if (test.contains("\":\"")) {// 判断存在：但非时间
            return false;
        }
        Matcher matcher = patTime1.matcher(test);
        Matcher matcher2 = patTime2.matcher(test);
        if (!test.contains(":") || matcher.find() || matcher2.find()) {
            return true;
        }
        return false;
    }

    // 递归获取json数据 调用这个！！！
    public static Object iteratorJson2(String test) {

        if (!test.contains("[{")) {
            if (matchMaoHao(test)) {// test只剩v
                return null;
            }
            else {
                Map map = new HashMap();// 还有kv 创建新的map 放入继续递归获取的kv
                iteratorJson3(test, map);
                return map;// 返回最终得到的map对象
            }
        }
        else if (test.contains("[{")) {
            if (test.lastIndexOf("[{") == 0) {// v内部 不存在 无k对象数组
                String jsons = test.substring(1, test.lastIndexOf("]"));// 字符串去除[ ]
                jsons = jsons.replaceAll("},\\{", "}|{");// 将逗号分隔符变成|,方便分割操作(花括号中可能还有都还，会有问题)
                String[] split = jsons.split("\\|");// 分割成{...}，{...}，{...}

                Map tempMap = null;// map -> list -> map
                List list = new ArrayList();
                for (int i = 0; i < split.length; i++) {
                    tempMap = new HashMap<>();
                    iteratorJson3(split[i], tempMap);
                    list.add(tempMap);// list中放入一个个对象map
                }
                return list;// 将list放入调用方法之前的map
            }
            else {
                // 两个if有意义，第二个if的条件建立在第一个if的成功后执行操作的基础之上
                if (test.indexOf("[{") == 0) {// v内部存在无k对象数组
                    test = test.substring(1, test.lastIndexOf("]"));// 字符串去除[ ]
                    // 切割无k对象数组
                    if (test.substring(2).contains("[")) {
                        List<Integer> indexs = new ArrayList<>();// 所有右花括号的index后一位
                        int zhua = 0;// 定义右花括号的指针
                        indexs.add(0);// v的第一个花括号
                        // 当zhua每次变为0时，说明一个无k对象结束
                        for (int i = 0; i < test.length(); i++) {
                            if (test.charAt(i) == '{') {
                                zhua++;
                            }
                            if (test.charAt(i) == '}') {
                                zhua--;
                                if (zhua == 0) {
                                    try {
                                        indexs.add(i + 1);// v最后一个index可能越界
                                    }
                                    catch (Exception e) {
                                        indexs.add(i);
                                    }
                                }
                            }

                        }
                        // 切割存放string 长度为右花括号list-1 如4个index取出三个string
                        String[] jsono = new String[indexs.size() - 1];
                        // 第一个从0开始切割
                        jsono[0] = test.substring(indexs.get(0), indexs.get(1));

                        // 从第二个到倒数第二个
                        for (int i = 1; i < indexs.size() - 2; i++) {
                            // 去掉第一位的逗号
                            jsono[i] = test.substring(indexs.get(i) + 1, indexs.get(i + 1));
                        }
                        // 最后一个可能越界，单独切割
                        jsono[indexs.size() - 2] = test.substring(indexs.get(indexs.size() - 2) + 1);

                        Map tempMap = null;// map -> list -> map
                        List list = new ArrayList();

                        for (int i = 0; i < jsono.length; i++) {
                            tempMap = new HashMap<>();
                            iteratorJson3(jsono[i], tempMap);// 所有对象数组继续递归
                            list.add(tempMap);
                        }
                        return list;
                    }
                }
                Map map = new HashMap();// 还有kv 创建新的map 放入继续递归获取的kv
                iteratorJson3(test, map);
                return map;// 返回最终得到的map对象
            }
        }
        return null;
    }

    // 处理test成map
    private static void iteratorJson3(String test, Map map) {
        JSONObject jsonObj = JSONObject.parseObject(test);// 获取kv
        Set<String> keySet = jsonObj.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            Object value = jsonObj.get(key).toString();
            Object asdf = iteratorJson2(value.toString());// 获取到kv 递归判断是否v中还有kv
            if (asdf == null) {// 没有kv则放入map
                map.put(key, value);
            }
            else {// 否则map中放入kv生成的map
                map.put(key, asdf);
            }
        }
    }

}

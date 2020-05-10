package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * [序列化]
 *
 * @author baB_hyf
 * @version [版本号, 2019年9月8日]
 */
public class ObjectSerialize implements Serializable {
    //获取系统参数
    private static final Map<String, String> env;

    //获取桌面路径
    private static final String DESKTOP;

    static {
        env = System.getenv();
        StringBuilder builder = new StringBuilder();
        builder.append(env.get("USERNAME"));
        builder.append(File.separator);
        builder.append("Desktop");
        builder.append(File.separator);
        DESKTOP = builder.toString();
    }

    //获取系统用户名拼接文件路径
    private static String file = DESKTOP + "serialize.txt";

    /**
     * [序列化对象]
     */
    public static void serial(String file, Object o) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * [反序列化获取对象]
     */
    public static Object deserial(String file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

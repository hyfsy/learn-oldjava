package util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 机器相关工具类
 *
 * @author baB_hyf
 */
public class MachineUtil {

    /**
     * 获取IP地址
     *
     * @return 本机 IP 地址
     */
    public static String getLocalIP() {
        String ip = null;
        try {
            // 得到IP，输出DESKTOP-VOLGA13/122.206.73.83
            InetAddress ia = InetAddress.getLocalHost();
            ip = ia.toString().split("/")[1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println("IP: " + ip);
        return ip;
    }

    /**
     * 获取MAC地址
     *
     * @return 本机 MAC 地址
     */
    public static String getLocalMac() {
        try {
            return getLocalMac(InetAddress.getLocalHost());
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getLocalMac(InetAddress ia) throws SocketException {
        // 获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        // System.out.println("mac数组长度："+mac.length);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // 字节转换为整数 第一个数为负数时与第二个数做加法
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            // System.out.println("每8位:"+str);
            if (str.length() == 1) {
                sb.append("0").append(str);
            } else {
                sb.append(str);
            }
        }
        System.out.println("本机MAC地址: " + sb.toString().toUpperCase());
        return sb.toString().toUpperCase();
    }

}

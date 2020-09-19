package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Rili {

    private static final char[] title = {'日', '一', '二', '三', '四', '五', '六'};
    private static final String IN_Y_M = "请输入年份和月份：";
    private static final String GET_Y_M_ERROR = "请输入正确的年份和月份！！！";

    /**
     * 注意!!!
     * <p>
     * 1. 生成Calendar对象用 getInstance()
     * 2. 得到静态数据用 对象.get(Calendar.想要的静态数据)
     * 3. DAY_OF_WEEK 按照日到六的顺序，索引为1-7
     * 4. set(year,month,day)时间时，month-1
     * 5. 得到天数用 Calendar.DATE
     */
    private static void getCalendar(int year, int month) {//输出日历
        int[][] d = new int[6][7];//六行七列
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, 1);//month需要 -1
        int row = 0;
        do {
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {//判断当天是否为星期日
                if (d[0][6] != 0) {//如果第一天就是星期日 取消第一行的空行
                    row++;//进入下一周
                }
                d[row][0] = c.get(Calendar.DAY_OF_MONTH);//插入周日的日期

            } else {
                //插入周一到周六的日期
                d[row][c.get(Calendar.DAY_OF_WEEK) - 1] = c.get(Calendar.DAY_OF_MONTH);
            }
            c.add(Calendar.DATE, 1);//每月天数加一
        } while (c.get(Calendar.MONTH) != month);

        System.out.println("\n" + year + "年" + month + "月的日历：");
        for (int i = 0; i < title.length; i++) {//输出日历头
            System.out.print(title[i] + "\t");
        }
        System.out.println();

        for (int i = 0; i < d.length; i++) {//打印日历体
            for (int j = 0; j < d[1].length; j++) {
                if (d[i][j] <= 0) {
                    System.out.print("  \t");//当月数组中为0的用空白填充
                } else if (d[i][j] < 10) {
                    System.out.print("0" + d[i][j] + "\t");//日期小于10的用0补上
                } else {
                    System.out.print(d[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }


    public static void getCalendarController() {
        System.out.println(IN_Y_M);
        Scanner s1 = new Scanner(System.in);
        Scanner s2 = new Scanner(System.in);
        try {
            int year = s1.nextInt();
            int month = s2.nextInt();
            if (year <= 0 || month <= 0 || month > 12) throw new RuntimeException(GET_Y_M_ERROR);
            getCalendar(year, month);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        getCalendarController();//调用输出日历控制台
    }

    public void test() {
        Calendar instance = Calendar.getInstance();
        System.out.println(instance.getTime());
        System.out.println(Calendar.DATE);
        instance.add(Calendar.MONTH, 1);
        instance.set(123, 123, 123);//真正的month需要 -1
        Date time = instance.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(simpleDateFormat.format(time));
    }

}

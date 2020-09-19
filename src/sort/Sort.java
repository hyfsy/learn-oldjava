package sort;

import java.util.Arrays;
import java.util.Date;

public class Sort {

    public static void bubbleSort() {//冒泡排序
        int a[] = {49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35, 25, 53, 51};
        int temp = 0;
        //外围循环控制比较轮数
        for (int i = 0; i < a.length - 1; i++) {//比较长度等于数列长度-1
            for (int j = 0; j < a.length - 1 - i; j++) {//每轮减少比较的次数
                if (a[j] > a[j + 1]) {
                    temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
    }

    public static void simpleSelectSort() {//选择排序
        int a[] = {1, 54, 6, 3, 78, 34, 12, 45};
        for (int i = 0; i < a.length - 1; i++) {
            int position = i;//每轮假设一个最小下标
            for (int j = i + 1; j < a.length; j++) {
                if (a[position] > a[j]) {
                    position = j;
                }
            }
            if (position != i) {//判断要交换的数是否为自己
                a[position] = a[position] + a[i];
                a[i] = a[position] - a[i];
                a[position] = a[position] - a[i];
            }
        }
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
    }

    public static void directInsertSort() {//直接插入排序
        int a[] = {49, 38, 65, 97, 76, 13, 4, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35, 25, 53, 51};
        //比较控制的轮数
        for (int i = 1; i < a.length; i++) {
            int j = i - 1;
            int temp = a[i];
            for (; j >= 0 && temp < a[j]; j--) {
                a[j + 1] = a[j];                       //将大于temp的值整体后移一个单位
            }
            a[j + 1] = temp;
        }
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
    }

    public static void binarySearch() {//二分查找
        int a[] = {1, 3, 6, 12, 34, 54, 78};//必须保证有序
        int searchNum = 6;

        int start = 1;
        int last = a.length - 1;

        while (start <= last) {
            int middle = (last + start) / 2;
            if (searchNum < a[middle]) {
                last = middle - 1;
            } else if (searchNum > a[middle]) {
                start = middle + 1;
            } else {
                System.out.println("找到了");
                return;
            }
        }
        System.out.println("没找到");
    }

    public static void main(String[] args) {
        int a[] = {1, 3, 6, 12, 34, 54, 78};
        int b[] = Arrays.copyOfRange(a, 0, 1);
        System.out.println(Arrays.toString(b));
        System.out.println(new Date());
        //binarySearch();
    }

    public void ShellSort() {
        int a[] = {1, 54, 6, 3, 78, 34, 12, 45, 56, 100};
        double d1 = a.length;
        int temp = 0;
        while (true) {
            d1 = Math.ceil(d1 / 2);
            int d = (int) d1;
            for (int x = 0; x < d; x++) {
                for (int i = x + d; i < a.length; i += d) {
                    int j = i - d;
                    temp = a[i];
                    for (; j >= 0 && temp < a[j]; j -= d) {
                        a[j + d] = a[j];
                    }
                    a[j + d] = temp;
                }
            }
            if (d == 1)
                break;
        }
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
    }
}

package functional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 
 * Consumer 消费者接口 Function 接受一个参数并产生一个结果的函数 Supplier 代表结果供应商 predicate 断言接口
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月9日]
 */
public class FunctionalInterface
{
    public static void main(String[] args) {
        System.out.println("consumer:-------------------");
        consumer();
        System.out.println("function:-------------------");
        function();
        System.out.println("supplier:-------------------");
        supplier();
        System.out.println("predicate:------------------");
        predicate();

    }

    /**
     * 
     * [消费者接口]
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void consumer() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        // 1.8新特性 map也有
        list.forEach((Integer i) -> {System.out.println(i);});// 多个变量的写法
        list.forEach(i -> System.out.println(i));
        list.forEach(System.out::println);
    }

    /**
     * 
     * [接受参数并返回结果的接口]
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void function() {
        String str = functionRealize(234213123, (o) -> o.toString());
        System.out.println(str);
    }

    // 将任何类型转换为String类型
    private static String functionRealize(Object str, Function<Object, String> f) {
        // 返回结果
        return f.apply(str);
    }

    /**
     * 
     * [结果供应商接口]
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void supplier() {
        List<Integer> list = supplierRealize(10, () -> Math.random() * 100);
        list.forEach(System.out::println);
    }

    // 获取指定次数的随机数列表
    private static List<Integer> supplierRealize(int loop, Supplier<Double> in) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < loop; i++) {
            list.add((int) (double) in.get());
        }
        return list;
    }

    /**
     * 
     * [断言接口，做测试用]
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void predicate() {
        int i = 10;
        List<Integer> list = new ArrayList<>();
        // 获取十个随机数
        while (i > 0) {
            list.add((int) (Math.random() * 100));
            i--;
        }
        List<Boolean> boolList = predicateRealize(list, (num) -> num > 50);
        boolList.forEach(System.out::println);
    }

    // 测试整数是否大于50
    private static List<Boolean> predicateRealize(List<Integer> intList, Predicate<Integer> p) {
        List<Boolean> boolList = new ArrayList<>();
        for (Integer i : intList) {
            boolList.add(p.test(i));
        }
        return boolList;
    }

}

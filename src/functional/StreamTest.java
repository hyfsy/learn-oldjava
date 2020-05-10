package functional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 
 * Stream接口：不是数据结构，数据源可以是一个集合 惰式执行 只能被消费一次,一次后会关闭
 * 
 * 两种属性操作： 1、中间操作（生成一个Stream） 2、结束操作（执行一个计算操作）
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月10日]
 */
public class StreamTest
{
    public static void main(String[] args) {
        // 中间操作
        Stream<List<Integer>> stream = Stream.of(Arrays.asList(1, 2, 2), Arrays.asList(4, 5));
        // 合成list转换为Stream
        Stream<Integer> flatMap = stream.flatMap((list) -> list.stream());
        // 结束操作
        // flatMap.forEach(System.out::println);
        // 过滤
        // flatMap.filter((i) -> i > 3).forEach(System.out::println);
        // 去重
        // flatMap.distinct().forEach(System.out::println);
        // 映射
        // flatMap.map((i) -> i + 100).forEach(System.out::println);

        // 转换为list
//        List<Integer> list = flatMap.collect(Collectors.toList());

        //传入两个参数，得到操作后的结果
        Optional<Integer> option = flatMap.reduce((s1, s2) -> s1 > s2 ? s1 : s2);
        System.out.println(option.get());

    }

}

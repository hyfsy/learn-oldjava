package list;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * [1.8新版本的default方法]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月11日]
 */
public class NewMap
{

    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "张三1");
        map.put(2, "张三2");
        map.put(3, "张三3");

        // 有重复的key不会插入
        String putIfAbsent = map.putIfAbsent(3, "张三4");
        // put 操作会返回插入之前的key对应的value
        System.out.println("插入之前的key对应的值：" + putIfAbsent);

        // 查找指定key,没有返回默认值
        String orDefault = map.getOrDefault(4, "张三4");
        System.out.println("查找后的结果：" + orDefault);

        // 如何key和value不匹配 则不会改变值
        map.replace(3, "张三4", "张三测试");

        // 传入两个参数，得到的结果作为value的值
        // BiFunction
        // 新的值不为null,执行添加操作
        // 新的值为null key存在，执行删除操作
        map.compute(3, (key, value) -> key + "改变值成功");

        // 对应的key存在值才会执行操作
        map.computeIfPresent(5, (key, value) -> key + value + "asdfghjkl");

        // 传入一个参数，如果key不存在，得到的结果做为value
        // Function
        map.computeIfAbsent(4, (val) -> val + "测试用");
        
        //合并
        map.merge(1, "新的值", (oldVal,newVal)->oldVal+newVal);

        map.forEach((k, v) -> System.out.println(k + "->" + v));

    }

}

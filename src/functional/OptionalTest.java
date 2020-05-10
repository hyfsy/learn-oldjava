package functional;

import java.util.Optional;

/**
 * 
 * [容器类]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月11日]
 */
public class OptionalTest
{
    public static void main(String[] args) {

        // 生成一个Optional对象
        // 对象为null返回空Optional
        Optional<String> optional = Optional.ofNullable("测试");
        Optional.of("测试");
        Optional<Object> empty = Optional.empty();

        // 获取Optional的value
        System.out.println(optional.get());

        
        // 判断是否存在对象
        System.out.println(optional.isPresent());
        // 存在对象则执行操作
        optional.ifPresent((val) -> System.out.println("存在值：" + val));
      
        
        //没有值存在则返回默认值
        System.out.println(empty.orElse("没有值存在"));
        System.out.println(empty.orElseGet(() -> "供应商返回的默认值"));
        //无值抛异常
        //empty.orElseThrow(NullPointerException::new);
        
        
        //返回值为null则返回空的Optional
        Optional<String> map = empty.map((val) -> val + "添加后的值");
        //好像一样
        //必须返回Optional对象？
        Optional<String> flatMap = optional.flatMap((val)->Optional.of(val.toUpperCase()));
        
        
        //过滤value,不满足条件则返回空Optional
        Optional<String> filter = optional.filter((val)->val.length()>2);
        System.out.println(filter.isPresent());

        
    }
}

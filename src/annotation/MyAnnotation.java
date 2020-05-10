package annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Target 表明 该注解 可以出现的地方
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
// @Retention 注解生效时间 RUNTIME 在类运行状态都可以 获取该注解
@Retention(RetentionPolicy.RUNTIME)
// 使注解在javadoc中存在
@Documented
public @interface MyAnnotation
{
    // 定义变量
    String name();

    int age() default 4;// 设置默认值

    String[] like();// 数组变量
}

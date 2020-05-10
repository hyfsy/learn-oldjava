package annotation.dbl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 存放多注解的容器注解
@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DBLAnnotationContainer
{
    // 此处的成员值必须为value
    DBLAnnotation[] value();
}

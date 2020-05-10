package annotation.dbl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可重复的注解
 * 
 * @author baB_hyf
 *
 */
@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DBLAnnotationContainer.class)
public @interface DBLAnnotation
{
    String value() default "asdf";
}

package annotation;

/**
 * 
 * [测试自定义接口用 实体类]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月6日]
 */
@MyAnnotation(name = "测试", like = {"1", "2" })
public class Bean
{
    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Bean [name=" + name + ", age=" + age + "]";
    }
    
    
}

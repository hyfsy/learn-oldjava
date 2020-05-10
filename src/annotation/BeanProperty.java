package annotation;

/**
 * 
 * [属性枚举类]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月6日]
 */
enum BeanProperty implements Property
{

    // 后期增加属性只要再次添加即可
    NAME("name"), AGE("age"), LIKE("like");

    private String property = "";

    private BeanProperty(String property) {
        this.property = property;
    }

    // 获取属性字符串
    @Override
    public String getProperty() {
        return property;
    }
}
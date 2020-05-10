package xml.jdom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import xml.Person;

/**
 * JDOM解析 XML 
 * 1、与DOM类似基于树型结构 
 * 2、与DOM的区别: 
 *      (1)第三方开源的组件 
 *      (2)实现使用JAVA Collection接口
 *      (3)效事比DOM更快
 * 
 * @author baB_hyf
 */
public class JDomParser
{

    public static void main(String[] args) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();

        // 获取xml输入流
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("xml/person.xml");

        Document build = builder.build(in);
        // 获取根节点元素
        Element rootElement = build.getRootElement();

        // 获取所有为person的子节点
        List<Element> personList = rootElement.getChildren("person");
        List<Person> persons = new ArrayList<>();
        Person person = null;
        for (Element e : personList) {
            person = new Person();
            // 获取标签属性
            String name = e.getAttributeValue("name");
            person.setName(name);

            List<Element> children = e.getChildren();
            for (Element element : children) {
                // 获取标签名
                String elementName = element.getName();
                // 获取标签值
                String elementText = element.getText();

                if ("age".equals(elementName)) {
                    person.setAge(Integer.valueOf(elementText));
                }
                else if ("sex".equals(elementName)) {
                    person.setSex(elementText);
                }

            }
            persons.add(person);
        }
        System.out.println(Arrays.toString(persons.toArray()));
    }

}

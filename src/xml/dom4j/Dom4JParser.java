package xml.dom4j;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import xml.Person;

/**
 * DOM4J解析XML 
 * 1、基于树型结构，第三方组件”解析速度快，效事更高,使用的JAVA中的送代器
 * 2、实现数据读取，在WEB框架中使用较(Hibernate)
 * 
 * @author baB_hyf
 */
public class Dom4JParser
{

    public static void main(String[] args) throws DocumentException {
        SAXReader reader = new SAXReader();
        // 获取xml输入流
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("xml/person.xml");

        Document document = reader.read(in);

        Element rootElement = document.getRootElement();
        // 获取元素迭代器
        Iterator<Element> elementIterator = rootElement.elementIterator();
        List<Person> personList = new ArrayList<>();
        Person person = null;
        while (elementIterator.hasNext()) {
            person = new Person();
            Element element = elementIterator.next();
            //获取标签名
            String elementName = element.attribute("name").getText();
            person.setName(elementName);
            Iterator<Element> iterator = element.elementIterator();
            //遍历子迭代器
            while (iterator.hasNext()) {
                Element e = iterator.next();
                String name = e.getName();
                if ("age".equals(name)) {
                    person.setAge(Integer.valueOf(e.getText()));
                }
                else if ("sex".equals(name)) {
                    person.setSex(e.getText());
                }
            }
            personList.add(person);
        }
        personList.forEach(System.out::println);
    }

}

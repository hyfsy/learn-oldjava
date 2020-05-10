package xml.dom;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xml.Person;

/**
 * document解析xml文件
 * 
 * Document解析特点： 
 * 1、基于树型结构，通过解析器一次性把文档加截到内存中,所以会比较占用内存，可以随机访问 
 * 2、更加灵活，更适合在WEB开发中使用
 * 
 * @author baB_hyf
 */
public class DocumentParser
{

    public static void main(String[] args) throws Exception {
        // 创建工厂对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 获取解析器
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        // 获取xml输入流
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("xml/person.xml");

        // 将整个文档解析进内存中
        Document doc = documentBuilder.parse(in);

        NodeList personNodeList = doc.getElementsByTagName("person");

        List<Person> personList = new ArrayList<>();
        Person person = null;

        for (int i = 0, len = personNodeList.getLength(); i < len; i++) {
            person = new Person();
            Node item = personNodeList.item(i);
            // 获取属性值
            String name = item.getAttributes().getNamedItem("name").getNodeValue();
            person.setName(name);

            // 获取子节点
            NodeList childNodes = item.getChildNodes();
            for (int j = 0, length = childNodes.getLength(); j < length; j++) {
                Node childItem = childNodes.item(j);
                String nodeName = childItem.getNodeName();
                String value = "";
                if ("age".equals(nodeName)) {
                    // 标签中的值也算子节点，所有要获取第一个子节点
                    value = childItem.getFirstChild().getNodeValue();
                    person.setAge(Integer.valueOf(value));
                }
                else if ("sex".equals(nodeName)) {
                    value = childItem.getFirstChild().getNodeValue();
                    person.setSex(value);
                }
            }
            personList.add(person);
        }
        personList.forEach(System.out::println);

    }

}

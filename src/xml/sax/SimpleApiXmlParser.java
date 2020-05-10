package xml.sax;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import xml.Person;

/**
 * 
 * 解析 xml 主函数
 * 
 * SAX解析特点：
 * 1、基于事件驱动
 * 2、顺序加载，速度快
 * 3、不能任意读取节点，灵活性差
 * 4、解析时占用的内存小
 * 5、SAX更适用于在性能要求更高的设备上使用，比如 Android开发
 * 
 * @author baB_hyf
 */
public class SimpleApiXmlParser
{

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        // 获取sax解析器工厂对象
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        // 通过工厂创建SAX对象解析器
        SAXParser saxParser = saxParserFactory.newSAXParser();

        // 定义自己的xml处理器
        PersonHandler personHandler = new PersonHandler();

        // 获取xml的输入流
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("xml/person.xml");

        saxParser.parse(in, personHandler);

        List<Person> persons = personHandler.getPersons();
        persons.forEach(System.out::println);
    }
}

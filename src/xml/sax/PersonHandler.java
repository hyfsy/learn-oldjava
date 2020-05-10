package xml.sax;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import xml.Person;

/**
 * 自定义的xml解析器
 * 
 * @author baB_hyf
 */
public class PersonHandler extends DefaultHandler
{

    private List<Person> persons = null;

    private Person person = null;

    private String tag = null;

    /**
     * 开始解析文档
     */
    @Override
    public void startDocument() throws SAXException {
        System.out.println("开始解析文档......");
        persons = new ArrayList<>();
    }

    /**
     * 文档解析结束
     */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("解析结束！");
    }

    /**
     * 开始解析元素
     * 
     * @param uri
     *            命名空间
     * @param localName
     *            不带前缀的标签名
     * @param qName
     *            带前缀的标签名
     * @param attributes
     *            当前标签的属性集合
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        // 解析到person标签
        if ("person".equals(qName)) {
            person = new Person();
            String name = attributes.getValue("name");
            person.setName(name);
        }

        // 将当前标签名称赋给tag
        tag = qName;

    }

    /**
     * 元素解析结束 如：</person>
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        // person标签解析结束后，添加到list
        if ("person".equals(qName)) {
            persons.add(person);
        }

        // 将tag置为空，取消下次的空格解析
        tag = null;

    }

    /**
     * 解析标签中的文本内容
     * 
     * @param ch
     *            文本字节
     * @param start
     *            文本开始
     * @param length
     *            文本的长度
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        // 去除空格的判断
        if (tag != null) {
            if ("age".equals(tag)) {
                String string = new String(ch, start, length);
                Integer valueOf = Integer.valueOf(string);
                person.setAge(valueOf);
            }
            else if ("sex".equals(tag)) {
                person.setSex(new String(ch, start, length));
            }
        }
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

}

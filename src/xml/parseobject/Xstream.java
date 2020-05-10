package xml.parseobject;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Xpp3DomDriver;

import xml.Person;

/**
 * 将对象编码进xml，将对象从xml中解码
 * 
 * XMLEncoder
 * XMLDecoder
 * XStream
 * 
 * @author baB_hyf
 */
public class Xstream
{
    
    private static Person person;
    
    static{
        person = new Person();
        person.setName("张三");
        person.setAge(18);
        person.setSex("女");
    }

    public static void main(String[] args) {
       
        //编码单个对象
        xmlEncoder();
        //解码单个对象
        xmlDecoder();
        System.out.println();
        //编码单个对象（更快、方便）
        xstreamDemo();
    }
    
    public static void xstreamDemo(){
        XStream xstream = new XStream(new Xpp3DomDriver());
        //类名节点设置别名
        xstream.alias("person", Person.class);
        //设置属性节点
        xstream.useAttributeFor(Person.class, "name");
        
        //将对象转换为xml文件字符串(没有xml声明头)
        String xml = xstream.toXML(person);
        
        System.out.println(xml);
        
        //从xml文件(字符串)中返回对象
        Person p = (Person)xstream.fromXML(xml);
        System.out.println(p);
    }
    
    /**
     * jdk 对象生成xml文件
     */
    public static void xmlEncoder() {
        BufferedOutputStream bos;
        try {
            //获取文件输出流
            bos = new BufferedOutputStream(new FileOutputStream("bean.xml"));
            
            //获取xml编码器
            XMLEncoder encoder = new XMLEncoder(bos);
            
            //将对象写入文件
            encoder.writeObject(person);
            
            //关闭
            encoder.close();
            bos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
       
    }
    
    /**
     * jdk xml返回对象
     */
    public static void xmlDecoder() {
        try {
            //获取文件
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream("bean.xml"));
            
            //获取xml解码器
            XMLDecoder decoder = new XMLDecoder(bis);
            
            //从文件中读取出对象
            Person p = (Person)decoder.readObject();
            
            //关闭
            decoder.close();
            bis.close();
            
            System.out.println(p);
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

}

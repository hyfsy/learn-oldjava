package designmodel.other.prototype;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import designmodel.other.builder.House;
import designmodel.other.builder.HouseBuilderImpl;

public class HousePrototype implements Cloneable,Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 克隆使用的是  = 会克隆引用，String不会
     */
    public int i = 1;
    public String s = "2";
    public House house = new HouseBuilderImpl().createHouse();

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // 浅克隆
//        return super.clone();
        // 深克隆
        return deepClone();
    }

    /**
     * 深度克隆，原型内部的对象都要实现序列化（包括原型本身类）
     *  @return
     */
    public Object deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object readObject = ois.readObject();
            return readObject;
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

}

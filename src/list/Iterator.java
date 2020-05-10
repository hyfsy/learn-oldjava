package list;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

public class Iterator
{
    public static void main(String[] args){
        vectorEnumeration();
        System.out.println("-----------");
        listIterator();
    }
    
    /**
     * 
     *  [遍历vector]     
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void vectorEnumeration() {
        Vector<String> v=new Vector<>();
        v.add("1");
        v.add("2");
        Enumeration<String> elements = v.elements();
        while(elements.hasMoreElements()) {
            System.out.println(elements.nextElement());
        }
    }
    
    /**
     * 
     *  [古老的遍历list方法]     
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void listIterator() {
        List<String> list=new ArrayList<>();
        list.add("一");
        list.add("二");
        //jdk1.2 仅支持list,可前后双向遍历
        ListIterator<String> listIterator = list.listIterator();
        while(listIterator.hasNext()) {
            //会将光标下移
            System.out.println(listIterator.next());
        }
        while(listIterator.hasPrevious()) {
            listIterator.previous();
            //不会移动光标,只取下标值
           System.out.println(listIterator.previousIndex());
        }
    }
}

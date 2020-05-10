package list;

import java.util.TreeSet;

import object.TwoElephant;

public class TreeSetTest
{
    public static void main(String[] args) {
        //使用对象时,对象必须能进行比较
        TreeSet<TwoElephant> treeSet=new TreeSet<>();
        treeSet.add(new TwoElephant(1,"1"));
        //相同对象不会插入
        //treemap会把value插入，key不会插入
        treeSet.add(new TwoElephant(1,"2"));
        for (TwoElephant te : treeSet) {
            System.out.println(te);
        }
    }

 
}

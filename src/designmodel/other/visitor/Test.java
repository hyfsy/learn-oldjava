package designmodel.other.visitor;

/**
 * 访问者模式
 * 
 * 针对一个相对稳定的数据结构，访问者模式提供了一个基于该数据结构之上的的操作方法，使得访问该数据的模式可以基于OCP进行变化
 * 或者说，是针对不同的数据，提供可扩展的访问方案
 * 
 * 注意：访问者模式的使用条件是系统中要有一个稳定的数据结构，这样才能够保证OCP的执行
 * 
 * @author baB_hyf
 */
public class Test
{
    public static void main(String[] args) {
        // 创建账单及访问者对象
        Account a = new AccountA();
        Account b = new AccountB();
        Visitor accountant = new Accountant();
        Visitor boss = new Boss();

        // 不同的访问者对不同的对象执行不同的操作
        a.beView(accountant);
        a.beView(boss);
        System.out.println();

        b.beView(accountant);
        b.beView(boss);
    }

}

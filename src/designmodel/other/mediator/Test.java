package designmodel.other.mediator;

/**
 * 中介者模式
 * 
 * 一个中介对象，封装了一系列对象的交互，使得各个对象间的交互由紧密网络变成松散的中心耦合状态
 * 
 * @author baB_hyf
 */
public class Test
{

    public static void main(String[] args) {
        Computer familyComputer = new FamilyComputer();
        Computer superComputer = new SuperComputer();
        ComputerCenter center = new ComputerCenter();
        center.contact("将信息保存到服务器", familyComputer);
        center.contact("将信息展现给客户端看", superComputer);
    }

}

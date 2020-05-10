package designmodel.other.visitor;

/**
 * 具体访问者-会计师具体类
 * 
 * @author baB_hyf
 */
public class Accountant implements Visitor
{

    @Override
    public void browseAccount(Account account) {
        System.out.println("正在计算" + account.getAccountName() + "的收支情况...");
    }

}

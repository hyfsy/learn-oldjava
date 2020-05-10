package designmodel.other.visitor;

/**
 * 具体访问者-老板类
 * 
 * @author baB_hyf
 */
public class Boss implements Visitor
{

    @Override
    public void browseAccount(Account account) {
        System.out.println("看着" + account.getAccountName() + ",感觉又亏了一个亿");
    }

}

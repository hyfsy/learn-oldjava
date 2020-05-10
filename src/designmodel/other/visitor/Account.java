package designmodel.other.visitor;

/**
 * 被访问对象-账单
 * 
 * @author baB_hyf
 */
public abstract class Account
{
    protected String accountName;
    
    /**
     * 传入不同的访问者，执行不同的操作
     *  @param visitor
     */
    protected abstract void beView(Visitor visitor);

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    
}

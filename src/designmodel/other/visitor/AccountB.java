package designmodel.other.visitor;

public class AccountB extends Account
{
    
    public AccountB() {
        super.accountName = "账单B";
    }

    @Override
    protected void beView(Visitor visitor) {
        visitor.browseAccount(this);
    }

}

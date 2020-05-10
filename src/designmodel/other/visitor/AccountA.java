package designmodel.other.visitor;

public class AccountA extends Account
{
    
    public AccountA() {
        super.accountName = "账单A";
    }

    @Override
    protected void beView(Visitor visitor) {
        visitor.browseAccount(this);
    }

}

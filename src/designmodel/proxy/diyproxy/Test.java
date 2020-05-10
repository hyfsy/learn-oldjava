package designmodel.proxy.diyproxy;

public class Test
{

    public static void main(String[] args) throws Exception {
        Subject person = new Person();
        InvocationHandler handler = new ShopHandler(person);
        Subject proxy = (Subject) Proxy.newProxyInstance(Subject.class, handler);
        proxy.shopping();
    }

}

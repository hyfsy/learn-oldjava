package designmodel.template;

public class TestTemplate {//模板设计模式

	public static void main(String[] args) {
		String name="admin";
		String pwd="123456";
		String method="adds";
		AddExecute add = new AddExecute();
		add.action(name,pwd,method);
		
	}

}

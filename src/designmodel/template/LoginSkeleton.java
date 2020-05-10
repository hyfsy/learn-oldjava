package designmodel.template;

public abstract class LoginSkeleton {

	
	public void action(String name,String pwd,String method){//模板方法
		if ("admin".equals(name) && "123456".equals(pwd)) {
			afterLoginAction(method);
		} else {
			System.out.println("用户名或密码错误！");
		}
	}
	//子类重写此方法，执行各自操作
	public abstract void afterLoginAction(String method);
	
}

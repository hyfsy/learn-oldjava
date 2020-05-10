package designmodel.template;

public class AddExecute extends LoginSkeleton {

	@Override
	public void afterLoginAction(String method) {
		if("add".equals(method)) {
			System.out.println("执行添加操作");			
		}else if("del".equals(method)) {
			System.out.println("执行删除操作");	
		}else {
			System.out.println("您执行了其他未知的操作");
		}
	}

}

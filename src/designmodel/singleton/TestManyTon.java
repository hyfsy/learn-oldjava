package designmodel.singleton;

//多例模式的特点跟单例模式不同的是，在类中定义了有限个可实例化的对象个数；目的是提供对此类有多个访问入口，在多线程模式下可提高异步效率
class ManyTon {
	private String info;

	private static ManyTon m1 = new ManyTon("m1");// 多例
	private static ManyTon m2 = new ManyTon("m2");

	private ManyTon(String info) {
		this.info = info;
	}

	public static ManyTon getInstance(int choose) {
		switch (choose) {
		case Choose.ONE:
			return m1;
		case Choose.TWO:
			return m2;
		default:
			return null;
		}

	}
	
	public String getInfo() {
		return info;
	}
}

interface Choose {
	public int ONE = 1;
	public int TWO = 2;
}



public class TestManyTon{
	public static void main(String[] args){
		ManyTon m1 = ManyTon.getInstance(Choose.ONE);
		 System.out.println(m1.getInfo());
		 ManyTon m2 = ManyTon.getInstance(Choose.TWO);
		 System.out.println(m2.getInfo());
	}
}
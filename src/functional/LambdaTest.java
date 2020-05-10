package functional;

public class LambdaTest {
	public static void main(String[] args){
		//lambda用法：
		Lam lam1=(final String e)->{System.out.println("hello");return "";};
		lam1.eat("asdf");
		
		//有且只有返回值
		//Lam lam2=(e)->"";
		
		//final参数要写全
		//Lam lam3=(final String e)->{System.out.println(hello);};
	}
}


//接口内只能有一个方法才能使用lambda
//默认方法和静态方法不影响
interface Lam{
	String eat(final String e);
	default String drink() {
		System.out.println("drink");
		return "";
	}
}

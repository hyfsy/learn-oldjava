package designmodel.proxy;

public class StaticProxy {//代理模式
	public static void main(String[] args){
		SingerI singerI=new SingerImpl();
		BrokerProxy brokerProxy = new BrokerProxy(singerI);
	    brokerProxy.letSing();//调用代理对象调用被代理对象的方法
	}
}

//代理对象
class BrokerProxy{
	private SingerI singerI;
	
	public BrokerProxy(SingerI singerI) {
		this.singerI=singerI;
	}
	
	void letSing(){
		//do something
		System.out.println("start do something");
		singerI.sing();
		//do something
		System.out.println("end do something");
	}
}


interface SingerI{
	void sing();
}

//被代理对象
class SingerImpl implements SingerI{

	@Override
	public void sing() {
		System.out.println("唱歌");
	}
	
}
package designmodel.factory;

public class TestSimpleFactory {//简单工厂模式
	public static void main(String[] args){
		ProductFile file = FileFactory.getFile("xlsx");//获得对应对象
		if(null!=file) {
			file.product();
		}
	}
}

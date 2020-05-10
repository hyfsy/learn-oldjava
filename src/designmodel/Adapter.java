package designmodel;
//需求需要将A型号转换为B型号
public class Adapter {//适配器模式
	
	public static void main(String[] args) {
		XSSFCreateI xssfCreateI = new XSSFCreateImpl();
		produce(xssfCreateI);
		
		HSSFGenerateI hssfGenerateI=new HSSFGenerateImpl();
		HSSFGenerateAdapter hssfGenerateAdapter = new HSSFGenerateAdapter(hssfGenerateI);//通过适配器转换
		produce(hssfGenerateAdapter);
		
	}
	
	public static void produce(XSSFCreateI xssfCreateI){//只能A型号
		xssfCreateI.create();
	}
}

//适配器
class HSSFGenerateAdapter implements XSSFCreateI{//实现A型号
	private HSSFGenerateI hssfGenerateI;
	
	//初始化B型号
	public HSSFGenerateAdapter(HSSFGenerateI hssfGenerateI) {
		this.hssfGenerateI=hssfGenerateI;
	}

	@Override
	public void create() {
		hssfGenerateI.generate();//A型号对象调用B型号的方法
	}
}

//B型号
interface HSSFGenerateI{
	void generate();
}

class HSSFGenerateImpl implements HSSFGenerateI{
	@Override
	public void generate() {
		System.out.println("生成HSSF对象");	
	}
}


//A型号
interface XSSFCreateI{
	void create();
}

class XSSFCreateImpl implements XSSFCreateI{
	@Override
	public void create() {
		System.out.println("生成XSSF对象");	
	}
	
}

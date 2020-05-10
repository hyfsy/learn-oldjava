package designmodel.strategy;

public class XlsxSave implements StrategyI {

	@Override
	public void saveAs() {
		System.out.println("保存为xlsx文件");
	}

}

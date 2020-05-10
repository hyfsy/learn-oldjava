package designmodel.strategy;

public class XlsSave implements StrategyI {

	@Override
	public void saveAs() {
		System.out.println("保存为xls文件");
	}

}

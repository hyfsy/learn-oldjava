package designmodel.strategy;

public class TestStrategyI {//策略设计模式

	public static void main(String[] args) {
		
		BaseSave baseSave = new BaseSave();
		baseSave.setStrategyI(new XlsxSave());//传入不同的策略实现 得到不同的结果
		baseSave.save();
		
	}

}

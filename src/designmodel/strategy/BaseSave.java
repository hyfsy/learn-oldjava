package designmodel.strategy;

public class BaseSave {
	private StrategyI strategyI;

	public StrategyI getStrategyI() {
		return strategyI;
	}

	public void setStrategyI(StrategyI strategyI) {
		this.strategyI = strategyI;
	}
	
	public void save(){
		System.out.println("开始保存");
		strategyI.saveAs();
		System.out.println("保存成功");
	}
	
}

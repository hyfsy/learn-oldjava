package designmodel.other.mediator;

/**
 * 具体计算机
 * 
 * @author baB_hyf
 */
public class FamilyComputer extends Computer
{

    @Override
    public void contact(String message) {

        // 数据改变时不自己去处理相关的关联，而是交给中介的交互方法处理
        super.getCenter().contact(message, this);

    }

}

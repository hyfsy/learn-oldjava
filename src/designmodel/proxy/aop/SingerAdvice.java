package designmodel.proxy.aop;

/**
 * 
 *  [通知接口实现类] 
 * @author baB_hyf
 * @version [版本号, 2019年10月6日]
 */
public class SingerAdvice implements Advice
{

    @Override
    public void beforeAdvice() {
        System.out.println("请开始你的表演。。。");
    }

    @Override
    public void afterAdvice() {
        System.out.println("同学请坐下！");
    }

}

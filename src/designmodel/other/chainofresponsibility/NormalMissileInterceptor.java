package designmodel.other.chainofresponsibility;

/**
 * 子责任链，一条一条向下传递
 * 
 * @author baB_hyf
 */
public class NormalMissileInterceptor extends MissileInterceptor
{
    
    public NormalMissileInterceptor(MissileInterceptor interceptor) {
        super.nextInterceptor = interceptor;
    }

    @Override
    public void handleEvent(Missile missile) {
        if(missile.missileLevel<=1) {
            System.out.println("普通拦截器拦截成功！");
        }else {
            System.out.println("普通拦截器拦截失败...");
            nextInterceptor.handleEvent(missile);
        }
    }

}

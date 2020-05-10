package designmodel.other.chainofresponsibility;

/**
 * 子责任链，一条一条向下传递
 * 
 * @author baB_hyf
 */
public class UpdateMissileInterceptor extends MissileInterceptor
{
    
    public UpdateMissileInterceptor(MissileInterceptor interceptor) {
        super.nextInterceptor = interceptor;
    }

    @Override
    public void handleEvent(Missile missile) {
        if(missile.missileLevel<=3) {
            System.out.println("高级拦截器拦截成功！");
        }else {
            System.out.println("高级拦截器拦截失败...");
            System.out.println("导弹无法拦截.");
        }
        
    }

}

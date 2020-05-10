package designmodel.other.chainofresponsibility;

/**
 * 责任链抽象类
 * 
 * @author baB_hyf
 */
public abstract class MissileInterceptor
{
    protected MissileInterceptor nextInterceptor;
    
    public abstract void handleEvent(Missile missile);
}

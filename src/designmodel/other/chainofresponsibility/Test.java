package designmodel.other.chainofresponsibility;

/**
 * 责任链模式
 * 
 * 责任链中每一个对象必须存有下一个责任链的对象，并在handleEvent方法中对事件进行传递或截获
 * 
 * @author baB_hyf
 */
public class Test
{
    public static void main(String[] args) {
        MissileInterceptor updateInterceptor = new UpdateMissileInterceptor(null);
        //传递下一个责任链
        MissileInterceptor normalInterceptor = new NormalMissileInterceptor(updateInterceptor);
        Missile missile = new ConcreteMissile();
        //第一级链条开始处理
        normalInterceptor.handleEvent(missile);

    }

}

package designmodel.proxy.aop;

/**
 * 
 *  [唱歌实现类] 
 * @author baB_hyf
 * @version [版本号, 2019年10月6日]
 */
public class Beethoven implements Singer
{

    @Override
    public void sing() {
        System.out.println("贝多芬在唱歌");
    }

}

package socket.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 
 *  [客户端的消息处理器] 
 * @author baB_hyf
 * @version [版本号, 2019年9月28日]
 */
public class MinaClientHandler extends IoHandlerAdapter
{
  //会话开始
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("开始会话......");
    }
    
    //会话关闭
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("会话关闭");
    }
    
    //消息接收
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        //接收到的消息对象
        String msg = (String)message;
        System.out.println(msg);
    }
}

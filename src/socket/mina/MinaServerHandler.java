package socket.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 
 *  [服务器端的消息处理器] 
 * @author baB_hyf
 * @version [版本号, 2019年9月28日]
 */
public class MinaServerHandler extends IoHandlerAdapter
{
    //会话开始
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("开始会话："+session.getRemoteAddress());
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
        System.out.println("client: "+msg);
        session.write("echo: "+msg);
    }
}

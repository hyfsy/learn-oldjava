package util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailUtil
{

    /**
     * 创建内容 + 图片链接
     * 
     * @return
     */
    public static MimeBodyPart play() {
        MimeBodyPart bodyPart = null;
        // 5. 创建图片"节点"
        MimeBodyPart image = new MimeBodyPart();
        // 读取本地文件
        DataHandler dh = new DataHandler(new FileDataSource("C:\\Users\\baB_hyf\\Desktop\\1.jpg"));
        try {
            // 将图片数据添加到"节点"
            image.setDataHandler(dh);
            // 为"节点"设置一个唯一编号（在文本"节点"将引用该ID）
            image.setContentID("picture01");

            // 6. 创建文本"节点"
            MimeBodyPart text = new MimeBodyPart();
            // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
            text.setContent("这是一张图片<br/><a href='https://www.baidu.com'><img src='cid:picture01'/></a>", "text/html;charset=UTF-8");

            // 7. （文本+图片）设置 文本 和 图片"节点"的关系（将 文本 和 图片"节点"合成一个混合"节点"）
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(text);
            multipart.addBodyPart(image);
            multipart.setSubType("related"); // 关联关系

            bodyPart = new MimeBodyPart();
            bodyPart.setContent(multipart);
        }
        catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bodyPart;
    }

    /**
     * 发送QQ邮件
     * 
     * @param mail
     *            邮件对象
     * @return 发送成功/失败
     */
    public static boolean sendEmail(MailInfo mail) {

        if (!checkMail(mail)) {
            return false;
        }

        Session session = getMailSession(mail);

        // 通过会话对象和邮件对象获取MimeMessage对象
        MimeMessage message = getMimeMessage(session, mail);

        // 邮差发送邮件
        try {
            Transport.send(message);
            return true;
        }
        catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 通过基本邮件信息获取会话对象
     * 
     * @param mail
     *            邮件对象
     * @return Session对象
     */
    protected static Session getMailSession(MailInfo mail) {
        // 获取连接信息
        Properties prop = new Properties();
        prop.put("mail.transport.protocol", "smtp");// 连接协议（简单邮件传输协议）
        prop.put("mail.smtp.host", "smtp." + mail.getFrom().split("@")[1]);// 主机名,如：smtp.qq.com
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        prop.put("mail.smtp.port", 465);// 端口号
        prop.setProperty("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.auth", "true");// 需要经过授权
        prop.put("mail.smtp.ssl.enable", "true");// 使用SSL安全连接
        prop.put("mail.debug", "true");// 控制台显示debug信息

        // 创建会话对象
        Session session = Session.getInstance(prop, new Authenticator()
        {
            // 对应授权自己的邮件账户，密码为QQ邮箱开通的SMTP服务后得到的客户端授权码
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail.getFrom(), mail.getAuthorizationCode());
                // ojqwcsxgqwlzcjjj lacfykmrizapfdff
            }
        });
        return session;
    }

    /**
     * 生成配置好内容的MimeMessage对象
     * 
     * @param session
     *            会话对象
     * @param mail
     *            邮件对象
     * @return MimeMessage对象
     */
    protected static MimeMessage getMimeMessage(Session session, MailInfo mail) {
        // 通过会话对象获取邮件对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 设置发件人
            message.setFrom(new InternetAddress(mail.getFrom()));
            // 设置收件人 多个收件人方法加s,放入地址数组
            InternetAddress[] addresses = getAllToAddress(mail);
            // TO-发送 CC-抄送 BCC-密送
            message.setRecipients(RecipientType.TO, addresses);

            // 设置抄送人(不设置可能一些邮箱会认为是垃圾邮件)
            message.setRecipient(RecipientType.CC, new InternetAddress(mail.getFrom()));
            // 设置标题
            message.setSubject(mail.getSubject());

            // 设置内容
            MimeMultipart mulitipart = new MimeMultipart();
            MimeBodyPart content = new MimeBodyPart();
            // 设置非文本内容
            content.setContent(mail.getContent(), "text/html;charset=utf-8");// text/plain
            // content = play();
            mulitipart.addBodyPart(content);

            // 设置附件节点
            MimeBodyPart attachBodyPart = getAttachMultipart(mail);
            if (attachBodyPart != null) {
                mulitipart.addBodyPart(attachBodyPart);
            }

            message.setContent(mulitipart);
            // 设置发送时间
            message.setSentDate(new Date());
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 获取所有收件人的邮件地址
     * 
     * @param mail
     *            邮件对象
     * @return 地址数组
     */
    protected static InternetAddress[] getAllToAddress(MailInfo mail) {
        // 设置收件人 多个收件人方法加s,放入地址数组
        List<String> toList = mail.getTo();

        InternetAddress[] addresses = new InternetAddress[toList.size()];
        try {
            for (int i = 0; i < toList.size(); i++) {
                addresses[i] = new InternetAddress(toList.get(i));
            }
        }
        catch (AddressException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    /**
     * 获取所有附件节点
     * 
     * @param mail
     *            邮件对象
     * @return 附件节点对象
     */
    protected static MimeBodyPart getAttachMultipart(MailInfo mail) {
        List<String> attachPaths = mail.getAttachPaths();
        if (attachPaths == null) {
            return null;
        }
        MimeMultipart multipart = new MimeMultipart();
        MimeBodyPart bodyPart = new MimeBodyPart();
        MimeBodyPart attach = null;
        DataHandler dataHandler = null;
        try {
            for (String attachPath : attachPaths) {
                // 创建附件节点
                attach = new MimeBodyPart();
                // 读取本地文件
                dataHandler = new DataHandler(new FileDataSource(attachPath));
                attach.setDataHandler(dataHandler);
                // 设置附件的文件名（需要编码）
                attach.setFileName(MimeUtility.encodeText(dataHandler.getName()));

                // 添加多个附件
                multipart.addBodyPart(attach);
            }
            // 设置混合关系
            multipart.setSubType("mixed");
            // 转换为BodyPart对象
            bodyPart.setContent(multipart);
        }
        catch (MessagingException | UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return bodyPart;
    }

    /**
     * 检查邮件信息是否正确
     * 
     * @param mail
     *            邮件对象
     * @return
     */
    public static boolean checkMail(MailInfo mail) {
        return notNullOrEmpty(mail.getFrom()) && notNullOrEmpty(mail.getTo()) && mail.getTo().size() > 0 && notNullOrEmpty(mail.getAuthorizationCode()) && notNullOrEmpty(mail.getSubject()) && notNullOrEmpty(mail.getContent()) && notNullOrEmpty(mail.getAttachPaths());
    }

    private static boolean notNullOrEmpty(Object o) {
        if (o instanceof String) {
            return !(o == null || "".equals(o));
        }
        else if (o instanceof List) {
            return o != null && ((List<?>) o).size() >= 0;
        }
        else {
            return o != null;
        }
    }

    /**
     * 邮件对象
     * 
     * @author baB_hyf
     *
     */
    static class MailInfo
    {
        // 发送人邮箱
        private String from;
        // 收件人邮箱（多个）
        private List<String> to;
        // 登录授权码（对应授权自己的邮件账户，密码为QQ邮箱开通的SMTP服务后得到的客户端授权码）
        private String authorizationCode;
        // 标题
        private String subject;
        // 内容
        private String text;
        // 附件路径
        private List<String> attachPaths;
        // 邮箱后缀
        public static final String QQ_SUFFIX = "@qq.com";

        public MailInfo(String from, List<String> to, String authorizationCode, String subject, String text) {
            this.from = from;
            this.to = to;
            this.authorizationCode = authorizationCode;
            this.subject = subject;
            this.text = text;
        }

        public MailInfo(String from, String to, String authorizationCode, String subject, String text) {
            this.from = from;
            addTo(to);
            this.authorizationCode = authorizationCode;
            this.subject = subject;
            this.text = text;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public List<String> getTo() {
            return to;
        }

        public void addTo(String to) {
            if (this.to == null) {
                this.to = new ArrayList<>();
            }
            this.to.add(to);
        }

        public void setTo(List<String> to) {
            this.to = to;
        }

        public String getAuthorizationCode() {
            return authorizationCode;
        }

        public void setAuthorizationCode(String authorizationCode) {
            this.authorizationCode = authorizationCode;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getContent() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<String> getAttachPaths() {
            return attachPaths;
        }

        public void addAttach(String attachPath) {
            if (this.attachPaths == null) {
                this.attachPaths = new ArrayList<>();
            }
            this.attachPaths.add(attachPath);
        }

        public void setAttachPaths(List<String> attachPaths) {
            this.attachPaths = attachPaths;
        }

    }

}

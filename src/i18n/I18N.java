package i18n;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class I18N {

	public static void main(String[] args) {
		//定义一个本地语言
		Locale locale=new Locale("zh","CN");
		Locale locale2=new Locale("en","US");
		Locale default1 = Locale.getDefault();//默认系统语言
		
		//获取语言配置文件，包名+前缀  可增加语言选项参数
		ResourceBundle rb = ResourceBundle.getBundle("i18n.lan");
		System.out.println(rb.getString("system.welcome"));//根据key获取value值
		
		System.out.println(rb.getString("system.username"));
		Scanner s=new Scanner(System.in);
		String username = s.nextLine();
		
		System.out.println(rb.getString("system.pass"));
		Scanner s2=new Scanner(System.in);
		String pwd = s2.nextLine();
		
		
		if("admin".equals(username) && "hyf".equals(pwd)) {
			String success = rb.getString("system.loginsuccess");
			success = MessageFormat.format(success,",欢迎", username);//可有动态文本  属性文件要添加{0/1/2}
			System.out.println(success);
		}else {
			System.out.println(rb.getString("system.loginerror"));
		}
		
		
	}

}

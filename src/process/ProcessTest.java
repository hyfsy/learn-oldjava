package process;
import java.io.IOException;

import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;
import org.omg.SendingContext.RunTime;

public class ProcessTest {
	public static void main(String[] args) {
		ProcessBuilder processBuilder = new ProcessBuilder("start \"\" D:\\notepad++\\notepad++.exe");
		try {
			Process start = processBuilder.start();
			System.out.println(start);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("D:\\\\notepad++\\\\notepad++.exe");
			System.out.println(runtime.availableProcessors());//处理器
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

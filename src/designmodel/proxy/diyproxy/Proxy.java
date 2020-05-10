package designmodel.proxy.diyproxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;


//动态代理实现思路
//实现功能:通过Proxy的newProxyInstance返回代理对象
//1、声明一段源码(动态产生代理)
//2、编译源码( JDK Compiler API)，产生新的类(代理类)
//3、将这个类load到内存当中，产生一个新的对象(代理对象)
//4, return代理对象
public class Proxy
{
    
    /**
     * 
     *  [获取代理类对象]
     *  @param infce
     *  @return
     *  @throws Exception    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static Object newProxyInstance(Class<?> infce, InvocationHandler handler) throws Exception{
        String rn = "\r\n";
        Method[] methods = infce.getDeclaredMethods();
        String methodStr = "";
        
        //接口方法代码
        for(Method method : methods) {
            methodStr += 
                    rn +
                    "    @Override" + rn +
                    "    public void " + method.getName() + "() {" + rn +
                    "        try {" + rn +
                    "            Method md = " + infce.getName()+".class.getMethod(\"" + method.getName() + "\");" + rn +
                    "            handler.invoke(this, md);" + rn +                    
                    "        }" + rn + 
                    "        catch (Exception e) {" + rn +
                    "            e.printStackTrace();" + rn +
                    "        }" + rn +
                    "    }" + rn;
        }
        
        //代理类代码
        String str =
               "package designmodel.proxy.diyproxy;" + rn +
               rn +
               "import java.lang.reflect.Method;" + rn +
               "import designmodel.proxy.diyproxy.InvocationHandler;" + rn +
               rn +
               "public class $Proxy0 implements " + infce.getName() + rn +
               "{" + rn +
               rn +
               "    private InvocationHandler handler;" + rn +
               rn +
               "    public $Proxy0 (InvocationHandler handler) {" + rn +
               "        this.handler = handler;" + rn +
               "    }" + rn +
               
               methodStr + rn +

               "}" + rn;
        
        //Navigator可方便查看路径
        String fileName = System.getProperty("user.dir") + "/bin/designmodel/proxy/diyproxy/$Proxy0.java";
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(str.getBytes());
        fos.close();
        
        //编译文件
        javac(fileName);
        
        //获取系统类加载器
        //load到内存
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Class<?> clazz = cl.loadClass("designmodel.proxy.diyproxy.$Proxy0");
        Constructor<?> ctr = clazz.getConstructor(InvocationHandler.class);
        return ctr.newInstance(handler);
    }
    
    /**
     * 
     *  [编译文件] 
     *  @param fileName
     *  @throws IOException    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void javac(String fileName) throws IOException {
        //获取Java编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        
        //文件管理者
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        //获取文件
        Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjects(fileName);
        //获取编译任务
        CompilationTask task = compiler.getTask(null, fileManager, null, null, null, units);
        //执行编译任务
        task.call();
        //关闭文件管理者
        fileManager.close();
    }
    
}



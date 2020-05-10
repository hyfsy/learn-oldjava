package machine;

public class JniCall {
    static{
        System.loadLibrary("");//动态链接库的名称，不加后缀
    }

    public static native void sayBaBi();

    public static void main(String[] args) {
        sayBaBi();
    }
}

package machine;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class JnaCall {
    public static void main(String[] args) {
        CLibrary.INSTANCE.jnaCall();
    }

    public interface CLibrary extends Library {
        //两个字符串都表示dll文件
        CLibrary INSTANCE = Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"),
                CLibrary.class);

        void jnaCall();
    }

}
package test;

/**
 * @author baB_hyf
 * @date 2020/02/25
 */
public class StringTest {

    public static void main(String[] args){
        StringBuffer sb = new StringBuffer();
        sb.append("asdf");
        System.out.println(sb);

        StringBuilder sb2 = new StringBuilder();
        sb2.append("asdfdfdsasdf");
        System.out.println(sb2);
    }
}

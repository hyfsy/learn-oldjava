package test;

import org.junit.Test;
import util.PdfUtil;

import java.util.List;
import java.util.Map;

public class PdfTest {

    @Test
    public void testSplit() {
        String source = "C:\\Users\\baB_hyf\\Desktop\\asdf\\GG规范.pdf";
        String targetDir = "C:\\Users\\baB_hyf\\Desktop\\asdf\\split";
        String target = "C:\\Users\\baB_hyf\\Desktop\\asdf\\split\\test.pdf";

        List<Map<String, Object>> bookmark = PdfUtil.getBookmarkByResource(source);
        String createFilePath = PdfUtil.splitPdfByBookmarkMap(source, targetDir, bookmark);
        System.out.println(createFilePath);
    }
}

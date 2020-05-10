package play.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

/**
 * ???为未知的问题
 * 
 * @Link https://blog.csdn.net/weixin_34320724/article/details/92498454
 * @link https://blog.csdn.net/yamadeee/article/details/83384071#b_913
 * 
 * @author baB_hyf
 *
 */

public class PDFDemo
{

    // 操作文件路径
    private static String target = "C:\\Users\\baB_hyf\\Desktop\\1.pdf";

    // 生成文件路径
    private static String save = "C:/Users/baB_hyf/Desktop/10.pdf";

    // 背景图片路径
    private static String pic = "C:/Users/baB_hyf/Desktop/2.jpg";

    /**
     * 入门学习
     */
    public static void test() throws IOException, DocumentException {

        // 页面对象,自定义页面
        Rectangle rotate = PageSize.B6.rotate();
        // 设置背景颜色
        rotate.setBackgroundColor(BaseColor.ORANGE);

        // 将页面属性加载进文档对象
        Document document = new Document(rotate);
        FileOutputStream fos = new FileOutputStream(new File(target));
        // 获取单例对象会自动将document写入
        PdfWriter writer = PdfWriter.getInstance(document, fos);

        // 设置监听事件
        writer.setPageEvent(new PdfPageEventHelper());// 此处需要自定义事件
        // 设置页边距 左右上下
        document.setMargins(0, 0, 80, 20);

        document.open();
        document.add(new Paragraph("hellow woeo "));
        document.newPage();
        // 设置当前页为空页
        writer.setPageEmpty(false);
        document.newPage();
        document.add(new Paragraph("esfs"));

        // 顺序关闭
        document.close();
        fos.close();

    }

    /**
     * 增加水印、背景
     */
    public static void test2() throws IOException, DocumentException {

        PdfReader reader = new PdfReader(target);
        FileOutputStream fos = new FileOutputStream(new File(save));
        PdfStamper stamper = new PdfStamper(reader, fos);

        // 背景图片
        // 添加图片
        Image image = Image.getInstance(pic);
        image.setAbsolutePosition(200, 400);
        // 获取底部内容
        PdfContentByte underContent = stamper.getUnderContent(1);
        underContent.addImage(image);

        // 水印文字
        // 获取前面的内容
        PdfContentByte overContent = stamper.getOverContent(10);
        // 创建 可输入中文的 字体
        BaseFont createFont = BaseFont.createFont("C:/Windows/Fonts/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        overContent.beginText();
        // 字体颜色
        overContent.setColorFill(BaseColor.GREEN);
        // 字体和大小
        overContent.setFontAndSize(createFont, 50);
        // 设置起始位置
        overContent.setTextMatrix(30, 30);
        // 位置-文本-左-下-倾斜度
        overContent.showTextAligned(Element.ALIGN_CENTER, "asdfdsa水印asdfdsa", 245, 400, 30);
        overContent.endText();

        // 按顺序关闭
        stamper.close();
        reader.close();
        fos.close();
    }

    /**
     * 作用域对象
     * 
     * @块 Chunk对象: a String, a Font, and some attributes
     * @短语 Phrase对象: a List of Chunks with leading
     * @段落 Paragraph对象: a Phrase with extra properties and a newline
     * @列表 List对象: a sequence of Paragraphs called ListItem
     * 
     */
    public static void test3() throws IOException, DocumentException {

        Document document = new Document();
        FileOutputStream fos = new FileOutputStream(new File(save));
        PdfWriter.getInstance(document, fos);

        document.open();
        document.add(new Chunk("china"));
        document.add(new Chunk(" "));

        // 字体家族-字体大小-样式-颜色
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.ORANGE);
        // 使用对应字体样式写入文字
        Chunk chunk = new Chunk("chinese", font);
        // 设置块的背景颜色 颜色-左-下-右-上（背景突出的厚度）
        chunk.setBackground(BaseColor.BLACK, 1f, 0.5f, 1f, 1.5f);
        // 文字升起的高度
        chunk.setTextRise(7);
        // 文字下划线 线的宽度-与字体原本所在位置之间的距离
        chunk.setUnderline(0.2f, -2f);
        document.add(chunk);
        // ???无效
        document.add(Chunk.NEWLINE);
        document.add(new Chunk("asf"));

        // ------------------------------------------------------------------------------------
        document.newPage();

        Phrase phrase = new Phrase();
        phrase.add(new Chunk("china"));
        phrase.add(new Chunk(" "));
        phrase.add(new Chunk(","));
        phrase.add(new Chunk("japan"));
        phrase.add(Chunk.NEWLINE);
        // 设置与顶部的距离（同一行的对象，顶部距离以高的为准）
        phrase.setLeading(0);
        document.add(phrase);

        Phrase phrase1 = new Phrase();
        phrase1.add(new Chunk("china"));
        phrase1.add(new Chunk(" "));
        phrase1.add(new Chunk(","));
        phrase1.add(new Chunk("japan"));
        phrase1.setLeading(100);
        document.add(phrase1);

        // ------------------------------------------------------------------------------------
        document.newPage();

        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk("china"));
        paragraph.add(new Chunk(" chinese"));
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(new Chunk("japan"));
        paragraph.add(new Chunk(" japanese"));
        document.add(paragraph);

        // ------------------------------------------------------------------------------------
        document.newPage();

        /**
         * item用于存放list < - > list可输入文本，可添加item
         */

        // 初始化可设置是否排序，排序的前缀
        List list = new List(List.ORDERED);

        for (int i = 0; i < 1; i++) {
            // 初始化可设置项的名称和样式
            ListItem item = new ListItem(String.format("catalog %s", i + 1), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.GREEN));
            // 带字典序的list
            List moveList = new List(List.ORDERED, List.ALPHABETICAL);
            moveList.setLowercase(List.LOWERCASE);
            for (int j = 0; j < 5; j++) {
                ListItem moveItem = new ListItem("title" + (j + 1));
                // 不带前缀的list
                List director = new List(List.UNORDERED);
                for (int k = 0; k < 3; k++) {
                    director.add(String.format("%s, %s", "name1:" + (k + 1), "name2:" + (k + 1)));
                }
                moveItem.add(director);
                moveList.add(moveItem);
            }
            item.add(moveList);
            list.add(item);
        }

        document.add(list);

        document.close();

    }

    /**
     * 其他对象
     * 
     * @锚点 Anchor对象: internal and external links
     * @图片 Image对象
     * @目录 Chapter, Section对象（目录）
     * 
     */
    public static void test4() throws IOException, DocumentException {
        Document document = new Document();
        FileOutputStream fos = new FileOutputStream(new File(save));
        PdfWriter.getInstance(document, fos);

        document.open();
        // 定义锚点对象
        Anchor anchor = new Anchor("go to -> baidu", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED));
        // 设置此名称，其他锚点可以跳转到这里
        anchor.setName("CH");

        anchor.setReference("http://www.baidu.com");// 跳转到外部链接

        // 单独添加才能显示，不能放在段落等对象里再添加
        document.add(anchor);
        document.add(new Chunk("china"));
        document.add(new Chunk(" "));

        document.newPage();

        // ???
        Anchor anchor2 = new Anchor("go to -> #CH");
        anchor2.setReference("#CH");// 内部跳转
        document.add(anchor2);

        // ------------------------------------------------------------------------------------
        document.newPage();

        Image image = Image.getInstance(pic);
        // 位置
        image.setAlignment(Image.LEFT | Image.TEXTWRAP);
        // box类型为四边
        image.setBorder(Image.BOX);
        image.setBorderWidth(10);
        image.setBorderColor(BaseColor.PINK);
        // 图片 宽高 相差太大好像无效
        image.scaleToFit(300, 300);
        // 旋转角度(负为顺时针旋转)
        image.setRotationDegrees(-30);
        document.add(image);

        // ------------------------------------------------------------------------------------
        document.newPage();

        Paragraph title = new Paragraph("title");
        Chapter chapter = new Chapter(title, 2);// 标题和序号
        chapter.setBookmarkTitle("标题");
        // 设置下一级的缩进
        chapter.setIndentation(20);

        // 添加子目录
        Section section = chapter.addSection(new Paragraph("section A"), 3);
        // 书签名
        section.setBookmarkTitle("目录1");
        section.setIndentation(90);
        // 层级（如1.1.1.）手动设置，默认1
        section.setNumberDepth(2);
        // section的层级去最后的逗号，只作用用于当前目录
        section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);

        // section默认不另起一页，要手动设置
        section.setTriggerNewPage(true);

        Section subSection = section.addSection(new Paragraph("page1 page2"));
        subSection.setIndentationLeft(100);
        subSection.setNumberDepth(3);
        subSection.setBookmarkTitle("目录1目录2");// 目录支持中文

        document.add(chapter);

        document.close();

    }

    /**
     * 箭头-直线-点线-下划线
     * 
     * @throws IOException
     * @throws DocumentException
     */
    public static void test5() throws IOException, DocumentException {
        Document document = new Document();
        FileOutputStream fos = new FileOutputStream(new File(save));
        PdfWriter.getInstance(document, fos);

        document.open();

        // 绘制箭头
        document.add(new MyArrowMark());

        // 直线
        Paragraph paragraph = new Paragraph();
        paragraph.add("head");
        paragraph.add(new Chunk(new LineSeparator()));
        paragraph.add("foot");
        // 下划线
        // 线厚度-宽度比例-颜色-位置-偏移量
        LineSeparator lineSeparator = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
        paragraph.add(lineSeparator);
        document.add(paragraph);

        // 点线
        document.add(new Paragraph(new Chunk(new DottedLineSeparator())));

        document.close();
    }

    // @Test
    public void test6() {
        try {
            PdfReader reader = new PdfReader("C:/Users/baB_hyf/Desktop/1.pdf");
            // 获取选中页
            reader.selectPages("1,3,5,7,9,11-15");
            FileOutputStream fos = new FileOutputStream(new File(save));
            PdfStamper stamper = new PdfStamper(reader, fos);
            // 插入一页
            stamper.insertPage(5, reader.getPageSize(2));
            ColumnText ct = new ColumnText(null);
            ct.addElement(new Paragraph(24, new Chunk("INSERT PAGE")));
            ct.setCanvas(stamper.getOverContent(5));
            ct.setSimpleColumn(36, 36, 559, 770);
            stamper.close();
            reader.close();
            fos.close();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        // Document document = new Document(PageSize.LETTER);
        // PdfWriter writer = null;
        // try {
        // writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/baB_hyf/Desktop/1.pdf"));
        // document.open();
        //
        // document.close();
        //
        // }
        // catch (FileNotFoundException | DocumentException e) {
        // e.printStackTrace();
        // }
        catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

/**
 * 画箭头 (char)220 为箭头图案
 * 
 * @author baB_hyf
 *
 */
class MyArrowMark extends VerticalPositionMark
{
    @Override
    public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {

        canvas.beginText();
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont(BaseFont.ZAPFDINGBATS, "", BaseFont.EMBEDDED);
        }
        catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        // 设置字体样式和大小
        canvas.setFontAndSize(bf, 16);

        // LEFT
        canvas.showTextAligned(Element.ALIGN_CENTER, String.valueOf((char) 220), llx - 10, y, 0);
        // RIGHT
        canvas.showTextAligned(Element.ALIGN_CENTER, String.valueOf((char) 220), urx + 10, y + 8, 180);

        canvas.endText();

    }
}

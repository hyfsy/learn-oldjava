package play.pdf;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class InsidePDF
{

    /**
     * 反转键值对
     * 
     * @param map
     * @return
     */
    public static <K, V> Map<V, K> changeKV(Map<K, V> map) {
        Map<V, K> newMap = new HashMap<>();

        map.forEach((k, v) -> {
            newMap.put(v, k);
        });

        return newMap;
    }

    /**
     * 添加页眉文字
     * 
     * @param writer
     *            PDF写出流
     * @param headerText
     *            页眉文字
     */
    protected static void addHeader(PdfWriter writer, String headerText) {

        PdfPTable table = new PdfPTable(1);
        Header header = new Header(table);
        header.setTableHeader(writer, headerText);

    }

    /**
     * 添加页脚文字
     * 
     * @param writer
     *            PDF写出流
     * @param footerText
     *            页脚文字
     */
    protected static void addFooter(PdfWriter writer, String footerText) {

        PdfPTable table = new PdfPTable(1);
        Footer footer = new Footer(table);
        footer.setTableFooter(writer, footerText);

    }

    private static class Header extends PdfPageEventHelper
    {
        private PdfPTable header;

        public Header(PdfPTable header) {
            this.header = header;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            // 把页眉表格定位
            header.writeSelectedRows(0, -1, 38, 660, writer.getDirectContent());
        }

        /**
         * 设置页眉文字
         * 
         * @param writer
         * @param headText
         */
        public void setTableHeader(PdfWriter writer, String headText) {

            PdfPTable table = new PdfPTable(1);
            table.setTotalWidth(520f);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(0);
            Font font = getFont();
            Paragraph p = new Paragraph(headText, font);
            cell.setPaddingLeft(10f);
            cell.setPaddingTop(-2f);
            cell.addElement(p);
            table.addCell(cell);
            Header event = new Header(table);
            writer.setPageEvent(event);

        }

        public Font getFont() {
            String os = System.getProperties().getProperty("os.name");

            // 获取系统默认字体的路径
            String prefixFont = "";
            if (os.startsWith("win") || os.startsWith("Win")) {
                prefixFont = "C:/Windows/Fonts" + File.separator;
            }
            else {
                prefixFont = "/usr/share/fonts/chinese" + File.separator;
            }

            try {
                // 设置字体对象为Windows系统默认的字体，可输入中文的字体
                BaseFont baseFont = BaseFont.createFont(prefixFont + "simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                Font font = new Font(baseFont);
                font.setSize(9);
                return font;
            }
            catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }

    }

    private static class Footer extends PdfPageEventHelper
    {
        public PdfPTable footer;

        public Footer(PdfPTable footer) {
            this.footer = footer;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            // 把页脚表格定位
            footer.writeSelectedRows(0, -1, 38, 50, writer.getDirectContent());
        }

        /**
         * 设置页脚文字
         * 
         * @param writer
         * @param footText
         */
        public void setTableFooter(PdfWriter writer, String footText) {
            PdfPTable table = new PdfPTable(1);
            table.setTotalWidth(520f);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(1);
            Font font = getFont();
            Paragraph p = new Paragraph(footText, font);
            cell.setPaddingLeft(10f);
            cell.setPaddingTop(-2f);
            cell.addElement(p);
            table.addCell(cell);
            Footer event = new Footer(table);
            writer.setPageEvent(event);
        }

        public Font getFont() {
            String os = System.getProperties().getProperty("os.name");

            // 获取系统默认字体的路径
            String prefixFont = "";
            if (os.startsWith("win") || os.startsWith("Win")) {
                prefixFont = "C:/Windows/Fonts" + File.separator;
            }
            else {
                prefixFont = "/usr/share/fonts/chinese" + File.separator;
            }

            try {
                // 设置字体对象为Windows系统默认的字体，可输入中文的字体
                BaseFont baseFont = BaseFont.createFont(prefixFont + "simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                Font font = new Font(baseFont);
                font.setSize(9);
                return font;
            }
            catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }
    }

}

package util;

import com.aspose.words.SaveFormat;
import com.aspose.words.SaveOptions;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import net.sf.json.JSONObject;
import play.pdf.WordMailMergeUtil;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PDF文件操作工具类 仅供操作正版PDF文件
 * <p>
 * 要求PDF目录格式：
 * <p>
 * 1.1.1.1 标题.....页码 （序号和标题之间要有空格）
 * <p>
 * 工具依赖：
 * <ul>
 * <li>com.itextpdf.itextpdf</li>
 * <li>net.sf.json</li>
 * <li>com.aspose.words</li>
 * </ul>
 *
 * @author baB_hyf
 */
public class PdfUtil {

    /**
     * PDF后缀
     */
    public static final String PDF_SUFFIX = ".pdf";

    /**
     * word后缀
     */
    public static final String WORD_SUFFIX = ".doc";

    /**
     * PDF封面名称
     */
    public static final String PDF_COVER_NAME = "封面";

    /**
     * 默认生成的PDF文件名
     */
    public static final String DEFAULT_FILE_NAME = "新建PDF文件";

    /**
     * 临时文件夹名称
     */
    public static final String TEMP_DIR_NAME = "temp";

    /**
     * 匹配索引页数字是否合法
     */
    private static Pattern pattern = Pattern.compile("\\d+");

    /**
     * 通过封面模板生成封面PDF,生成在当前文件所在的文件夹下
     *
     * @param sourceFilePath 源文件路径
     * @param json           域合并json字符串
     * @return 保存文件的路径
     */
    public static String generateCover(String sourceFilePath, String json) {
        if (isEmpty(sourceFilePath)) {
            return null;
        }

        // 生成封面路径,将文件格式转为pdf格式
        String savePath = getDefaultPath(sourceFilePath) + PDF_COVER_NAME;
        SaveOptions saveOptions = null;
        if (sourceFilePath.endsWith(WORD_SUFFIX)) {
            savePath += WORD_SUFFIX;
            saveOptions = SaveOptions.createSaveOptions(SaveFormat.PDF);
        } else {
            savePath += PDF_SUFFIX;
        }

        // 生成合并后的文件
        new WordMailMergeUtil().mergeWordTemplate(sourceFilePath, savePath, JSONObject.fromObject(json), saveOptions);
        return savePath;
    }

    /**
     * 傻瓜式用方法：通过《文件的书签》获取《通过文件名列表 动态拼接好的》新PDF文件，默认在源文件路径下生成新文件
     *
     * @param sourceFilePath 源文件路径
     * @param fileNameList   文件名列表
     * @param createFileName 生成的文件名
     * @return 保存文件的路径
     */
    public static String createPDF(String sourceFilePath, List<String> fileNameList, String createFileName) {
        return createPDF(sourceFilePath, 0, 0, fileNameList, createFileName);
    }

    /**
     * 傻瓜式用方法：通过《文件的目录》获取《通过文件名列表 动态拼接好的》新PDF文件，默认在源文件路径下生成新文件
     *
     * @param sourceFilePath 源文件路径
     * @param fileNameList   文件名列表
     * @param createFileName 生成的文件名
     * @return 保存文件的路径
     */
    public static String createPDF(String sourceFilePath, int startPage, int endPage, List<String> fileNameList, String createFileName) {

        if (isEmpty(sourceFilePath) || fileNameList == null || fileNameList.size() <= 0 || isEmpty(createFileName)) {
            return null;
        }

        // 获取临时资源路径
        String savePath = getTempPath(sourceFilePath);
        createFileOrDir(savePath);

        List<Map<String, Object>> bookmark;
        if (endPage == 0) {
            bookmark = getBookmarkByResource(sourceFilePath);
        } else {
            bookmark = getBookmarkByCatalog(sourceFilePath, startPage, endPage);
        }

        Map<Integer, String> bookmarkMap = getBookmarkMapByDepth(bookmark, 0, true);

        // 通过目录分割文件
        splitPdfByBookmarkMap(sourceFilePath, savePath, bookmarkMap);

        // 通过文件名称合并文件
        mergePDF(savePath, fileNameList, getDefaultPath(sourceFilePath) + createFileName + PDF_SUFFIX, true, true);
        // 删除分割后的临时资源
        // deleteTempFile(new File(savePath));

        return savePath;
    }

    /**
     * 从源PDF文件中切割出一个单独的PDF文件
     *
     * @param sourceFilePath 源文件路径
     * @param createFilePath 保存文件路径
     * @param startPage      切割开始页数
     * @param endPage        切割结束页数（包括）
     * @param bookmark       目录列表（用于添加书签）
     * @return 保存文件的路径
     */
    public static String splitPDF(String sourceFilePath, String createFilePath, int startPage, int endPage, List<Map<String, Object>> bookmark) {
        if (isEmpty(sourceFilePath)) {
            return null;
        }

        // 没有 保存路径 默认为 当前切割的pdf 所在的路径
        if (isEmpty(createFilePath)) {
            createFilePath = getDefaultPath(sourceFilePath) + DEFAULT_FILE_NAME + PDF_SUFFIX;
        }
        File file = createFileOrDir(createFilePath);
        if (file == null) {
            return null;
        }

        Document document = null;
        PdfReader reader = null;
        PdfWriter writer = null;
        // 切割文件输出流
        FileOutputStream fos = null;

        try {
            reader = new PdfReader(sourceFilePath);

            // 获取总页数
            int totalPage = reader.getNumberOfPages();
            // 默认开始页数
            if (startPage < 1 || startPage > totalPage || startPage > endPage) {
                startPage = 1;
            }
            // 默认结束页数
            if (endPage < 1 || endPage > totalPage) {
                endPage = totalPage;
            }

            document = new Document(reader.getPageSize(2));
            fos = new FileOutputStream(file);
            writer = PdfWriter.getInstance(document, fos);

            // 设置打开pdf文件时显示书签
            writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);

            // 开启编辑模式
            // 要在writer连接document后才能开启
            document.open();

            for (int i = startPage; i <= endPage; i++) {

                // 复制pdf新增一空页
                document.newPage();

                // 通过源pdf文件获取指定页数
                PdfImportedPage importedPage = writer.getImportedPage(reader, i);
                writer.getDirectContentUnder().addTemplate(importedPage, 0, 0);

            }
            // 添加书签
            addBookmark(writer, bookmark);

            return file.getPath();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭编辑模式
            closeDocument(document, reader, writer, fos);
        }
        return null;
    }

    /**
     * 通过PDF源文件目录动态切割成多个子PDF文件
     *
     * @param sourceFilePath 源文件路径
     * @param savePath       保存路径（文件夹）
     * @param bookmark       书签对象列表
     * @return 保存文件的路径
     */
    public static String splitPdfByBookmarkMap(String sourceFilePath, String savePath, List<Map<String, Object>> bookmark) {
        Map<Integer, String> bookmarkMap = getBookmarkMapByDepth(bookmark, 0, true);
        return splitPdfByBookmarkMap(sourceFilePath, savePath, bookmarkMap);
    }

    /**
     * 通过PDF源文件目录动态切割成多个子PDF文件
     *
     * @param sourceFilePath 源文件路径
     * @param savePath       保存路径（文件夹）
     * @param bookmarkMap    目录map
     * @return 保存文件的路径
     */
    public static String splitPdfByBookmarkMap(String sourceFilePath, String savePath, Map<Integer, String> bookmarkMap) {

        if (isEmpty(sourceFilePath)) {
            return null;
        }

        if (bookmarkMap == null || bookmarkMap.size() <= 0) {
            return null;
        }

        // 获取默认输出文件路径
        if (isEmpty(savePath)) {
            savePath = getTempPath(sourceFilePath);
        }
        if (!savePath.endsWith(File.separator)) {
            savePath += File.separator;
        }

        // 创建保存目录
        createFileOrDir(savePath);

        PdfReader reader;
        try {
            reader = new PdfReader(sourceFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        TreeMap<Integer, String> lowBookmark = new TreeMap<>(bookmarkMap);
        // 获取降序目录map
        NavigableMap<Integer, String> descBookmarkMap = lowBookmark.descendingMap();

        int endPage = reader.getNumberOfPages();
        //关闭流
        reader.close();

        // 通过目录开始循环切割文件
        for (Entry<Integer, String> entry : descBookmarkMap.entrySet()) {

            // 目录名_目录名 形式的文件名称
            String fileName = entry.getValue().trim();
            splitPDF(sourceFilePath, savePath + fileName + PDF_SUFFIX, entry.getKey(), endPage, null);
            endPage = entry.getKey() - 1;
        }
        // 第一个目录的索引不是第一页
        if (endPage != 0) {
            splitPDF(sourceFilePath, savePath + PDF_COVER_NAME + PDF_SUFFIX, 0, endPage, null);
        }
        return savePath;

    }

    /**
     * 合并PDF文件，合并后的文件默认存放在和源文件夹同级的目录下，默认带页码
     *
     * @param sourcesPath 源文件夹路径
     * @param titleList   合并的文件名集合 (生成的书签按照list顺序显示)
     * @param fileName    生成的新文件名称
     * @return 保存文件的路径
     * @see PdfUtil#evolveTitle(List) 方法优化切割及合并
     */
    public static String mergePDF(String sourcesPath, List<String> titleList, String fileName) {
        // 默认保存路径
        String saveFilePath = getDefaultPath(sourcesPath) + fileName + PDF_SUFFIX;
        return mergePDF(sourcesPath, titleList, saveFilePath, true, true);
    }

    /**
     * 合并PDF文件
     *
     * @param sourcesPath    合并文件的文件夹路径
     * @param titleList      合并的文件名集合
     * @param saveFilePath   生成的文件路径（文件全路径）
     * @param withPagination 是否生成页码
     * @param withBookmark   是否生成书签
     * @return 保存文件的路径
     * @see PdfUtil#evolveTitle(List) 方法优化切割及合并
     */
    public static String mergePDF(String sourcesPath, List<String> titleList, String saveFilePath, boolean withPagination, boolean withBookmark) {

        if (isEmpty(sourcesPath) || titleList == null || titleList.size() <= 0 || isEmpty(saveFilePath)) {
            return null;
        }

        if (!sourcesPath.endsWith(File.separator)) {
            sourcesPath += File.separator;
        }

        // 创建保存文件
        File file = createFileOrDir(saveFilePath);

        if (file == null) {
            return null;
        }

        String targetPath;
        Document document = null;
        PdfReader reader = null;
        FileOutputStream fos = null;
        PdfWriter writer = null;

        // 用于获取源文件的长宽模板 与之匹配
        for (String fileName : titleList) {
            try {
                Rectangle rectangle = getRectangleByFilePath(sourcesPath + fileName + PDF_SUFFIX);
                if (rectangle != null) {
                    document = new Document(rectangle);
                    break;
                }
            } catch (IOException e) {
                // 模板文件不能找到,什么也不操作
            }
        }

        // 没有文件存在直接返回
        if (document == null) {
            return null;
        }

        try {
            fos = new FileOutputStream(file);
            // 获取保存文件的输出流
            writer = PdfWriter.getInstance(document, fos);

            // 设置打开pdf文件时显示书签
            writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
            writer.setCloseStream(false);

            if (withPagination) {
                // 设置页码
                addPagination(writer);
            }

            document.open();

            // 书签跳转的索引数
            int index = 1;
            Map<Integer, String> bookmarkMap = new HashMap<>();
            // 遍历每个文件
            for (String fileName : titleList) {

                targetPath = sourcesPath + fileName + PDF_SUFFIX;

                try {
                    // 该文件不存在则跳过
                    reader = new PdfReader(targetPath);
                } catch (Exception e) {
                    continue;
                }

                // 获取总页数
                int pageNumber = reader.getNumberOfPages();

                // 遍历文件的每页
                for (int i = 1; i <= pageNumber; i++) {

                    // 生成一页新页并写入
                    document.newPage();
                    PdfImportedPage importedPage = writer.getImportedPage(reader, i);
                    writer.getDirectContentUnder().addTemplate(importedPage, 0, 0);
                    System.out.println(reader + "===" + importedPage + "===" + writer);
                }

                // 这边每次操作完要关闭连接,才能在该方法中删除文件,否则因为文件占用无法删除(未解决的问题)
                // reader.close();

                // 添加目录map
                bookmarkMap.put(index, fileName);
                index = index + pageNumber;
            }

            if (withBookmark) {
                // 添加书签
                List<Map<String, Object>> bookmark = change2StandardBookmark(bookmarkMap);
                addBookmark(writer, bookmark);
            }
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeDocument(document, reader, writer, fos);
        }
        return saveFilePath;

    }

    /**
     * 生成一份增加水印的新文件
     *
     * @param sourceFilePath 源文件路径
     * @param markName       水印文本
     * @param xpercent       x轴的比例因子
     * @param ypercent       y轴的比例因子
     * @param rotation       旋转角度
     * @return 保存文件的路径
     */
    public static String addWaterMark(String sourceFilePath, String markName, float xpercent, float ypercent, float rotation) {

        if (isEmpty(sourceFilePath) || isEmpty(markName)) {
            return null;
        }

        Document document = null;
        PdfReader reader = null;
        FileOutputStream fos = null;
        PdfStamper stamper = null;
        PdfContentByte over;
        BaseFont font;
        try {
            Rectangle rectangle = getRectangleByFilePath(sourceFilePath);
            if (rectangle == null) {
                return null;
            }
            document = new Document(rectangle);
            reader = new PdfReader(sourceFilePath);
            String savePath = getDefaultPath(sourceFilePath) + DEFAULT_FILE_NAME + PDF_SUFFIX;
            File file = createFileOrDir(savePath);
            if (file == null) {
                return null;
            }
            fos = new FileOutputStream(file);
            stamper = new PdfStamper(reader, fos);
            int pageNumber = reader.getNumberOfPages();

            // 创建 可输入中文的 字体
            String fontPath = getDefaultFontPath();
            font = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            // 获取文字的实际x,y
            float width = rectangle.getWidth() * xpercent;
            float height = rectangle.getHeight() - rectangle.getHeight() * ypercent;

            // 文字透明
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.3f);
            gs.setStrokeOpacity(0.4f);
            document.open();

            // 遍历所有pdf页
            for (int i = 0; i < pageNumber; i++) {

                over = stamper.getOverContent(i + 1);
                // 设置文字属性
                over.saveState();
                over.setGState(gs);
                over.beginText();
                // 设置颜色
                over.setColorFill(BaseColor.GRAY);
                // 字体和大小
                over.setFontAndSize(font, 100);
                // 设置起始位置
                over.setTextMatrix(30, 30);
                // 写入水印文字
                over.showTextAligned(Element.ALIGN_LEFT, markName, width, height, rotation);
                over.endText();
            }
            return savePath;
        } catch (IOException | DocumentException e) {
            return null;
        } finally {
            closeDocument(document, stamper, reader, null, fos);
        }

    }

    /**
     * 将单层map数据转换为标准的目录列表（需要map的value名称包含结构 + “_” ）
     *
     * @param bookmarkMap 书签map（k:页数,value:titlename with structure）
     * @return 带结构的目录列表
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> change2StandardBookmark(Map<Integer, String> bookmarkMap) {

        if (bookmarkMap == null || bookmarkMap.size() <= 0) {
            return null;
        }

        // 转为有序map
        bookmarkMap = new TreeMap<>(bookmarkMap);

        List<Map<String, Object>> bookmark = new ArrayList<>();

        // 子目录列表
        List<Map<String, Object>> child;
        // 单个子目录
        Map<String, Object> titleNode;

        // 判断当前节点的列表
        List<Map<String, Object>> childBookmark;
        // 判断是否存在当前目录节点
        boolean hasNode = false;

        for (Entry<Integer, String> entry : bookmarkMap.entrySet()) {
            // 获取当前目录的每个层级
            String[] titles = entry.getValue().split("_");

            // 生成所有标题节点
            child = new ArrayList<>();
            for (int i = 0, len = titles.length; i < len; i++) {
                titleNode = new HashMap<>();
                titleNode.put("Title", titles[i]);
                titleNode.put("Page", entry.getKey());
                titleNode.put("hierarchy", i + 1);
                child.add(titleNode);
            }

            // 每条目录重新获取bookmark
            childBookmark = bookmark;
            for (Map<String, Object> map : child) {
                for (Map<String, Object> book : childBookmark) {
                    // 判断当前节点是否已经存在
                    if (map.get("Title").equals(book.get("Title"))) {
                        List<Map<String, Object>> kids = (List<Map<String, Object>>) book.get("Kids");
                        if (kids == null) {
                            kids = new ArrayList<>();
                            book.put("Kids", kids);
                        }

                        // 当前节点已存在,获取当前节点的子节点继续判断
                        childBookmark = kids;
                        hasNode = true;
                        break;
                    }
                }

                // 目录中已存在当前目录节点
                if (hasNode) {
                    hasNode = false;
                    continue;
                }

                // 添加目录节点
                childBookmark.add(map);

                // 最后一个目录层级不添加子节点
                if (map == child.get(child.size() - 1)) {
                    continue;
                }

                // 添加子节点
                List<Map<String, Object>> kids = (List<Map<String, Object>>) map.get("Kids");
                if (kids == null) {
                    kids = new ArrayList<>();
                    map.put("Kids", kids);
                }

                // 当前节点已存在,获取当前节点的子节点继续判断
                childBookmark = kids;
            }
        }
        return bookmark;
    }

    /**
     * 返回源文件的书签map(页索引，标题名)
     *
     * @param bookmark   书签列表
     * @param depth      目录深度 0 默认获取所有子目录，不包括父目录
     * @param withParent 是否包含父辈目录
     * @return 书签map(索引 ， 标题名)
     */
    public static Map<Integer, String> getBookmarkMapByDepth(List<Map<String, Object>> bookmark, int depth, boolean withParent) {
        if (bookmark == null || bookmark.size() <= 0) {
            return null;
        }

        return getBookmarkMapByDepth(bookmark, depth, withParent, "");

    }

    /**
     * 通过书签获取最底层目录标题列表
     *
     * @param bookmark 书签对象列表
     * @return 标题列表
     */
    public static List<String> getTitleListByBookmark(List<Map<String, Object>> bookmark) {

        if (bookmark == null || bookmark.size() <= 0) {
            return null;
        }

        Map<Integer, String> bookmarkMap = getBookmarkMapByDepth(bookmark, 0, false);
        return getTitleListByBookmark(bookmarkMap);
    }

    /**
     * 通过书签获取最底层目录标题列表
     *
     * @param bookmarkMap 书签对象map
     * @return 标题列表
     */
    public static List<String> getTitleListByBookmark(Map<Integer, String> bookmarkMap) {

        if (bookmarkMap == null || bookmarkMap.size() <= 0) {
            return null;
        }

        // 存放所有目录标题
        List<String> titleList = new ArrayList<>();

        bookmarkMap.forEach((k, v) -> titleList.add(v));

        return titleList;
    }

    /**
     * 增加父/爷等标题，优化切割及合并细节（选择标题后、合并文件前 一定要执行一下此操作）
     *
     * @param fileNameList 文件名列表
     * @return 更细的 文件名列表
     */
    public static List<String> evolveTitle(List<String> fileNameList) {
        List<String> titleList = new ArrayList<>();
        fileNameList.forEach((title) -> addParentTitle(titleList, title));
        return titleList;
    }

    /**
     * 通过文件目录获取 带层级 的书签对象列表
     *
     * @param sourceFilePath 源文件路径
     * @param startPage      目录开始页数
     * @param endPage        目录结束页数
     * @return 带层级 的书签对象列表
     */
    public static List<Map<String, Object>> getBookmarkByCatalog(String sourceFilePath, int startPage, int endPage) {
        // 先通过文件获取所有的目录
        List<Map<String, Object>> catalog = getCatalogByResource(sourceFilePath, startPage, endPage);
        // 通过目录转换为可用的目录列表
        return getBookmarkByCatalog(catalog);
    }

    /**
     * 通过文件书签获取该文件 带层级 的书签对象列表
     *
     * @param sourceFilePath 源文件路径
     * @return 带层级 书签对象列表
     */
    public static List<Map<String, Object>> getBookmarkByResource(String sourceFilePath) {

        if (isEmpty(sourceFilePath)) {
            return null;
        }

        PdfReader reader = null;
        try {
            reader = new PdfReader(sourceFilePath);
            return new ArrayList<>(SimpleBookmark.getBookmark(reader));
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * 获取源文件所在文件夹路径为默认的保存文件路径
     *
     * @param filePath 源文件路径
     * @return 源文件所在文件夹的路径
     */
    public static String getDefaultPath(String filePath) {
        if (isEmpty(filePath)) {
            return null;
        }

        // 如果是文件夹,去除其\
        if (filePath.endsWith(File.separator)) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }

        int lastIndexOf = filePath.lastIndexOf(File.separator);
        if (lastIndexOf == -1) {
            return null;
        }

        return filePath.substring(0, lastIndexOf + 1);
    }

    /**
     * 通过源文件路径获取默认临时文件夹的路径
     *
     * @param filePath 源文件路径
     * @return 临时文件夹的路径
     */
    public static String getTempPath(String filePath) {
        if (isEmpty(filePath)) {
            return "";
        }

        String sourcePath = getDefaultPath(filePath);
        if (isEmpty(sourcePath)) {
            return "";
        }

        if (!sourcePath.endsWith(File.separator)) {
            sourcePath += File.separator;
        }
        return sourcePath + TEMP_DIR_NAME + File.separator;
    }

    /**
     * 删除包含该路径的所有文件 (在文件操作时无法删除使用的文件，具体原因:pdfReader流无法在document之前关闭,导致持续占用文件)
     *
     * @param resourcesPathFile 资源路径文件
     */
    public static void deleteTempFile(File resourcesPathFile) {
        // 删除源文件
        if (resourcesPathFile != null) {
            if (resourcesPathFile.isDirectory()) {
                File[] files = resourcesPathFile.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        deleteTempFile(file);
                    }
                }
                resourcesPathFile.delete();
            } else {
                resourcesPathFile.delete();
            }
        }
    }

    /**
     * 根据系统获取对应的 输入中文可显示 的默认字体路径
     *
     * @return 默认字体路径
     */
    private static String getDefaultFontPath() {
        String prefixFont;
        String os = System.getProperties().getProperty("os.name");
        if (os.startsWith("win") || os.startsWith("Win")) {
            prefixFont = "C:/Windows/Fonts" + File.separator;
        } else {
            prefixFont = "/usr/share/fonts/chinese" + File.separator;
        }
        return prefixFont + "simsun.ttc,0";
    }

    /**
     * 为PDF文件添加书签（只能添加单层书签，不能做到多层级）
     *
     * @param writer      PDF写出流
     * @param bookmarkMap 书签map 如(页索引,"目录名称")
     */
    @Deprecated
    protected static void addBookmark(PdfWriter writer, Map<Integer, String> bookmarkMap) {
        if (writer == null || bookmarkMap == null || bookmarkMap.size() <= 0) {
            return;
        }

        // 获取主书签
        PdfOutline root = writer.getRootOutline();
        if (root == null) {
            return;
        }

        // 转换为有序map
        bookmarkMap = new TreeMap<>(bookmarkMap);

        // 获取总页数
        int pageNumber = writer.getPageNumber();
        // 标签事件
        PdfAction pdfAction;

        if (bookmarkMap.size() > 0) {

            // 遍历目录map添加书签
            for (Entry<Integer, String> entry : bookmarkMap.entrySet()) {

                // 目录页数大于总页数直接返回
                if (entry.getKey() > pageNumber) {
                    return;
                }
                // 设置书签动作
                pdfAction = PdfAction.gotoLocalPage(entry.getKey(), new PdfDestination(PdfDestination.FIT), writer);
                // 添加 关联书签、关联动作、书签名、是否打开
                new PdfOutline(root, pdfAction, entry.getValue(), false);
            }
        }
    }

    /**
     * 为PDF文件添加书签
     *
     * @param writer   文档写出流
     * @param bookmark 目录对象list
     */
    private static void addBookmark(PdfWriter writer, List<Map<String, Object>> bookmark) {

        if (writer == null || bookmark == null || bookmark.size() <= 0) {
            return;
        }

        // 获取主标签
        PdfOutline root = writer.getRootOutline();

        // 获取总页数
        int totalPage = writer.getPageNumber();

        // 重新排列书签序号
        resortBookmark(bookmark);

        // 开始递归添加书签
        addBookmark(writer, root, bookmark, totalPage);
    }

    /**
     * 传入一个存放标题的list，获取标题的所有父/爷等标题 （增强切割细节，使合并用的切割文件分的更细，合并效果更好，书签显示也更人性化）
     *
     * @param titleList 存放标题的空列表
     * @param title     单个标题
     */
    private static void addParentTitle(List<String> titleList, String title) {
        String parent = title;
        // 获取父辈标题
        parent = title.substring(0, title.lastIndexOf("_") != -1 ? title.lastIndexOf("_") : 0);
        if (!isEmpty(parent)) {
            addParentTitle(titleList, parent);
        }

        // 判断当前标题是否已经存在
        if (!titleList.contains(title)) {
            titleList.add(title);
        }
    }

    /**
     * 递归添加书签
     *
     * @param writer    文档写入流
     * @param outline   书签对象
     * @param bookmark  目录对象list
     * @param totalPage PDF总页数
     */
    @SuppressWarnings("unchecked")
    private static void addBookmark(PdfWriter writer, PdfOutline outline, List<Map<String, Object>> bookmark, int totalPage) {

        if (writer == null || outline == null || bookmark == null || bookmark.size() <= 0) {
            return;
        }

        // 标签事件
        PdfAction pdfAction;
        PdfOutline pdfOutline;
        int page = 0;
        String strPage;

        // 遍历目录map添加书签
        for (Map<String, Object> map : bookmark) {

            // 获取目录的索引页
            strPage = map.get("Page").toString().trim();
            page = Integer.valueOf(strPage.substring(0, !strPage.contains(" ") ? strPage.length() : strPage.indexOf(" ")));

            // 目录页数大于总页数直接返回
            if (page > totalPage) {
                return;
            }

            // 设置书签动作
            pdfAction = PdfAction.gotoLocalPage(page, new PdfDestination(PdfDestination.FIT), writer);
            // 添加 关联书签、关联动作、书签名、是否打开
            pdfOutline = new PdfOutline(outline, pdfAction, (String) map.get("Title"), true);

            // 获取子目录
            List<Map<String, Object>> kids = (List<Map<String, Object>>) map.get("Kids");
            if (kids != null) {
                addBookmark(writer, pdfOutline, kids, totalPage);
            }
        }
    }

    /**
     * 设置页码
     *
     * @param writer PDF写出流
     */
    private static void addPagination(PdfWriter writer) {
        if (writer != null) {
            writer.setPageEvent(new XOfYPagination());
        }
    }

    /**
     * 创建新文档时，在文档开启之前设置密码
     *
     * @param writer   PDF写出流
     * @param userPwd  只读权限密码
     * @param adminPwd 管理员权限密码
     */
    protected static void addPassword(PdfWriter writer, String userPwd, String adminPwd) {
        try {
            writer.setEncryption(userPwd.getBytes(), adminPwd.getBytes(), PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新排列书签中的层级序号
     *
     * @param bookmark 书签列表
     */
    private static void resortBookmark(List<Map<String, Object>> bookmark) {

        if (bookmark != null && bookmark.size() > 0) {
            resortBookmark(bookmark, null, 0);
        }
    }

    /**
     * 通过目录map和层级深度获取对应层级的目录map
     *
     * @param bookmark       源目录map
     * @param depth          目录深度 0 默认获取所有子目录
     * @param titleStructure 文件所在的目录分支路径，如（1.2_1.2.1_）
     * @param withParent     是否包含父辈目录
     * @return 对应层级的书签map
     */
    @SuppressWarnings("unchecked")
    private static Map<Integer, String> getBookmarkMapByDepth(List<Map<String, Object>> bookmark, int depth, boolean withParent, String titleStructure) {

        if (bookmark == null) {
            return null;
        }

        String structure;
        int depthLevel = depth - 1;

        Map<Integer, String> bookmarkMap = new TreeMap<>();
        List<Map<String, Object>> kids;
        String title;
        String strPage;
        int page;
        for (Map<String, Object> map : bookmark) {

            if (isEmpty(map.get("Title")) || isEmpty(map.get("Page"))) {
                continue;
            }

            // 获取带层级的目录名称
            title = titleStructure + map.get("Title").toString().trim();
            // 获取目录的索引页
            strPage = map.get("Page").toString().trim();
            page = Integer.valueOf(strPage.substring(0, !strPage.contains(" ") ? strPage.length() : strPage.indexOf(" ")));

            // 添加父辈目录
            if (withParent) {
                bookmarkMap.put(page, title);
            }

            // 获取子目录
            kids = (List<Map<String, Object>>) map.get("Kids");

            // 默认获取到所有最底层目录
            if (depthLevel < 0) {
                if (kids != null) {
                    structure = title + "_";
                    bookmarkMap.putAll(getBookmarkMapByDepth(kids, depthLevel, withParent, structure));
                } else {
                    bookmarkMap.put(page, title);
                }
            }
            // 继续递归查找子目录是否为当前深度目录
            else if (depthLevel > 0) {
                if (kids != null) {
                    structure = title + "_";
                    bookmarkMap.putAll(getBookmarkMapByDepth(kids, depthLevel, withParent, structure));
                }
            }
            // 该目录为当前深度的目录
            else {
                // 将该目录添加至map
                bookmarkMap.put(page, title);
            }

        }
        return bookmarkMap;

    }

    /**
     * 转换列表的每一个节点信息
     *
     * @param bookmark 书签列表
     * @return 书签map
     */
    private static Map<Integer, String> getBookmarkMapAll(List<Map<String, Object>> bookmark) {
        return getBookmarkMapAll(bookmark, "");
    }

    /**
     * 递归获取每一个节点信息
     *
     * @param bookmark  书签列表
     * @param structure 目录字符串结构
     * @return 书签map
     */
    @SuppressWarnings("unchecked")
    private static Map<Integer, String> getBookmarkMapAll(List<Map<String, Object>> bookmark, String structure) {
        if (bookmark == null || bookmark.size() <= 0) {
            return null;
        }

        Map<Integer, String> bookmarkMap = new HashMap<>();
        List<Map<String, Object>> kids;
        String title;
        String strPage;
        int page;
        for (Map<String, Object> map : bookmark) {
            title = (String) map.get("Title");
            title = isEmpty(structure) ? title : structure + "_" + title;
            strPage = map.get("Page").toString().trim();
            page = Integer.valueOf(strPage.substring(0, !strPage.contains(" ") ? strPage.length() : strPage.indexOf(" ")));
            bookmarkMap.put(page, title);
            kids = (List<Map<String, Object>>) map.get("Kids");
            if (kids != null) {
                bookmarkMap.putAll(getBookmarkMapAll(kids, title));
            }
        }
        return bookmarkMap;
    }

    /**
     * 通过文件的目录获取所有目录列表
     *
     * @param sourceFilePath 源文件路径
     * @param startPage      目录开始页数
     * @param endPage        目录结束页数
     * @return 目录列表
     */
    private static List<Map<String, Object>> getCatalogByResource(String sourceFilePath, int startPage, int endPage) {

        if (isEmpty(sourceFilePath)) {
            return null;
        }

        if (startPage <= 0) {
            startPage = 1;
        }

        List<Map<String, Object>> bookmark = new ArrayList<>();
        PdfReader reader = null;
        try {
            reader = new PdfReader(sourceFilePath);

            String catalogs = "";
            for (int i = startPage; i <= endPage; i++) {
                catalogs = PdfTextExtractor.getTextFromPage(reader, i);
                // 每行目录信息
                String[] catalogInfos = catalogs.split("\n");
                for (String catalog : catalogInfos) {
                    // 获取标题
                    String title = getCatalogTitle(catalog);
                    // 获取页数
                    String page = getCatalogPageNumber(catalog);
                    if (isEmpty(title) || isEmpty(page)) {
                        continue;
                    }
                    // 获取层级
                    Integer hierarchy = getCatalogHierarchy(catalog);

                    Map<String, Object> map = new HashMap<>();
                    map.put("Title", title);
                    map.put("Page", page);
                    // 层级
                    map.put("Hierarchy", hierarchy);
                    bookmark.add(map);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return bookmark;
    }

    /**
     * 通过文件目录获取 带层级 的书签对象列表
     *
     * @param catalog 目录对象列表
     * @return 带层级 的书签对象列表
     */
    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> getBookmarkByCatalog(List<Map<String, Object>> catalog) {
        try {
            if (catalog == null || catalog.size() <= 0) {
                return null;
            }

            // 按顺序存放当前层级节点的父节点
            Deque<Map<String, Object>> deque = new ArrayDeque<>();
            // 目录根
            List<Map<String, Object>> root = new ArrayList<>();
            // 层级,用于判断前一个目录和当前目录的关系
            int preHierarchy = 0;

            // 遍历每条目录
            for (Map<String, Object> map : catalog) {

                // 获取当前目录层级
                int hierarchy = (Integer) map.get("Hierarchy");

                // 非前一个目录的 子目录要先删除栈
                if (hierarchy <= preHierarchy) {
                    // 重新开始父层级（可能相差几层）
                    int count = preHierarchy - hierarchy + 1;
                    for (int i = 0; i < count; i++) {
                        deque.pop();
                    }
                }

                // 根节点
                if (hierarchy == 1) {
                    root.add(map);
                }
                // 节点下子节点
                else {
                    // 获取当前节点的父节点
                    Map<String, Object> parent = deque.peek();
                    if (hierarchy > preHierarchy) {
                        // 此处的父节点肯定没有子节点
                        List<Map<String, Object>> kids = new ArrayList<>();
                        // 将当前节点加入父节点
                        kids.add(map);
                        parent.put("Kids", kids);
                    } else {
                        // 添加子节点
                        List<Map<String, Object>> kids = (List<Map<String, Object>>) parent.get("Kids");
                        kids.add(map);
                    }
                }
                // 将当前节点压栈
                deque.push(map);
                // 前一个层级改变
                preHierarchy = hierarchy;
            }
            return root;
        } catch (Exception e) {
            throw new RuntimeException("目录解析错误，目录格式错误或您的目录可能是盗版PDF文件生成的！");
        }
    }

    /**
     * 通过对应文件获取该文件的长宽模板，用于和生成的文档大小匹配
     *
     * @param filePath 文件的路径
     * @return 对应文件的长宽模板
     */
    private static Rectangle getRectangleByFilePath(String filePath) throws IOException {

        if (isEmpty(filePath)) {
            return null;
        }

        PdfReader reader = new PdfReader(filePath);
        Rectangle rectangle = reader.getPageSize(1);

        // 关闭读取流
        reader.close();
        return rectangle;
    }

    /**
     * 判断对象为空
     *
     * @param o 普通对象
     * @return if true return null , else return false
     */
    private static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            return "".equals(o.toString().trim()) || "null".equalsIgnoreCase(o.toString().trim());
        } else {
            return false;
        }
    }

    /**
     * 通过路径创建文件夹
     *
     * @param path 文件夹路径
     * @return 文件夹对象
     */
    public static File createDir(String path) {
        File file;
        try {
            file = new File(path);
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                if (!isCreate) {
                    return null;
                }
            }
            return file;
        } catch (Exception e) {
            throw new RuntimeException("文件夹路径错误！");
        }
    }

    /**
     * 通过路径创建文件
     *
     * @param path 文件路径
     * @return 文件对象
     */
    public static File createFile(String path) {
        File file;
        try {
            file = new File(path);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    return null;
                }
            }
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return null;
                }
            }
            return file;
        } catch (Exception e) {
            throw new RuntimeException("文件路径错误！");
        }
    }

    /**
     * 获取路径创建文件/文件夹
     *
     * @param path 路径
     * @return 文件/文件夹对象
     */
    public static File createFileOrDir(String path) {
        File file;
        try {
            file = new File(path);
            int index = path.lastIndexOf(File.separator);
            if (index == -1) {
                return null;
            }
            // 获取\后的字符串
            String sufPath = path.substring(index + 1);

            // 创建文件
            if (!"".equals(sufPath) && sufPath.contains(".")) {
                if (!file.getParentFile().exists()) {
                    if (!file.getParentFile().mkdirs()) {
                        return null;
                    }
                }
                if (!file.exists()) {
                    if (!file.createNewFile()) {
                        return null;
                    }
                }
            }
            // 创建文件夹
            else {
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        return null;
                    }
                }
            }
            return file;
        } catch (Exception e) {
            throw new RuntimeException("文件路径错误！");
        }
    }

    private static void closeDocument(Document document) {
        closeDocument(document, null, null, null, null);
    }

    private static void closeDocument(Document document, PdfWriter writer, OutputStream os) {
        closeDocument(document, null, null, writer, os);
    }

    private static void closeDocument(Document document, PdfReader reader, OutputStream os) {
        closeDocument(document, null, reader, null, os);
    }

    private static void closeDocument(Document document, PdfReader reader, PdfWriter writer, OutputStream os) {
        closeDocument(document, null, reader, writer, os);
    }

    /**
     * 结束对文档的操作时关闭文档编辑模式
     *
     * @param document 文档对象
     * @param stamper  打印对象
     * @param reader   读流对象
     * @param writer   写流对象
     * @param os       文件输出流
     */
    private static void closeDocument(Document document, PdfStamper stamper, PdfReader reader, PdfWriter writer, OutputStream os) {

        // 先关闭
        if (document != null) {
            document.close();
        }

        if (stamper != null) {
            try {
                stamper.close();
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
        }

        if (reader != null) {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (writer != null) {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 通过目录字符串 获取当前目录的层级
     *
     * @param catalog 目录字符串
     * @return 当前目录的层级
     */
    private static Integer getCatalogHierarchy(String catalog) {

        if (isEmpty(catalog)) {
            return null;
        }

        // 获取目录的层级字符串
        String catalogHierarchy = getCatalogTitle(catalog);

        if (catalogHierarchy == null) {
            return null;
        }

        // 默认层级
        int hierarchy = 1;
        // 通过" "获取目录名称前的层级
        String[] split = catalogHierarchy.split(" ");
        if (split.length > 1) {
            if (split[0].contains(".")) {
                // 通过.分割目录层级,通过长度获取层级数
                hierarchy = split[0].split("\\.").length;
            }
        }
        return hierarchy;
    }

    /**
     * 获取目录字符串的目录标题
     *
     * @param catalog 目录字符串
     * @return 目录标题
     */
    private static String getCatalogTitle(String catalog) {

        if (isEmpty(catalog)) {
            return null;
        }

        String title = null;

        Integer lastIndexOf = getLastPointIndex(catalog, 2);
        if (lastIndexOf == null) {
            return null;
        }

        // 通过" "获取目录名称 第一个字的索引
        int indexOf = catalog.substring(0, lastIndexOf).indexOf(" ");
        // 有层级的目录
        if (indexOf != -1) {
            // 获取目录名称最后一个字的索引
            int indexOf2 = catalog.substring(indexOf + 1).indexOf("..") + 1;// 加一为空格
            if (indexOf2 <= 0) {
                title = catalog.substring(0, indexOf + indexOf2);
            }
        }
        // 其他目录
        else {
            int indexOf2 = catalog.indexOf("..");
            if (indexOf2 != -1) {
                title = catalog.substring(0, indexOf2);
            }
        }

        if (title == null) {
            return null;
        }

        // 去除 可能存在的 多余的 标点符号
        title = trimPoint(title, 2);
        return title;
    }

    /**
     * 获取目录字符串的索引页数
     *
     * @param catalog 目录字符串
     * @return 索引页数
     */
    private static String getCatalogPageNumber(String catalog) {

        if (isEmpty(catalog)) {
            return null;
        }

        Integer lastIndexOf = getLastPointIndex(catalog, 1);
        if (lastIndexOf == null) {
            return null;
        }

        // 获取页数索引
        String page = catalog.substring(lastIndexOf + 1);

        // 去除 可能存在的 多余的 标点符号
        page = trimPoint(page, 1).replace(" ", "");

        // 匹配索引页数字是否合法
        Matcher matcher = pattern.matcher(page);
        if (matcher.matches()) {
            return page;
        } else {
            return null;
        }

    }

    /**
     * 去除字符串中的句号
     *
     * @param str      字符串
     * @param pointNum .的数量
     */
    private static String trimPoint(String str, int pointNum) {

        if (isEmpty(str)) {
            return "";
        }

        String point = ".";
        if (pointNum == 2) {
            point = "..";
        }
        if (str.contains(point)) {
            str = str.replace(point, "");
        }
        if (str.contains("…")) {
            str = str.replace("…", "");
        }
        return str.trim();
    }

    /**
     * 获取目录中最后一个句号索引
     *
     * @param catalog  目录字符串
     * @param pointNum .的数量
     * @return 目录中最后一个句号索引
     */
    private static Integer getLastPointIndex(String catalog, int pointNum) {

        if (isEmpty(catalog)) {
            return null;
        }

        String point = ".";
        if (pointNum == 2) {
            point = "..";
        }
        int lastIndexOf = catalog.lastIndexOf(point);
        if (lastIndexOf == -1) {
            int lastIndexOf2 = catalog.lastIndexOf("…");
            if (lastIndexOf2 == -1) {
                return null;
            }
            lastIndexOf = lastIndexOf2;
        }
        return lastIndexOf;
    }

    /**
     * 递归重排书签目录
     *
     * @param bookmark       目录列表
     * @param preName        目录前缀的前缀
     * @param hierarchyLevel 目录层级
     */
    @SuppressWarnings("unchecked")
    private static void resortBookmark(List<Map<String, Object>> bookmark, String preName, int hierarchyLevel) {
        if (bookmark == null || bookmark.size() <= 0) {
            return;
        }

        // 目录排序号
        int level = 1;
        List<Map<String, Object>> kids;
        for (Map<String, Object> map : bookmark) {
            String title = (String) map.get("Title");
            title = title.trim();
            int index = title.indexOf(" ");
            int hierarchy = hierarchyLevel + 1;

            // 判断目录标题是否有前缀
            String preTitle;

            // 章或节
            if (hierarchy == 1) {
                preTitle = "第" + level + "章";
            } else {
                preTitle = preName + "." + String.valueOf(level);
            }

            // 判断前缀 给出对应层级的标题名称
            if (index == -1) {
                title = preTitle + " " + title;
            } else {
                title = preTitle + title.substring(index);
            }

            // 更新标题
            map.replace("Title", title);
            kids = (List<Map<String, Object>>) map.get("Kids");
            // 递归子节点
            if (kids != null) {
                // 标题前缀为章节,更改前缀为排序号
                if (hierarchy == 1) {
                    preTitle = String.valueOf(level);
                }
                resortBookmark(kids, preTitle, hierarchy);
            }
            // 当前排序号加一
            level++;
        }
    }

    /**
     * 打印书签
     *
     * @param bookmark 书签/目录列表
     */
    public static void printBookmark(List<? extends Map<String, Object>> bookmark) {
        printBookmark(bookmark, 0);
    }

    /**
     * 打印书签
     *
     * @param bookmark 书签/目录列表
     * @param level    第一层目录前的制表符个数
     */
    public static void printBookmark(List<? extends Map<String, Object>> bookmark, int level) {

        if (bookmark == null || bookmark.size() <= 0) {
            return;
        }

        bookmark.forEach((map) -> {
            for (int i = 0; i < level; i++) {
                System.out.print("\t");
            }
            String strPage = String.valueOf(map.get("Page"));
            System.out.println(map.get("Title") + " (" + (!strPage.contains(" ") ? strPage : strPage.substring(0, strPage.indexOf(" "))) + ")");
            if (map.get("Kids") != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> kids = (List<Map<String, Object>>) map.get("Kids");

                printBookmark(kids, level + 1);
            }
        });
    }

    /**
     * 打印list
     *
     * @param list 书签列表
     */
    protected static void printList(List<?> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        list.forEach(System.out::println);
    }

    /**
     * 打印map
     *
     * @param map 目录Map
     */
    protected static void printMap(Map<?, ?> map) {

        if (map == null || map.size() <= 0) {
            return;
        }

        map = new TreeMap<>(map);

        map.forEach((k, v) -> {
            System.out.println(k + " " + v);
        });
    }

    /**
     * 自定义文档事件 设置文档的页码（x/y 居中格式）
     *
     * @author baB_hyf
     */
    private static class XOfYPagination extends PdfPageEventHelper {

        /**
         * 字体大小
         */
        private static final float FONT_SIZE = 12f;

        /**
         * 页码与页面底部的距离
         */
        private static final float Y_AXIS = 15f;

        /**
         * 文档模板
         */
        private PdfTemplate template = null;

        /**
         * 默认字体
         */
        private BaseFont baseFont = null;

        /**
         * 打开文档事件 生成一个模板，设置模板字体为系统字体
         */
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {

            // 得到文档的内容并为该内容新建一个模板
            template = writer.getDirectContent().createTemplate(500, 500);
            String os = System.getProperties().getProperty("os.name");

            // 获取系统默认字体的路径
            String prefixFont;
            if (os.startsWith("win") || os.startsWith("Win")) {
                prefixFont = "C:/Windows/Fonts" + File.separator;
            } else {
                prefixFont = "/usr/share/fonts/chinese" + File.separator;
            }

            try {
                // 设置字体对象为Windows系统默认的字体，可输入中文的字体
                baseFont = BaseFont.createFont(prefixFont + "simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }

        }

        /**
         * 文档每页结束前事件 写入当前页码
         */
        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            // 新建获得用户页面文本和图片内容位置的对象
            PdfContentByte directContent = writer.getDirectContent();
            // 保存图形状态
            directContent.saveState();

            // 获取当前页码 x/ 的宽度
            String text = writer.getPageNumber() + "/";
            float textSize = baseFont.getWidthPoint(text, FONT_SIZE);

            // 获取页码的x,y坐标
            float x = (document.right() + document.left()) / 2 - textSize;// X 居中
            float y = Y_AXIS;

            // 编辑页码
            directContent.beginText();
            // 设置文本字体和字号
            directContent.setFontAndSize(baseFont, FONT_SIZE);
            // 写入页码
            directContent.setTextMatrix(x, y);
            directContent.showText(text);
            directContent.endText();

            // 将模板加入到内容（content）中
            directContent.addTemplate(template, x + textSize, y);
            directContent.restoreState();

        }

        /**
         * 文档编辑关闭前事件(对所有页面操作) 设置所有页面 页码的总页数
         */
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {

            // 模板设置字体样式和大小
            template.setFontAndSize(baseFont, FONT_SIZE);

            // 设置所有页码的总页数
            template.beginText();
            template.showText(String.valueOf(writer.getPageNumber()));
            template.endText();
        }

    }

}

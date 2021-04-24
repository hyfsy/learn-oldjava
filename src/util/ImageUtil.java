package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ImageUtil
{

    // 桌面路径
    public static final String DESKTOP_PATH = System.getenv().get("USERNAME") + File.separator + "Desktop" + File.separator;

    /**
     * 将base64编码后的字符串转换为图片
     * 
     * @param str
     *            base64编码字符串
     * @param save
     *            保存路径
     * @return 是否成功
     */
    public static boolean base64ToImage(String str, String save) {
        BASE64Decoder decoder = new BASE64Decoder();
        FileOutputStream fos = null;
        try {
            byte[] data = decoder.decodeBuffer(str);
            // 处理解密后的数据
            for (byte b : data) {
                if (b < 0) {
                    b += 256;
                }
            }
            File file = new File(save);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将图片转为Base64编码
     * 
     * @param image
     *            图片路径
     * @return base64编码字符串
     */
    public static String imageToBase64(String image) {
        InputStream is = null;
        byte[] data = null;
        try {
            is = new FileInputStream(image);
            data = new byte[is.available()];
            is.read(data);
            is.close();
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(data);
            return encode;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 缩放图片
     * 
     * @param image
     *            图片路径
     * @param save
     *            保存路径
     * @param proportion
     *            缩放比例
     */
    public static void imageResize(String image, String save, double proportion) {

        BufferedImage bi = getBufferedImage(image);
        if (bi == null) {
            return;
        }

        int width = (int) (bi.getWidth() * proportion);
        int height = (int) (bi.getHeight() * proportion);
        BufferedImage resizeImage = new BufferedImage(width, height, bi.getType());

        Graphics2D g = resizeImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(bi, AffineTransform.getScaleInstance(proportion, proportion));
        g.dispose();

        saveImage(resizeImage, "bmp", save);
    }

    /**
     * 旋转图片
     * 
     * @param image
     *            图片路径
     * @param save
     *            保存路径
     * @param angle
     *            旋转角度
     */
    public static void imageRotate(String image, String save, int angle) {

        BufferedImage bi = getBufferedImage(image);

        if (bi == null) {
            return;
        }

        int width = bi.getWidth(null);
        int height = bi.getHeight(null);

        // 获取原图片副本
        BufferedImage rotateImage = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        Graphics2D g2 = rotateImage.createGraphics();

        g2.translate((width - width) / 2, (height - height) / 2);
        g2.rotate(Math.toRadians(angle), width / 2, height / 2);

        g2.drawImage(bi, null, null);
        try {
            saveImage(rotateImage, "bmp", save);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 灰度处理图片
     * 
     * @param image
     *            图片路径
     * @param save
     *            保存路径
     */
    public static void imageGray(String image, String save) {
        // 灰度图像
        BufferedImage grayImage = grayProcess(getBufferedImage(image));
        // 保存图片
        saveImage(grayImage, "bmp", save);
    }

    /**
     * 二值化图片
     * 
     * @param image
     *            图片路径
     * @param save
     *            保存路径
     */
    public static void imageBinary(String image, String save) {
        // 图像二值化
        BufferedImage binaryImage = binaryImage(getBufferedImage(image));
        saveImage(binaryImage, "bmp", save);
    }

    /**
     * 获得图像
     */
    private static BufferedImage getBufferedImage(String image) {
        try {
            File f = new File(image);
            BufferedImage bi = ImageIO.read(f);
            return bi;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存图像
     */
    private static void saveImage(BufferedImage bi, String filetype, String file) {
        // filetype为bmp无损耗！！！
        try {
            ImageIO.write(bi, filetype, new File(file));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 灰度处理
     */
    private static BufferedImage grayProcess(BufferedImage bi) {
        BufferedImage gbi = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                final int rgb = bi.getRGB(i, j);
                final int r = rgb >> 16 & 0xff;
                final int g = rgb >> 8 & 0xff;
                final int b = rgb & 0xff;
                // int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b); // 加权法灰度化
                int gray = 0 * r + 0 * g + 1 * b;
                int pixel = colorToRGB(255, gray, gray, gray);
                gbi.setRGB(i, j, pixel);
            }
        }
        return gbi;
    }

    /**
     * 自己加周围8个灰度值再除以9，算出其相对灰度值
     */
    private static double getGray(double[][] zuobiao, int x, int y, int w, int h) {
        double rs = zuobiao[x][y] + (x == 0 ? 255 : zuobiao[x - 1][y]) + (x == 0 || y == 0 ? 255 : zuobiao[x - 1][y - 1]) + (x == 0 || y == h - 1 ? 255 : zuobiao[x - 1][y + 1]) + (y == 0 ? 255 : zuobiao[x][y - 1]) + (y == h - 1 ? 255 : zuobiao[x][y + 1]) + (x == w - 1 ? 255 : zuobiao[x + 1][y]) + (x == w - 1 || y == 0 ? 255 : zuobiao[x + 1][y - 1]) + (x == w - 1 || y == h - 1 ? 255 : zuobiao[x + 1][y + 1]);
        return rs / 9;
    }

    /**
     * 颜色分量转换为RGB值
     */
    private static int colorToRGB(int alpha, int r, int g, int b) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += r;
        newPixel = newPixel << 8;
        newPixel += g;
        newPixel = newPixel << 8;
        newPixel += b;

        return newPixel;
    }

    /**
     * 图像二值化
     */
    private static BufferedImage binaryImage(BufferedImage bi) {
        int w = bi.getWidth();
        int h = bi.getHeight();
        float[] rgb = new float[3];
        double[][] zuobiao = new double[w][h];
        int black = new Color(0, 0, 0).getRGB();
        int white = new Color(255, 255, 255).getRGB();
        BufferedImage gbi = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int pixel = bi.getRGB(i, j);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = pixel & 0xff;
                float argb = (rgb[0] + rgb[1] + rgb[2]) / 3;
                zuobiao[i][j] = argb;
            }
        }

        // 阈值
        double sw = 128;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (zuobiao[i][j] < sw) {
                    gbi.setRGB(i, j, black);
                }
                else {
                    gbi.setRGB(i, j, white);
                }
            }
        }
        return gbi;
    }

}

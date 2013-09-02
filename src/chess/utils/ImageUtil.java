package chess.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {

    /**
     * 
     * ����ͼ��
     * 
     * @param srcImageFileԴ
     *            ͼ���ļ���ַ
     * @param result
     *            ���ź��ͼ���ַ
     * @param scale
     *            ���ű���
     * @param flag
     *            ����ѡ�� :true �Ŵ�; false ��С;
     */

    public static void scale(String srcImageFile, String result, int scale, boolean flag) {

        try {

            BufferedImage src = ImageIO.read(new File(srcImageFile)); // �����ļ�
            int width = src.getWidth(); // �õ�Դͼ��
            int height = src.getHeight(); // �õ�Դͼ��
            if (flag) {// �Ŵ�
                width = width * scale;
                height = height * scale;
            } else {// ��С
                width = width / scale;
                height = height / scale;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // ������С���ͼ
            g.dispose();
            ImageIO.write(tag, "JPEG", new File(result));// ������ļ���
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ͼ���и�
     * 
     * @param srcImageFileԴ
     *            ͼ���ַ
     * @param descDir
     *            ��ƬĿ���ļ���
     * @param destWidthĿ
     *            ����Ƭ���
     * @param destHeightĿ
     *            ����Ƭ�߶�
     */

    public static void cut(String srcImageFile, String descDir, int destWidth, int destHeight) {
        try {
            Image img;
            ImageFilter cropFilter; // ��ȡԴͼ��
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // Դͼ���
            int srcHeight = bi.getWidth(); // Դͼ�߶�
            if (srcWidth > destWidth && srcHeight > destHeight) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                destWidth = 200; // ��Ƭ���
                destHeight = 150; // ��Ƭ�߶�
                int cols = 0; // ��Ƭ��������
                int rows = 0; // ��Ƭ��������
                // ������Ƭ�ĺ������������
                if (srcWidth % destWidth == 0) {
                    cols = srcWidth / destWidth;
                } else {
                    cols = (int) Math.floor(srcWidth / destWidth) + 1;
                }
                if (srcHeight % destHeight == 0) {
                    rows = srcHeight / destHeight;
                } else {
                    rows = (int) Math.floor(srcHeight / destHeight) + 1;
                }
                // ѭ��������Ƭ
                // �Ľ���뷨:�Ƿ���ö��̼߳ӿ��и��ٶ�
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // �ĸ�����ֱ�Ϊͼ��������Ϳ��
                        // ��: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * 200, i * 150, destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(), cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth, destHeight,
                                BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // ������С���ͼ
                        g.dispose();
                        // ���Ϊ�ļ�
                        ImageIO.write(tag, "JPEG", new File(descDir + "pre_map_" + i + "_" + j
                                + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ͼ������ת��GIF->JPG GIF->PNG PNG->JPG PNG->GIF(X)
    public static void convert(String source, String result) {
        try {
            File f = new File(source);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, "JPG", new File(result));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // ��ɫתΪ�ڰ�
    public static void gray(String source, String result) {
        try {
            BufferedImage src = ImageIO.read(new File(source));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, "JPEG", new File(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // cut("e:/1.jpg", "e:/t/", 200, 150);
    }
}

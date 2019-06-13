package com.qihong.img2char;


import com.qihong.img2char.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.Configuration;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageToCharFilter implements ImageFilter<BufferedImage> {

    private final static Logger logger = LoggerFactory.getLogger(VideoHandleUtils.class);
    private final static String base = Constant.CHARS;

    @Override
    public BufferedImage filter(BufferedImage image) {
        BufferedImage destPicture;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        destPicture = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_3BYTE_BGR);
        int fontSize = Constant.CHAR_MIN_SIZE;
        if (Constant.autoFontSize) {
            fontSize = (int) Math.floor(imageWidth / 200) + 1;
            fontSize = fontSize > Constant.CHAR_MIN_SIZE ? fontSize : Constant.CHAR_MIN_SIZE;
        }
        // 获取图像上下文
        Graphics g = createGraphics(destPicture, imageWidth, imageHeight, fontSize);
        for (int i = image.getMinX(); i < imageHeight; i += fontSize) {
            for (int j = image.getMinY(); j < imageWidth; j += fontSize) {
                //图片的像素点其实是个矩阵，这里利用两个for循环来对每个像素进行操作
                int gray = 0;
                if (Constant.CHAR_GIFT_AVG_SAMPLING) {
                    gray = getAvgGary(image, i, imageHeight, j, imageWidth, fontSize);

                } else {
                    Object data = image.getRaster().getDataElements(j, i, null);//获取该点像素，并以object类型表示
                    int red = image.getColorModel().getRed(data);
                    int green = image.getColorModel().getGreen(data);
                    int blue = image.getColorModel().getBlue(data);
                    gray = (red * 3 + green * 6 + blue * 1) / 10;
                }

                int index = Math.round(gray * (base.length() + 1) / 255);
                String c = index >= base.length() ? " " : String.valueOf(base.charAt(index));
                g.drawString(String.valueOf(c), j, i);
            }
        }
        g.dispose();
//        int destWidth=(int)Constant.GIF_WIDTH;
//        int destHeight=(int)(imageHeight/rate);
//        Image scaledInstance = destPicture.getScaledInstance(destWidth, destHeight, Image.SCALE_DEFAULT);
//        BufferedImage i=new BufferedImage()
        return destPicture;
    }

    private int getAvgGary(BufferedImage image, int i, int maxI, int j, int maxJ, int fontSize) {
        int iMax = i + fontSize;
        iMax = iMax < maxI ? iMax : maxI;
        int jMax = j + fontSize;
        jMax = jMax < maxJ ? jMax : maxJ;
        int sumGray = 0;
        int counter = 0;

        for (int currentI = i; currentI < iMax; currentI++) {
            for (int currentJ = j; currentJ < jMax; currentJ++) {
                Object data = image.getRaster().getDataElements(currentJ, currentI, null);//获取该点像素，并以object类型表示
                int red = image.getColorModel().getRed(data);
                int green = image.getColorModel().getGreen(data);
                int blue = image.getColorModel().getBlue(data);
                int gray = (red * 3 + green * 6 + blue * 1) / 10;
                counter++;
                sumGray += gray;
            }
        }
        logger.debug("counter={},sumgray={}", counter, sumGray);
        sumGray = sumGray / (fontSize * fontSize);
        return sumGray;

    }

    /**
     * 画板默认一些参数设置
     *
     * @param image  图片
     * @param width  图片宽
     * @param height 图片高
     * @param size   字体大小
     * @return
     */
    private static Graphics createGraphics(BufferedImage image, int width,
                                           int height, int size) {
        Graphics g = image.createGraphics();
        g.setColor(null); // 设置背景色
        g.fillRect(0, 0, width, height);// 绘制背景
        g.setColor(Color.BLACK); // 设置前景色
        g.setFont(new Font("微软雅黑", Font.PLAIN, size)); // 设置字体
        return g;
    }


}

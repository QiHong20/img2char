package com.qihong.img2char;


import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageToCharFilter implements ImageFilter<BufferedImage>{
    private final static String base = "#@*+.";
    @Override
    public BufferedImage filter(BufferedImage image) {
        BufferedImage destPicture;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        destPicture = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_3BYTE_BGR);
        int fontSize=(int)Math.floor(imageWidth/200)+1;
        fontSize=fontSize>6?fontSize:6;
        // 获取图像上下文
        Graphics g = createGraphics(destPicture, imageWidth, imageHeight, fontSize);
        for (int i = image.getMinX(); i < imageHeight; i+=fontSize) {
            for (int j = image.getMinY(); j < imageWidth; j+=fontSize) {
                //图片的像素点其实是个矩阵，这里利用两个for循环来对每个像素进行操作
                Object data = image.getRaster().getDataElements(j, i, null);//获取该点像素，并以object类型表示
                int red = image.getColorModel().getRed(data);
                int green = image.getColorModel().getGreen(data);
                int blue = image.getColorModel().getBlue(data);
                int gray = (red * 3 + green * 6 + blue * 1) / 10;
                int index = Math.round(gray * (base.length() + 1) / 255);
                String c = index >= base.length() ? " " : String.valueOf(base.charAt(index));
                g.drawString(String.valueOf(c), j, i);
            }
        }
        g.dispose();
        return destPicture;
    }
    /**
     * 画板默认一些参数设置
     * @param image 图片
     * @param width 图片宽
     * @param height 图片高
     * @param size 字体大小
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

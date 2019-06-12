package com.qihong.img2char;


import java.awt.image.BufferedImage;

public class GrayFilter implements ImageFilter<BufferedImage> {
    @Override
    public BufferedImage filter(BufferedImage image) {
        BufferedImage grayPicture;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        grayPicture = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = image.getMinX(); i < imageWidth; i++) {
            for (int j = image.getMinY(); j < imageHeight; j++) {
                //图片的像素点其实是个矩阵，这里利用两个for循环来对每个像素进行操作
                Object data = image.getRaster().getDataElements(i, j, null);//获取该点像素，并以object类型表示
                int red = image.getColorModel().getRed(data);
                int green = image.getColorModel().getGreen(data);
                int blue = image.getColorModel().getBlue(data);
                red = (red * 3 + green * 6 + blue * 1) / 10;
                green = red;
                blue = green;
                //这里将r、g、b再转化为rgb值，因为bufferedImage没有提供设置单个颜色的方法，只能设置rgb。rgb最大为8388608，当大于这个值时，应减去255*255*255即16777216
                int rgb = (red * 256 + green) * 256 + blue;
                if (rgb > 8388608) {
                    rgb = rgb - 16777216;
                }
                //将rgb值写回图片
                grayPicture.setRGB(i, j, rgb);
            }
        }
        return grayPicture;
    }
}

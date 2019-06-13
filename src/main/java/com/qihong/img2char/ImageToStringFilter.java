package com.qihong.img2char;

import java.awt.image.BufferedImage;

public class ImageToStringFilter implements ImageFilter<String> {
    private final static String base = Constant.CHARS;
    @Override
    public String filter(BufferedImage sourceImage) {
        int imageWidth = sourceImage.getWidth();
        int imageHeight = sourceImage.getHeight();
        int destWidth=100;
        int offset=1;
        if(imageWidth>destWidth){
            offset=imageWidth/destWidth;
        }
        StringBuffer result = new StringBuffer();
        for (int i = sourceImage.getMinY(); i < imageHeight; i+=offset*2) {
            for (int j = sourceImage.getMinX(); j < imageWidth; j+=offset) {
                //图片的像素点其实是个矩阵，这里利用两个for循环来对每个像素进行操作
                Object data = sourceImage.getRaster().getDataElements(j, i, null);//获取该点像素，并以object类型表示
                int red = sourceImage.getColorModel().getRed(data);
                int green = sourceImage.getColorModel().getGreen(data);
                int blue = sourceImage.getColorModel().getBlue(data);
                int gray = (red * 3 + green * 6 + blue * 1) / 10;
                int index = Math.round(gray * (base.length() + 1) / 255);
                result.append(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
            }
            result.append("\r\n");
        }
        return result.toString();
    }
}

package com.qihong.img2char;


import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageScaleFilter implements ImageFilter<BufferedImage> {
    private double scale;

    public ImageScaleFilter(double scale) {
        this.scale = scale;
    }

    @Override
    public BufferedImage filter(BufferedImage image) {
        int w = image.getWidth();// 得到图片宽度。
        int h = image.getHeight();// 得到图片高度。
        int type = image.getColorModel().getTransparency();// 得到图片透明度。
        BufferedImage img;// 空的图片。
        Graphics2D graphics2d;// 空的画笔。
        (graphics2d = (img = new BufferedImage((int)(w*scale), (int)(h*scale), type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);// 旋转，degree是整型，度数，比如垂直90度。
        graphics2d.scale(scale, scale);
        graphics2d.drawImage(image, 0, 0, null);// 从bufferedimagecopy图片至img，0,0是img的坐标。
        graphics2d.dispose();
        return img;// 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
    }
}

package com.qihong.img2char;

import com.qihong.img2char.constant.Constant;

import java.awt.image.BufferedImage;

public class ScaleAndGifFilter implements  ImageFilter<BufferedImage>{

    @Override
    public BufferedImage filter(BufferedImage image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        BufferedImage dest=image;
        if(Constant.GIF_WIDTH<imageWidth){
            double scale= Constant.GIF_WIDTH/imageWidth;
            dest=new ImageScaleFilter(scale).filter(dest);
        }
        dest=new ImageToCharFilter().filter(dest);
        return dest;
    }
}

package com.qihong.img2char;

import com.qihong.img2char.constant.Constant;

import java.awt.image.BufferedImage;

public class ScaleAndGifFilter implements  ImageFilter<BufferedImage>{

    @Override
    public BufferedImage filter(BufferedImage image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if(Constant.GIF_WIDTH>imageWidth){
            return image;
        }
        double scale= Constant.GIF_WIDTH/imageWidth;
        BufferedImage dest=new ImageScaleFilter(scale).filter(image);
        dest=new ImageToCharFilter().filter(dest);
        return dest;
    }
}

package com.qihong.img2char;





import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageHandleUtils {

    /**
     * 将图片转成字符串
     *
     * @param sourceImagePath
     * @return String
     */
    public static String imageToChar(String sourceImagePath) throws IOException {
        //图片转灰度图
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(sourceImagePath));
        ImageToStringFilter imageToStringFilter = new ImageToStringFilter();
        String str = imageToStringFilter.filter(bufferedImage);
        return str;
    }

    /**
     * 将图片转成字符串
     *
     * @param sourceImagePath
     * @return
     */
    public static void imageToChar(String sourceImagePath, String destImagePath) throws IOException {
        //图片转灰度图
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(sourceImagePath));
        ImageFilter charFilter = new ImageToCharFilter();
        BufferedImage charImage = (BufferedImage) charFilter.filter(bufferedImage);
        ImageIO.write(charImage, "jpg", new File(destImagePath));
    }

    /**
     * 图片转灰度图
     *
     * @param sourceImagePath
     * @param destImagePath
     * @throws IOException
     */
    public static void ImageToGray(String sourceImagePath, String destImagePath) throws IOException {
        //图片转灰度图
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(sourceImagePath));
        ImageFilter grayFilter = new GrayFilter();
        BufferedImage grayImage = (BufferedImage) grayFilter.filter(bufferedImage);
        ImageIO.write(grayImage, "jpg", new File(destImagePath));
    }

    /**
     * gif转字符串
     *
     * @param sourceImagePath
     * @param destImagePath
     */
    public static void gifToChar(String sourceImagePath, String destImagePath) throws IOException {
        File imageFile = new File(sourceImagePath);


        GifDecoder gifDecoder = new GifDecoder();
        FileInputStream fis = new FileInputStream(imageFile);
        int code = gifDecoder.read(fis);

        if (code == GifDecoder.STATUS_OK) {
            int num = gifDecoder.getFrameCount();
            int delay=gifDecoder.getDelay(1);
            ImageFilter charFilter = new ImageToCharFilter();
            BufferedImage[] bufferedImages = new BufferedImage[num];
            for (int i = 0; i < num; i++) {
                BufferedImage bi = gifDecoder.getFrame(i);
                BufferedImage charImage = (BufferedImage) charFilter.filter(bi);
                bufferedImages[i] = charImage;
            }
            jpgToGif(bufferedImages,destImagePath,delay);
        }
    }

    /**
     * n张jpg转gif方法
     *
     * @param bufferedImages
     * @param newPic
     * @param playTime
     */
    private static void jpgToGif(BufferedImage[] bufferedImages, String newPic,
                                 int playTime) {
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start(newPic);
            for (int i = 0; i < bufferedImages.length; i++) {
                e.setDelay(playTime); // 设置播放的延迟时间
                e.addFrame(bufferedImages[i]); // 添加到帧中
            }
            e.finish();
        } catch (Exception e) {
            System.out.println("jpgToGif Failed:");
        }
    }
}

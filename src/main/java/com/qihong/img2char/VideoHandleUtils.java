package com.qihong.img2char;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VideoHandleUtils {
    /**
     * 截取视频指定帧生成gif
     * @param videoFile 视频文件
     * @param destFile
     * @throws IOException 截取的长度超过视频长度
     */
    public static void buildGif(String videoFile,String destFile) throws IOException {
        FileOutputStream targetFile = new FileOutputStream(destFile);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        ff.start();
        try {
            //ff.setFrameNumber(0);
            AnimatedGifEncoder en = new AnimatedGifEncoder();
            //en.setFrameRate((float)frameRate);
            en.start(targetFile);
            int gifDelay=(int)Math.round(1000/ff.getVideoFrameRate());
            int total=ff.getLengthInFrames();
            int counter=0;
            int health=9999999;
            while(true){
                if(total<=0||health<0)break;
                health--;
                Frame grab = ff.grab();
                BufferedImage bufferedImage=converter.getBufferedImage(grab);
                if(bufferedImage==null){
                    continue;
                }
                total--;
                counter++;
                en.addFrame(bufferedImage);
                en.setDelay(gifDelay);
                //ImageIO.write(bufferedImage, "jpg", new File(videoFile.substring(0,videoFile.lastIndexOf("."))+"-"+counter+".jpg"));
            }
            en.finish();
        }finally {
            ff.stop();
            ff.close();
        }
    }

    /**
     * 截取视频指定帧生成字符gif
     * @param videoFile 视频文件
     * @param destFile
     * @throws IOException 截取的长度超过视频长度
     */
    public static void buildCharGif(String videoFile,String destFile) throws IOException {
        FileOutputStream targetFile = new FileOutputStream(destFile);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        ff.start();
        ImageToCharFilter imageToCharFilter=new ImageToCharFilter();
        try {
            //ff.setFrameNumber(0);
            AnimatedGifEncoder en = new AnimatedGifEncoder();
            //en.setFrameRate((float)frameRate);
            en.start(targetFile);
            int gifDelay=(int)Math.round(1000/ff.getVideoFrameRate());
            int total=ff.getLengthInFrames();
            int counter=0;
            int health=9999999;
            while(true){
                if(total<=0||health<0)break;
                health--;
                Frame grab = ff.grab();
                BufferedImage bufferedImage=converter.getBufferedImage(grab);
                if(bufferedImage==null){
                    continue;
                }
                total--;
                counter++;
                BufferedImage charImage = imageToCharFilter.filter(bufferedImage);
                en.addFrame(charImage);
                en.setDelay(gifDelay);
                //ImageIO.write(bufferedImage, "jpg", new File(videoFile.substring(0,videoFile.lastIndexOf("."))+"-"+counter+".jpg"));
            }
            en.finish();
        }finally {
            ff.stop();
            ff.close();
        }
    }


    /**
     * 截取视频指定帧生成gif
     * @param videofile 视频文件
     * @param startFrame 开始帧
     * @param frameCount 截取帧数
     * @param frameRate 帧频率（默认：3）
     * @param margin 每截取一次跳过多少帧（默认：3）
     * @throws IOException 截取的长度超过视频长度
     */
    public static void buildGif(String videofile,int startFrame,int frameCount,Integer frameRate,Integer margin) throws IOException {
        if(margin==null)margin=3;
        if(frameRate==null)frameRate=3;
        FileOutputStream targetFile = new FileOutputStream(videofile.substring(0,videofile.lastIndexOf("."))+".gif");
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);

        Java2DFrameConverter converter = new Java2DFrameConverter();
        ff.start();
        try {
            if(startFrame>ff.getLengthInFrames() & (startFrame+frameCount)>ff.getLengthInFrames()) {
                throw new RuntimeException("视频太短了");
            }
            //ff.setFrameNumber(startFrame);
            AnimatedGifEncoder en = new AnimatedGifEncoder();
            en.setFrameRate(frameRate);
            en.start(targetFile);
            for(int i=0;i<frameCount;i++) {
                en.addFrame(converter.convert(ff.grab()));
               // ff.setFrameNumber(ff.getFrameNumber()+margin);
            }
            en.finish();
        }finally {
            ff.stop();
            ff.close();
        }
    }
    /**
     * 截取视频指定帧保存为指定格式的图片（图片保存在视频同文件夹下）
     * @param videoFile 视频地址
     * @param destFile 图片格式
     * @param indexFrame 第几帧（默认：5）
     * @throws Exception
     */
    public static void fetchFrame(String videoFile,String destFile,Integer indexFrame)throws Exception {
        if(indexFrame==null)indexFrame=5;
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        ff.start();
        try {
            int length = ff.getLengthInFrames();
            int i = 0;
            Frame f = null;
            while (i < length) {
                f = ff.grabFrame();
                if ((i > indexFrame) && (f.image != null)) {
                    break;
                }
                i++;
            }
            /*int owidth = f.imageWidth ;
            int oheight = f.imageHeight ;
            int width = 800;
            int height = (int) (((double) width / owidth) * oheight);*/
            Java2DFrameConverter converter =new Java2DFrameConverter();
            BufferedImage fetchedImage =converter.getBufferedImage(f);
            //bi=rotateImage(bi, 90);
            File targetFile = new File(destFile);
            ImageIO.write(fetchedImage, "jpg", targetFile);
        }finally {
            ff.stop();
            ff.close();
        }
    }
    /**
     * 将图片旋转指定度
     * @param bufferedimage 图片
     * @param degree 旋转角度
     * @return
     */
    public static BufferedImage rotateImage(BufferedImage bufferedimage, int degree){
        int w= bufferedimage.getWidth();// 得到图片宽度。
        int h= bufferedimage.getHeight();// 得到图片高度。
        int type= bufferedimage.getColorModel().getTransparency();// 得到图片透明度。
        BufferedImage img;// 空的图片。
        Graphics2D graphics2d;// 空的画笔。
        (graphics2d= (img= new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);// 旋转，degree是整型，度数，比如垂直90度。
        graphics2d.drawImage(bufferedimage, 0, 0, null);// 从bufferedimagecopy图片至img，0,0是img的坐标。
        graphics2d.dispose();
        return img;// 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
    }


}

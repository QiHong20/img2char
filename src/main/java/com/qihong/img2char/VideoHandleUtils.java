package com.qihong.img2char;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VideoHandleUtils {
    private final static DefaultFilter DEFAULT_FILTER=new DefaultFilter();
    /**
     * 截取视频指定帧生成gif
     *
     * @param videoFile 视频文件
     * @param destFile
     * @throws IOException 截取的长度超过视频长度
     */
    public static void buildGif(String videoFile, String destFile) throws IOException {
        buildGif(videoFile, destFile,null,null,DEFAULT_FILTER);
    }

    /**
     * 截取视频指定帧生成字符gif
     *
     * @param videoFile 视频文件
     * @param destFile
     * @throws IOException 截取的长度超过视频长度
     */
    public static void buildCharGif(String videoFile, String destFile) throws IOException {
        ImageToCharFilter imageToCharFilter = new ImageToCharFilter();
        buildGif(videoFile,destFile,null,null,imageToCharFilter);
    }
    private static Integer computeFrameNumber(Integer targetTime,double frameRate){
        if(targetTime==null){
            return targetTime;
        }
        double v = targetTime * frameRate / 1000;
        return (int)Math.round(v);
    }
    public static void buildCharGif(String videoFile, String destFile,Integer startTime,Integer endTime ) throws IOException {

        double frameRate=getFrameRate(videoFile);
        Integer startFrame=computeFrameNumber(startTime,frameRate);
        Integer endFrame=computeFrameNumber(endTime,frameRate);
        ImageToCharFilter imageToCharFilter = new ImageToCharFilter();
        buildGif(videoFile, destFile,startFrame,endFrame,imageToCharFilter);
    }

    /**
     * 获取视频帧率
     * @param videoFile
     * @return
     * @throws FrameGrabber.Exception
     */
    private static double getFrameRate(String videoFile) throws FrameGrabber.Exception {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        try {
            ff.start();
            return ff.getFrameRate();
        }
        finally {
            ff.stop();
            ff.close();
        }
    }

    /**
     * 生成gif
     * @param videoFile 视频地址
     * @param destFile gif地址
     * @param startFrame 开始时间
     * @param endFrame 结束时间
     * @param filter 过滤器
     * @throws IOException
     */
    private static void buildGif(String videoFile, String destFile,Integer startFrame,Integer endFrame,ImageFilter<BufferedImage> filter) throws IOException {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        ff.start();
        try {
            AnimatedGifEncoder en = new AnimatedGifEncoder();
            en.start(destFile);
            en.setFrameRate((float) ff.getVideoFrameRate());
          //  int gifDelay = (int) Math.round(1000 / ff.getVideoFrameRate());
            startFrame=startFrame==null?0:startFrame;
            endFrame=endFrame==null?ff.getLengthInVideoFrames():endFrame;
            for(int i=startFrame;i<=endFrame;i++){
                ff.setVideoFrameNumber(i);
                Frame grab = ff.grab();
                BufferedImage bufferedImage = converter.getBufferedImage(grab);
                if(bufferedImage==null)continue;
                BufferedImage charImage = filter.filter(bufferedImage);
                en.addFrame(charImage);
              //  en.setDelay(gifDelay);
            }
            en.finish();
        } finally {
            ff.stop();
            ff.close();
        }
    }

    /**
     * 默认的过滤器，不进行任何处理
     */
    private static class DefaultFilter implements ImageFilter<BufferedImage> {
        @Override
        public BufferedImage filter(BufferedImage image) {
            return image;
        }
    }

    /**
     * 截取视频指定帧保存为指定格式的图片（图片保存在视频同文件夹下）
     *
     * @param videoFile  视频地址
     * @param destFile   图片格式
     * @param indexFrame 第几帧（默认：5）
     * @throws Exception
     */
    public static void fetchFrame(String videoFile, String destFile, Integer indexFrame) throws Exception {
        if (indexFrame == null) indexFrame = 5;
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
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage fetchedImage = converter.getBufferedImage(f);
            //bi=rotateImage(bi, 90);
            File targetFile = new File(destFile);
            ImageIO.write(fetchedImage, "jpg", targetFile);
        } finally {
            ff.stop();
            ff.close();
        }
    }

    /**
     * 将图片旋转指定度
     *
     * @param bufferedimage 图片
     * @param degree        旋转角度
     * @return
     */
    public static BufferedImage rotateImage(BufferedImage bufferedimage, int degree) {
        int w = bufferedimage.getWidth();// 得到图片宽度。
        int h = bufferedimage.getHeight();// 得到图片高度。
        int type = bufferedimage.getColorModel().getTransparency();// 得到图片透明度。
        BufferedImage img;// 空的图片。
        Graphics2D graphics2d;// 空的画笔。
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);// 旋转，degree是整型，度数，比如垂直90度。
        graphics2d.drawImage(bufferedimage, 0, 0, null);// 从bufferedimagecopy图片至img，0,0是img的坐标。
        graphics2d.dispose();
        return img;// 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
    }


}

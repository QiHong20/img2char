package com.qihong.img2char;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VideoHandleUtils {
    private final static Logger logger = LoggerFactory.getLogger(VideoHandleUtils.class);

    private final static DefaultFilter DEFAULT_FILTER = new DefaultFilter();

    /**
     * 截取视频指定帧生成gif
     *
     * @param videoFile 视频文件
     * @param destFile
     * @throws IOException 截取的长度超过视频长度
     */
    public static void buildGif(String videoFile, String destFile) throws IOException {
        buildGif(videoFile, destFile, null, null, DEFAULT_FILTER);
    }

    /**
     * @param videoFile
     * @param destFile
     * @param filter    自定义过滤器
     * @throws IOException
     */
    public static void buildGif(String videoFile, String destFile, ImageFilter filter) throws IOException {
        buildGif(videoFile, destFile, null, null, filter);
    }


    /**
     * 截取视频指定帧生成字符gif
     *
     * @param videoFile 视频文件
     * @param destFile
     * @throws IOException 截取的长度超过视频长度
     */
    public static void buildCharGif(String videoFile, String destFile) throws IOException {
        buildCharGif(videoFile, destFile, null, null);
    }

    private static Integer computeFrameNumber(Integer targetTime, double frameRate) {
        if (targetTime == null) {
            return targetTime;
        }
        double v = targetTime * frameRate / 1000;
        return (int) Math.round(v);
    }

    public static void buildCharGif(String videoFile, String destFile, Integer startTime, Integer endTime) throws IOException {
        logger.info("start buildChar gif");
        double frameRate = getFrameRate(videoFile);
        Integer startFrame = computeFrameNumber(startTime, frameRate);
        Integer endFrame = computeFrameNumber(endTime, frameRate);
        ScaleAndGifFilter scaleAndGifFilter = new ScaleAndGifFilter();
        buildGif(videoFile, destFile, startFrame, endFrame, scaleAndGifFilter);
        logger.info("end buildChar gif");
    }

    public static void buildGif(String videoFile, String destFile, Integer start, Integer end, ImageFilter filter, ConvertRangeType type) throws IOException {
        logger.info("start buildChar gif");
        if (type == ConvertRangeType.Frame) {

            buildGif(videoFile, destFile, start, end, filter);
        } else {
            double frameRate = getFrameRate(videoFile);
            Integer startFrame = computeFrameNumber(start, frameRate);
            Integer endFrame = computeFrameNumber(end, frameRate);
            buildGif(videoFile, destFile, startFrame, endFrame, filter);
        }
        logger.info("end buildChar gif");
    }

    enum ConvertRangeType {
        Time, Frame
    }

    /**
     * 获取视频帧率
     *
     * @param videoFile
     * @return
     * @throws FrameGrabber.Exception
     */
    private static double getFrameRate(String videoFile) throws FrameGrabber.Exception {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        try {
            ff.start();
            return ff.getFrameRate();
        } finally {
            ff.stop();
            ff.close();
        }
    }

    /**
     * 生成gif
     *
     * @param videoFile  视频地址
     * @param destFile   gif地址
     * @param startFrame 开始时间
     * @param endFrame   结束时间
     * @param filter     过滤器
     * @throws IOException
     */
    private static void buildGif(String videoFile, String destFile, Integer startFrame, Integer endFrame, ImageFilter<BufferedImage> filter) throws IOException {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        Java2DFrameConverter converter = new Java2DFrameConverter();

        ff.start();
        try {
            AnimatedGifEncoder en = new AnimatedGifEncoder();
            en.start(destFile);
            en.setFrameRate((float) 30);
            //en.setFrameRate((float) ff.getVideoFrameRate());
            //  int gifDelay = (int) Math.round(1000 / ff.getVideoFrameRate());
            startFrame = startFrame == null ? 0 : startFrame;
            int speed = (int) Math.round(ff.getFrameRate() / 30);
            endFrame = endFrame == null ? ff.getLengthInVideoFrames() : endFrame;
            for (int i = startFrame; i <= endFrame; i += speed) {
                logger.info("共{}帧，正在处理第{}帧", endFrame, i);
                ff.setVideoFrameNumber(i);
                Frame grab = ff.grab();
                BufferedImage bufferedImage = converter.getBufferedImage(grab);
                if (bufferedImage == null) continue;
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

    public static void buildVideo(String videoFile, String destFile, Integer startTime, Integer endTime, ImageFilter filter) throws IOException {
        logger.info("start buildChar gif");
        double frameRate = getFrameRate(videoFile);
        Integer startFrame = computeFrameNumber(startTime, frameRate);
        Integer endFrame = computeFrameNumber(endTime, frameRate);
        _buildVideo(videoFile, destFile, startFrame, endFrame, filter);
        logger.info("end buildChar gif");
    }

    public static void buildGifVideo(String videoFile, String destFile, Integer startTime, Integer endTime) throws IOException {
        ScaleAndGifFilter scaleAndGifFilter = new ScaleAndGifFilter();
        buildVideo(videoFile, destFile, startTime, endTime, scaleAndGifFilter);
    }

    public static void buildGifVideo(String videoFile, String destFile) throws IOException {
        ScaleAndGifFilter scaleAndGifFilter = new ScaleAndGifFilter();
        buildVideo(videoFile, destFile, null, null, scaleAndGifFilter);
    }

    /**
     * 生成gif
     *
     * @param videoFile  视频地址
     * @param destFile   gif地址
     * @param startFrame 开始时间
     * @param endFrame   结束时间
     * @param filter     过滤器
     * @throws IOException
     */
    private static void _buildVideo(String videoFile, String destFile, Integer startFrame, Integer endFrame, ImageFilter<BufferedImage> filter) throws IOException {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        FFmpegFrameRecorder recorder = null;
        ff.start();
        try {
            int videoHeight = ff.getImageHeight();
            int videoWidth = ff.getImageWidth();
            recorder = new FFmpegFrameRecorder(destFile, videoWidth, videoHeight);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // 28
            recorder.setFormat("mp4");
            recorder.setVideoQuality(1);
            // recorder.setPixelFormat(0);
            recorder.setFrameRate(ff.getFrameRate());
            recorder.setAudioChannels(ff.getAudioChannels());
            recorder.start();
            //  int gifDelay = (int) Math.round(1000 / ff.getVideoFrameRate());
            startFrame = startFrame == null ? 0 : startFrame;
            endFrame = endFrame == null ? ff.getLengthInVideoFrames() : endFrame;
            for (int i = startFrame; i <= endFrame; i++) {
                logger.info("共{}帧，正在处理第{}帧", endFrame, i);
                ff.setVideoFrameNumber(i);
                Frame grab = ff.grabImage();
//                ff.grabSamples()
                BufferedImage bufferedImage = converter.getBufferedImage(grab);
                if (bufferedImage == null) continue;
                BufferedImage destImage = filter.filter(bufferedImage);
                Frame convert = converter.convert(destImage);
                recorder.record(convert);
            }
        } finally {
            ff.stop();
            ff.close();
            recorder.stop();
            recorder.close();
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


}

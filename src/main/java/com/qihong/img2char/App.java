package com.qihong.img2char;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {
        //图片转字符串
        //String charStr = ImageHandleUtils.imageToChar("C:\\Users\\mis\\Desktop\\images\\6.jpg");
//        IOUtils.write(charStr,new FileOutputStream("C:\\Users\\mis\\Desktop\\images\\dest3.txt"), Charset.forName("UTF-8"));
    //图片转字符串图片
      //  ImageHandleUtils.imageToChar("C:\\Users\\mis\\Desktop\\images\\6.jpg","C:\\Users\\mis\\Desktop\\images\\6avg.jpg");
        //gif转字符串gif
      //  ImageHandleUtils.gifToChar("C:\\Users\\mis\\Desktop\\images\\1.gif","C:\\Users\\mis\\Desktop\\images\\char.gif");
        //视频转gif
      // VideoHandleUtils.buildGif("C:\\Users\\mis\\Desktop\\images\\123.mp4", "C:\\Users\\mis\\Desktop\\images\\123.gif");
        //视频转字符串gif
        //VideoHandleUtils.buildCharGif("C:\\Users\\mis\\Desktop\\images\\123.mp4", "C:\\Users\\mis\\Desktop\\images\\123.gif");

        //视频转字符串gif 指定时间
       //VideoHandleUtils.buildCharGif("C:\\Users\\mis\\Desktop\\images\\123.mp4","C:\\Users\\mis\\Desktop\\images\\从第三秒开始.gif",3000,null);
       // VideoHandleUtils.buildCharGif("C:\\Users\\mis\\Desktop\\images\\123.mp4","C:\\Users\\mis\\Desktop\\images\\直到第三秒.gif",null,3000);
        //视频转字符串视频
        VideoHandleUtils.buildGifVideo("C:\\Users\\mis\\Desktop\\images\\123.mp4","C:\\Users\\mis\\Desktop\\images\\123Dest.mp4");
       // VideoHandleUtils.buildGifVideo("C:\\Users\\mis\\Desktop\\images\\Reimu 灵梦+音乐.mp4","C:\\Users\\mis\\Desktop\\images\\Reimu 灵梦+音乐dest.mp4",null,1000);
        //VideoHandleUtils.fetchFrame("C:\\Users\\mis\\Desktop\\images\\123.mp4","C:\\Users\\mis\\Desktop\\images\\123.jpg",null);
       // VideoHandleUtils.buildGif("C:\\Users\\mis\\Desktop\\images\\Reimu 灵梦+音乐.mp4","C:\\Users\\mis\\Desktop\\images\\Reimu 灵梦+音乐gray.gif",null,15000,new GrayFilter(), VideoHandleUtils.ConvertRangeType.Time);
    }
}

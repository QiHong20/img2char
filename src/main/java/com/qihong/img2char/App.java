package com.qihong.img2char;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {
//        String charStr = ImageHandleUtils.imageToChar("C:\\Users\\mis\\Desktop\\images\\6.jpg");
//        IOUtils.write(charStr,new FileOutputStream("C:\\Users\\mis\\Desktop\\images\\dest3.txt"), Charset.forName("UTF-8"));
//        System.out.println(charStr);
        //ImageHandleUtils.imageToChar("C:\\Users\\mis\\Desktop\\images\\苹果广告.jpg","C:\\Users\\mis\\Desktop\\images\\char.jpg");
        //ImageHandleUtils.gifToChar("C:\\Users\\mis\\Desktop\\images\\0.gif","C:\\Users\\mis\\Desktop\\images\\char.gif");
       //VideoHandleUtils.buildGif("C:\\Users\\mis\\Desktop\\images\\123.mp4", "C:\\Users\\mis\\Desktop\\images\\123.gif");
     //   VideoHandleUtils.buildCharGif("C:\\Users\\mis\\Desktop\\images\\123.mp4","C:\\Users\\mis\\Desktop\\images\\从第三秒开始.gif",3000,null);
       // VideoHandleUtils.buildCharGif("C:\\Users\\mis\\Desktop\\images\\123.mp4","C:\\Users\\mis\\Desktop\\images\\直到第三秒.gif",null,3000);
        VideoHandleUtils.buildCharGif("C:\\Users\\mis\\Desktop\\images\\123.mp4","C:\\Users\\mis\\Desktop\\images\\从第三秒开始.gif");
        //ImageHandleUtils.gifToChar("C:\\Users\\mis\\Desktop\\images\\123.gif", "C:\\Users\\mis\\Desktop\\images\\char123.gif");
       // VideoHandleUtils.buildGif("C:\\Users\\mis\\Desktop\\images\\123.mp4",0,200,null,null);

        //VideoHandleUtils.fetchFrame("C:\\Users\\mis\\Desktop\\images\\123.mp4","C:\\Users\\mis\\Desktop\\images\\123.jpg",null);
    }
}

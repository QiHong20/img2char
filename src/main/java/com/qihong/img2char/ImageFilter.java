package com.qihong.img2char;

import java.awt.image.BufferedImage;

public interface ImageFilter<T>  {
    T filter(BufferedImage image);
}

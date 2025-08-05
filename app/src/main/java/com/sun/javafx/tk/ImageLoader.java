package com.sun.javafx.tk;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/ImageLoader.class */
public interface ImageLoader {
    Exception getException();

    int getFrameCount();

    PlatformImage getFrame(int i2);

    int getFrameDelay(int i2);

    int getLoopCount();

    int getWidth();

    int getHeight();
}

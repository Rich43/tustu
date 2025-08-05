package com.sun.javafx.iio.common;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/common/ScalerFactory.class */
public class ScalerFactory {
    private ScalerFactory() {
    }

    public static PushbroomScaler createScaler(int sourceWidth, int sourceHeight, int numBands, int destWidth, int destHeight, boolean isSmooth) {
        PushbroomScaler scaler;
        if (sourceWidth <= 0 || sourceHeight <= 0 || numBands <= 0 || destWidth <= 0 || destHeight <= 0) {
            throw new IllegalArgumentException();
        }
        boolean isMagnifying = destWidth > sourceWidth || destHeight > sourceHeight;
        if (isMagnifying) {
            if (isSmooth) {
                scaler = new RoughScaler(sourceWidth, sourceHeight, numBands, destWidth, destHeight);
            } else {
                scaler = new RoughScaler(sourceWidth, sourceHeight, numBands, destWidth, destHeight);
            }
        } else if (isSmooth) {
            scaler = new SmoothMinifier(sourceWidth, sourceHeight, numBands, destWidth, destHeight);
        } else {
            scaler = new RoughScaler(sourceWidth, sourceHeight, numBands, destWidth, destHeight);
        }
        return scaler;
    }
}

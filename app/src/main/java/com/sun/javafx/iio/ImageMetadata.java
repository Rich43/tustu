package com.sun.javafx.iio;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/ImageMetadata.class */
public class ImageMetadata {
    public final Float gamma;
    public final Boolean blackIsZero;
    public final Integer backgroundIndex;
    public final Integer backgroundColor;
    public final Integer delayTime;
    public final Integer loopCount;
    public final Integer transparentIndex;
    public final Integer imageWidth;
    public final Integer imageHeight;
    public final Integer imageLeftPosition;
    public final Integer imageTopPosition;
    public final Integer disposalMethod;

    public ImageMetadata(Float gamma, Boolean blackIsZero, Integer backgroundIndex, Integer backgroundColor, Integer transparentIndex, Integer delayTime, Integer loopCount, Integer imageWidth, Integer imageHeight, Integer imageLeftPosition, Integer imageTopPosition, Integer disposalMethod) {
        this.gamma = gamma;
        this.blackIsZero = blackIsZero;
        this.backgroundIndex = backgroundIndex;
        this.backgroundColor = backgroundColor;
        this.transparentIndex = transparentIndex;
        this.delayTime = delayTime;
        this.loopCount = loopCount;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageLeftPosition = imageLeftPosition;
        this.imageTopPosition = imageTopPosition;
        this.disposalMethod = disposalMethod;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("[" + getClass().getName());
        if (this.gamma != null) {
            sb.append(" gamma: " + ((Object) this.gamma));
        }
        if (this.blackIsZero != null) {
            sb.append(" blackIsZero: " + ((Object) this.blackIsZero));
        }
        if (this.backgroundIndex != null) {
            sb.append(" backgroundIndex: " + ((Object) this.backgroundIndex));
        }
        if (this.backgroundColor != null) {
            sb.append(" backgroundColor: " + ((Object) this.backgroundColor));
        }
        if (this.delayTime != null) {
            sb.append(" delayTime: " + ((Object) this.delayTime));
        }
        if (this.loopCount != null) {
            sb.append(" loopCount: " + ((Object) this.loopCount));
        }
        if (this.transparentIndex != null) {
            sb.append(" transparentIndex: " + ((Object) this.transparentIndex));
        }
        if (this.imageWidth != null) {
            sb.append(" imageWidth: " + ((Object) this.imageWidth));
        }
        if (this.imageHeight != null) {
            sb.append(" imageHeight: " + ((Object) this.imageHeight));
        }
        if (this.imageLeftPosition != null) {
            sb.append(" imageLeftPosition: " + ((Object) this.imageLeftPosition));
        }
        if (this.imageTopPosition != null) {
            sb.append(" imageTopPosition: " + ((Object) this.imageTopPosition));
        }
        if (this.disposalMethod != null) {
            sb.append(" disposalMethod: " + ((Object) this.disposalMethod));
        }
        sb.append("]");
        return sb.toString();
    }
}

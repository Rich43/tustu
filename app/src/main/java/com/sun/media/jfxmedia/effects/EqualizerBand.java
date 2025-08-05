package com.sun.media.jfxmedia.effects;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/effects/EqualizerBand.class */
public interface EqualizerBand {
    public static final double MIN_GAIN = -24.0d;
    public static final double MAX_GAIN = 12.0d;

    double getCenterFrequency();

    void setCenterFrequency(double d2);

    double getBandwidth();

    void setBandwidth(double d2);

    double getGain();

    void setGain(double d2);
}

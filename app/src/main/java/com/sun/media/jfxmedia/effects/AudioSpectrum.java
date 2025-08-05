package com.sun.media.jfxmedia.effects;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/effects/AudioSpectrum.class */
public interface AudioSpectrum {
    boolean getEnabled();

    void setEnabled(boolean z2);

    int getBandCount();

    void setBandCount(int i2);

    double getInterval();

    void setInterval(double d2);

    int getSensitivityThreshold();

    void setSensitivityThreshold(int i2);

    float[] getMagnitudes(float[] fArr);

    float[] getPhases(float[] fArr);
}

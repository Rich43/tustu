package com.sun.media.jfxmedia.effects;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/effects/AudioEqualizer.class */
public interface AudioEqualizer {
    public static final int MAX_NUM_BANDS = 64;

    boolean getEnabled();

    void setEnabled(boolean z2);

    EqualizerBand addBand(double d2, double d3, double d4);

    boolean removeBand(double d2);
}

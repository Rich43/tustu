package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.effects.AudioSpectrum;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeAudioSpectrum.class */
final class NativeAudioSpectrum implements AudioSpectrum {
    private static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final int DEFAULT_THRESHOLD = -60;
    public static final int DEFAULT_BANDS = 128;
    public static final double DEFAULT_INTERVAL = 0.1d;
    private final long nativeRef;
    private float[] magnitudes = EMPTY_FLOAT_ARRAY;
    private float[] phases = EMPTY_FLOAT_ARRAY;

    private native boolean nativeGetEnabled(long j2);

    private native void nativeSetEnabled(long j2, boolean z2);

    private native void nativeSetBands(long j2, int i2, float[] fArr, float[] fArr2);

    private native double nativeGetInterval(long j2);

    private native void nativeSetInterval(long j2, double d2);

    private native int nativeGetThreshold(long j2);

    private native void nativeSetThreshold(long j2, int i2);

    NativeAudioSpectrum(long refMedia) {
        if (refMedia == 0) {
            throw new IllegalArgumentException("Invalid native media reference");
        }
        this.nativeRef = refMedia;
        setBandCount(128);
    }

    @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
    public boolean getEnabled() {
        return nativeGetEnabled(this.nativeRef);
    }

    @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
    public void setEnabled(boolean enabled) {
        nativeSetEnabled(this.nativeRef, enabled);
    }

    @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
    public int getBandCount() {
        return this.phases.length;
    }

    @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
    public void setBandCount(int bands) {
        if (bands > 1) {
            this.magnitudes = new float[bands];
            for (int i2 = 0; i2 < this.magnitudes.length; i2++) {
                this.magnitudes[i2] = -60.0f;
            }
            this.phases = new float[bands];
            nativeSetBands(this.nativeRef, bands, this.magnitudes, this.phases);
            return;
        }
        this.magnitudes = EMPTY_FLOAT_ARRAY;
        this.phases = EMPTY_FLOAT_ARRAY;
        throw new IllegalArgumentException("Number of bands must at least be 2");
    }

    @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
    public double getInterval() {
        return nativeGetInterval(this.nativeRef);
    }

    @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
    public void setInterval(double interval) {
        if (interval * 1.0E9d >= 1.0d) {
            nativeSetInterval(this.nativeRef, interval);
            return;
        }
        throw new IllegalArgumentException("Interval can't be less that 1 nanosecond");
    }

    @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
    public int getSensitivityThreshold() {
        return nativeGetThreshold(this.nativeRef);
    }

    @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
    public void setSensitivityThreshold(int threshold) {
        if (threshold <= 0) {
            nativeSetThreshold(this.nativeRef, threshold);
            return;
        }
        throw new IllegalArgumentException(String.format("Sensitivity threshold must be less than 0: %d", Integer.valueOf(threshold)));
    }

    @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
    public float[] getMagnitudes(float[] mag) {
        int size = this.magnitudes.length;
        if (mag == null || mag.length < size) {
            mag = new float[size];
        }
        System.arraycopy(this.magnitudes, 0, mag, 0, size);
        return mag;
    }

    @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
    public float[] getPhases(float[] phs) {
        int size = this.phases.length;
        if (phs == null || phs.length < size) {
            phs = new float[size];
        }
        System.arraycopy(this.phases, 0, phs, 0, size);
        return phs;
    }
}

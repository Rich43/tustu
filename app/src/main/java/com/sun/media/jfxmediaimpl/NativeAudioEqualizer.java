package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.EqualizerBand;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeAudioEqualizer.class */
final class NativeAudioEqualizer implements AudioEqualizer {
    private final long nativeRef;

    private native boolean nativeGetEnabled(long j2);

    private native void nativeSetEnabled(long j2, boolean z2);

    private native int nativeGetNumBands(long j2);

    private native EqualizerBand nativeAddBand(long j2, double d2, double d3, double d4);

    private native boolean nativeRemoveBand(long j2, double d2);

    NativeAudioEqualizer(long nativeRef) {
        if (nativeRef == 0) {
            throw new IllegalArgumentException("Invalid native media reference");
        }
        this.nativeRef = nativeRef;
    }

    @Override // com.sun.media.jfxmedia.effects.AudioEqualizer
    public boolean getEnabled() {
        return nativeGetEnabled(this.nativeRef);
    }

    @Override // com.sun.media.jfxmedia.effects.AudioEqualizer
    public void setEnabled(boolean enable) {
        nativeSetEnabled(this.nativeRef, enable);
    }

    @Override // com.sun.media.jfxmedia.effects.AudioEqualizer
    public EqualizerBand addBand(double centerFrequency, double bandwidth, double gain) {
        if (nativeGetNumBands(this.nativeRef) < 64 || gain < -24.0d || gain > 12.0d) {
            return nativeAddBand(this.nativeRef, centerFrequency, bandwidth, gain);
        }
        return null;
    }

    @Override // com.sun.media.jfxmedia.effects.AudioEqualizer
    public boolean removeBand(double centerFrequency) {
        if (centerFrequency > 0.0d) {
            return nativeRemoveBand(this.nativeRef, centerFrequency);
        }
        return false;
    }
}

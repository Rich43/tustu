package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.effects.EqualizerBand;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeEqualizerBand.class */
final class NativeEqualizerBand implements EqualizerBand {
    private final long bandRef;

    private native double nativeGetCenterFrequency(long j2);

    private native void nativeSetCenterFrequency(long j2, double d2);

    private native double nativeGetBandwidth(long j2);

    private native void nativeSetBandwidth(long j2, double d2);

    private native double nativeGetGain(long j2);

    private native void nativeSetGain(long j2, double d2);

    private NativeEqualizerBand(long bandRef) {
        if (bandRef != 0) {
            this.bandRef = bandRef;
            return;
        }
        throw new IllegalArgumentException("bandRef == 0");
    }

    @Override // com.sun.media.jfxmedia.effects.EqualizerBand
    public double getCenterFrequency() {
        return nativeGetCenterFrequency(this.bandRef);
    }

    @Override // com.sun.media.jfxmedia.effects.EqualizerBand
    public void setCenterFrequency(double centerFrequency) {
        nativeSetCenterFrequency(this.bandRef, centerFrequency);
    }

    @Override // com.sun.media.jfxmedia.effects.EqualizerBand
    public double getBandwidth() {
        return nativeGetBandwidth(this.bandRef);
    }

    @Override // com.sun.media.jfxmedia.effects.EqualizerBand
    public void setBandwidth(double bandwidth) {
        nativeSetBandwidth(this.bandRef, bandwidth);
    }

    @Override // com.sun.media.jfxmedia.effects.EqualizerBand
    public double getGain() {
        return nativeGetGain(this.bandRef);
    }

    @Override // com.sun.media.jfxmedia.effects.EqualizerBand
    public void setGain(double gain) {
        if (gain >= -24.0d && gain <= 12.0d) {
            nativeSetGain(this.bandRef, gain);
        }
    }
}

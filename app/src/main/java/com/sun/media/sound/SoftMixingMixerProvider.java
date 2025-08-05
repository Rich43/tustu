package com.sun.media.sound;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.spi.MixerProvider;

/* loaded from: rt.jar:com/sun/media/sound/SoftMixingMixerProvider.class */
public final class SoftMixingMixerProvider extends MixerProvider {
    static SoftMixingMixer globalmixer = null;
    static Thread lockthread = null;
    static final Object mutex = new Object();

    @Override // javax.sound.sampled.spi.MixerProvider
    public Mixer getMixer(Mixer.Info info) {
        SoftMixingMixer softMixingMixer;
        if (info != null && info != SoftMixingMixer.info) {
            throw new IllegalArgumentException("Mixer " + info.toString() + " not supported by this provider.");
        }
        synchronized (mutex) {
            if (lockthread != null && Thread.currentThread() == lockthread) {
                throw new IllegalArgumentException("Mixer " + info.toString() + " not supported by this provider.");
            }
            if (globalmixer == null) {
                globalmixer = new SoftMixingMixer();
            }
            softMixingMixer = globalmixer;
        }
        return softMixingMixer;
    }

    @Override // javax.sound.sampled.spi.MixerProvider
    public Mixer.Info[] getMixerInfo() {
        return new Mixer.Info[]{SoftMixingMixer.info};
    }
}

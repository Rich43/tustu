package javax.sound.sampled.spi;

import javax.sound.sampled.Mixer;

/* loaded from: rt.jar:javax/sound/sampled/spi/MixerProvider.class */
public abstract class MixerProvider {
    public abstract Mixer.Info[] getMixerInfo();

    public abstract Mixer getMixer(Mixer.Info info);

    public boolean isMixerSupported(Mixer.Info info) {
        for (Mixer.Info info2 : getMixerInfo()) {
            if (info.equals(info2)) {
                return true;
            }
        }
        return false;
    }
}

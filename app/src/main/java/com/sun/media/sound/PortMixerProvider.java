package com.sun.media.sound;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.spi.MixerProvider;

/* loaded from: rt.jar:com/sun/media/sound/PortMixerProvider.class */
public final class PortMixerProvider extends MixerProvider {
    private static PortMixerInfo[] infos;
    private static PortMixer[] devices;

    private static native int nGetNumDevices();

    private static native PortMixerInfo nNewPortMixerInfo(int i2);

    static {
        Platform.initialize();
    }

    public PortMixerProvider() {
        synchronized (PortMixerProvider.class) {
            if (Platform.isPortsEnabled()) {
                init();
            } else {
                infos = new PortMixerInfo[0];
                devices = new PortMixer[0];
            }
        }
    }

    private static void init() {
        int iNGetNumDevices = nGetNumDevices();
        if (infos == null || infos.length != iNGetNumDevices) {
            infos = new PortMixerInfo[iNGetNumDevices];
            devices = new PortMixer[iNGetNumDevices];
            for (int i2 = 0; i2 < infos.length; i2++) {
                infos[i2] = nNewPortMixerInfo(i2);
            }
        }
    }

    @Override // javax.sound.sampled.spi.MixerProvider
    public Mixer.Info[] getMixerInfo() {
        Mixer.Info[] infoArr;
        synchronized (PortMixerProvider.class) {
            infoArr = new Mixer.Info[infos.length];
            System.arraycopy(infos, 0, infoArr, 0, infos.length);
        }
        return infoArr;
    }

    @Override // javax.sound.sampled.spi.MixerProvider
    public Mixer getMixer(Mixer.Info info) {
        synchronized (PortMixerProvider.class) {
            for (int i2 = 0; i2 < infos.length; i2++) {
                if (infos[i2].equals(info)) {
                    return getDevice(infos[i2]);
                }
            }
            throw new IllegalArgumentException("Mixer " + info.toString() + " not supported by this provider.");
        }
    }

    private static Mixer getDevice(PortMixerInfo portMixerInfo) {
        int index = portMixerInfo.getIndex();
        if (devices[index] == null) {
            devices[index] = new PortMixer(portMixerInfo);
        }
        return devices[index];
    }

    /* loaded from: rt.jar:com/sun/media/sound/PortMixerProvider$PortMixerInfo.class */
    static final class PortMixerInfo extends Mixer.Info {
        private final int index;

        private PortMixerInfo(int i2, String str, String str2, String str3, String str4) {
            super("Port " + str, str2, str3, str4);
            this.index = i2;
        }

        int getIndex() {
            return this.index;
        }
    }
}

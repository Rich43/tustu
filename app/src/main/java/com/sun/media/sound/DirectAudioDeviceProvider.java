package com.sun.media.sound;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.spi.MixerProvider;

/* loaded from: rt.jar:com/sun/media/sound/DirectAudioDeviceProvider.class */
public final class DirectAudioDeviceProvider extends MixerProvider {
    private static DirectAudioDeviceInfo[] infos;
    private static DirectAudioDevice[] devices;

    private static native int nGetNumDevices();

    private static native DirectAudioDeviceInfo nNewDirectAudioDeviceInfo(int i2);

    static {
        Platform.initialize();
    }

    public DirectAudioDeviceProvider() {
        synchronized (DirectAudioDeviceProvider.class) {
            if (Platform.isDirectAudioEnabled()) {
                init();
            } else {
                infos = new DirectAudioDeviceInfo[0];
                devices = new DirectAudioDevice[0];
            }
        }
    }

    private static void init() {
        int iNGetNumDevices = nGetNumDevices();
        if (infos == null || infos.length != iNGetNumDevices) {
            infos = new DirectAudioDeviceInfo[iNGetNumDevices];
            devices = new DirectAudioDevice[iNGetNumDevices];
            for (int i2 = 0; i2 < infos.length; i2++) {
                infos[i2] = nNewDirectAudioDeviceInfo(i2);
            }
        }
    }

    @Override // javax.sound.sampled.spi.MixerProvider
    public Mixer.Info[] getMixerInfo() {
        Mixer.Info[] infoArr;
        synchronized (DirectAudioDeviceProvider.class) {
            infoArr = new Mixer.Info[infos.length];
            System.arraycopy(infos, 0, infoArr, 0, infos.length);
        }
        return infoArr;
    }

    @Override // javax.sound.sampled.spi.MixerProvider
    public Mixer getMixer(Mixer.Info info) {
        synchronized (DirectAudioDeviceProvider.class) {
            if (info == null) {
                for (int i2 = 0; i2 < infos.length; i2++) {
                    Mixer device = getDevice(infos[i2]);
                    if (device.getSourceLineInfo().length > 0) {
                        return device;
                    }
                }
            }
            for (int i3 = 0; i3 < infos.length; i3++) {
                if (infos[i3].equals(info)) {
                    return getDevice(infos[i3]);
                }
            }
            throw new IllegalArgumentException("Mixer " + info.toString() + " not supported by this provider.");
        }
    }

    private static Mixer getDevice(DirectAudioDeviceInfo directAudioDeviceInfo) {
        int index = directAudioDeviceInfo.getIndex();
        if (devices[index] == null) {
            devices[index] = new DirectAudioDevice(directAudioDeviceInfo);
        }
        return devices[index];
    }

    /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDeviceProvider$DirectAudioDeviceInfo.class */
    static final class DirectAudioDeviceInfo extends Mixer.Info {
        private final int index;
        private final int maxSimulLines;
        private final int deviceID;

        private DirectAudioDeviceInfo(int i2, int i3, int i4, String str, String str2, String str3, String str4) {
            super(str, str2, "Direct Audio Device: " + str3, str4);
            this.index = i2;
            this.maxSimulLines = i4;
            this.deviceID = i3;
        }

        int getIndex() {
            return this.index;
        }

        int getMaxSimulLines() {
            return this.maxSimulLines;
        }

        int getDeviceID() {
            return this.deviceID;
        }
    }
}

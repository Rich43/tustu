package com.sun.media.sound;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.spi.MidiDeviceProvider;

/* loaded from: rt.jar:com/sun/media/sound/AbstractMidiDeviceProvider.class */
public abstract class AbstractMidiDeviceProvider extends MidiDeviceProvider {
    private static final boolean enabled;

    abstract int getNumDevices();

    abstract MidiDevice[] getDeviceCache();

    abstract void setDeviceCache(MidiDevice[] midiDeviceArr);

    abstract Info[] getInfoCache();

    abstract void setInfoCache(Info[] infoArr);

    abstract Info createInfo(int i2);

    abstract MidiDevice createDevice(Info info);

    static {
        Platform.initialize();
        enabled = Platform.isMidiIOEnabled();
    }

    final synchronized void readDeviceInfos() {
        Info[] infoCache = getInfoCache();
        MidiDevice[] deviceCache = getDeviceCache();
        if (!enabled) {
            if (infoCache == null || infoCache.length != 0) {
                setInfoCache(new Info[0]);
            }
            if (deviceCache == null || deviceCache.length != 0) {
                setDeviceCache(new MidiDevice[0]);
                return;
            }
            return;
        }
        int length = infoCache == null ? -1 : infoCache.length;
        int numDevices = getNumDevices();
        if (length != numDevices) {
            Info[] infoArr = new Info[numDevices];
            MidiDevice[] midiDeviceArr = new MidiDevice[numDevices];
            for (int i2 = 0; i2 < numDevices; i2++) {
                Info infoCreateInfo = createInfo(i2);
                if (infoCache != null) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= infoCache.length) {
                            break;
                        }
                        Info info = infoCache[i3];
                        if (info == null || !info.equalStrings(infoCreateInfo)) {
                            i3++;
                        } else {
                            infoArr[i2] = info;
                            info.setIndex(i2);
                            infoCache[i3] = null;
                            midiDeviceArr[i2] = deviceCache[i3];
                            deviceCache[i3] = null;
                            break;
                        }
                    }
                }
                if (infoArr[i2] == null) {
                    infoArr[i2] = infoCreateInfo;
                }
            }
            if (infoCache != null) {
                for (int i4 = 0; i4 < infoCache.length; i4++) {
                    if (infoCache[i4] != null) {
                        infoCache[i4].setIndex(-1);
                    }
                }
            }
            setInfoCache(infoArr);
            setDeviceCache(midiDeviceArr);
        }
    }

    @Override // javax.sound.midi.spi.MidiDeviceProvider
    public final MidiDevice.Info[] getDeviceInfo() {
        readDeviceInfos();
        Info[] infoCache = getInfoCache();
        MidiDevice.Info[] infoArr = new MidiDevice.Info[infoCache.length];
        System.arraycopy(infoCache, 0, infoArr, 0, infoCache.length);
        return infoArr;
    }

    @Override // javax.sound.midi.spi.MidiDeviceProvider
    public final MidiDevice getDevice(MidiDevice.Info info) {
        if (info instanceof Info) {
            readDeviceInfos();
            MidiDevice[] deviceCache = getDeviceCache();
            Info[] infoCache = getInfoCache();
            Info info2 = (Info) info;
            int index = info2.getIndex();
            if (index >= 0 && index < deviceCache.length && infoCache[index] == info) {
                if (deviceCache[index] == null) {
                    deviceCache[index] = createDevice(info2);
                }
                if (deviceCache[index] != null) {
                    return deviceCache[index];
                }
            }
        }
        throw new IllegalArgumentException("MidiDevice " + info.toString() + " not supported by this provider.");
    }

    /* loaded from: rt.jar:com/sun/media/sound/AbstractMidiDeviceProvider$Info.class */
    static class Info extends MidiDevice.Info {
        private int index;

        Info(String str, String str2, String str3, String str4, int i2) {
            super(str, str2, str3, str4);
            this.index = i2;
        }

        final boolean equalStrings(Info info) {
            return info != null && getName().equals(info.getName()) && getVendor().equals(info.getVendor()) && getDescription().equals(info.getDescription()) && getVersion().equals(info.getVersion());
        }

        final int getIndex() {
            return this.index;
        }

        final void setIndex(int i2) {
            this.index = i2;
        }
    }
}

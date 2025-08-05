package com.sun.media.sound;

import com.sun.media.sound.AbstractMidiDeviceProvider;
import javax.sound.midi.MidiDevice;

/* loaded from: rt.jar:com/sun/media/sound/MidiOutDeviceProvider.class */
public final class MidiOutDeviceProvider extends AbstractMidiDeviceProvider {
    private static AbstractMidiDeviceProvider.Info[] infos = null;
    private static MidiDevice[] devices = null;
    private static final boolean enabled;

    private static native int nGetNumDevices();

    /* JADX INFO: Access modifiers changed from: private */
    public static native String nGetName(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String nGetVendor(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String nGetDescription(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String nGetVersion(int i2);

    static {
        Platform.initialize();
        enabled = Platform.isMidiIOEnabled();
    }

    @Override // com.sun.media.sound.AbstractMidiDeviceProvider
    AbstractMidiDeviceProvider.Info createInfo(int i2) {
        if (!enabled) {
            return null;
        }
        return new MidiOutDeviceInfo(i2, MidiOutDeviceProvider.class);
    }

    @Override // com.sun.media.sound.AbstractMidiDeviceProvider
    MidiDevice createDevice(AbstractMidiDeviceProvider.Info info) {
        if (enabled && (info instanceof MidiOutDeviceInfo)) {
            return new MidiOutDevice(info);
        }
        return null;
    }

    @Override // com.sun.media.sound.AbstractMidiDeviceProvider
    int getNumDevices() {
        if (!enabled) {
            return 0;
        }
        return nGetNumDevices();
    }

    @Override // com.sun.media.sound.AbstractMidiDeviceProvider
    MidiDevice[] getDeviceCache() {
        return devices;
    }

    @Override // com.sun.media.sound.AbstractMidiDeviceProvider
    void setDeviceCache(MidiDevice[] midiDeviceArr) {
        devices = midiDeviceArr;
    }

    @Override // com.sun.media.sound.AbstractMidiDeviceProvider
    AbstractMidiDeviceProvider.Info[] getInfoCache() {
        return infos;
    }

    @Override // com.sun.media.sound.AbstractMidiDeviceProvider
    void setInfoCache(AbstractMidiDeviceProvider.Info[] infoArr) {
        infos = infoArr;
    }

    /* loaded from: rt.jar:com/sun/media/sound/MidiOutDeviceProvider$MidiOutDeviceInfo.class */
    static final class MidiOutDeviceInfo extends AbstractMidiDeviceProvider.Info {
        private final Class providerClass;

        private MidiOutDeviceInfo(int i2, Class cls) {
            super(MidiOutDeviceProvider.nGetName(i2), MidiOutDeviceProvider.nGetVendor(i2), MidiOutDeviceProvider.nGetDescription(i2), MidiOutDeviceProvider.nGetVersion(i2), i2);
            this.providerClass = cls;
        }
    }
}

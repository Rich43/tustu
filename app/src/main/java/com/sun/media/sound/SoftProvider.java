package com.sun.media.sound;

import java.util.Arrays;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.spi.MidiDeviceProvider;

/* loaded from: rt.jar:com/sun/media/sound/SoftProvider.class */
public final class SoftProvider extends MidiDeviceProvider {
    static final MidiDevice.Info softinfo = SoftSynthesizer.info;
    private static final MidiDevice.Info[] softinfos = {softinfo};

    @Override // javax.sound.midi.spi.MidiDeviceProvider
    public MidiDevice.Info[] getDeviceInfo() {
        return (MidiDevice.Info[]) Arrays.copyOf(softinfos, softinfos.length);
    }

    @Override // javax.sound.midi.spi.MidiDeviceProvider
    public MidiDevice getDevice(MidiDevice.Info info) {
        if (info == softinfo) {
            return new SoftSynthesizer();
        }
        return null;
    }
}

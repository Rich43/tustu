package com.sun.media.sound;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.spi.MidiDeviceProvider;

/* loaded from: rt.jar:com/sun/media/sound/RealTimeSequencerProvider.class */
public final class RealTimeSequencerProvider extends MidiDeviceProvider {
    @Override // javax.sound.midi.spi.MidiDeviceProvider
    public MidiDevice.Info[] getDeviceInfo() {
        return new MidiDevice.Info[]{RealTimeSequencer.info};
    }

    @Override // javax.sound.midi.spi.MidiDeviceProvider
    public MidiDevice getDevice(MidiDevice.Info info) {
        if (info != null && !info.equals(RealTimeSequencer.info)) {
            return null;
        }
        try {
            return new RealTimeSequencer();
        } catch (MidiUnavailableException e2) {
            return null;
        }
    }
}

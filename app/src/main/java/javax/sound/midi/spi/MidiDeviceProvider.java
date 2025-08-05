package javax.sound.midi.spi;

import javax.sound.midi.MidiDevice;

/* loaded from: rt.jar:javax/sound/midi/spi/MidiDeviceProvider.class */
public abstract class MidiDeviceProvider {
    public abstract MidiDevice.Info[] getDeviceInfo();

    public abstract MidiDevice getDevice(MidiDevice.Info info);

    public boolean isDeviceSupported(MidiDevice.Info info) {
        for (MidiDevice.Info info2 : getDeviceInfo()) {
            if (info.equals(info2)) {
                return true;
            }
        }
        return false;
    }
}

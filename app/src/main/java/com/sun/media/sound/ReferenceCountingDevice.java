package com.sun.media.sound;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/* loaded from: rt.jar:com/sun/media/sound/ReferenceCountingDevice.class */
public interface ReferenceCountingDevice {
    Receiver getReceiverReferenceCounting() throws MidiUnavailableException;

    Transmitter getTransmitterReferenceCounting() throws MidiUnavailableException;
}

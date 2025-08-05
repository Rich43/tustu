package com.sun.media.sound;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDeviceTransmitter;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/* loaded from: rt.jar:com/sun/media/sound/MidiDeviceTransmitterEnvelope.class */
public final class MidiDeviceTransmitterEnvelope implements MidiDeviceTransmitter {
    private final MidiDevice device;
    private final Transmitter transmitter;

    public MidiDeviceTransmitterEnvelope(MidiDevice midiDevice, Transmitter transmitter) {
        if (midiDevice == null || transmitter == null) {
            throw new NullPointerException();
        }
        this.device = midiDevice;
        this.transmitter = transmitter;
    }

    @Override // javax.sound.midi.Transmitter
    public void setReceiver(Receiver receiver) {
        this.transmitter.setReceiver(receiver);
    }

    @Override // javax.sound.midi.Transmitter
    public Receiver getReceiver() {
        return this.transmitter.getReceiver();
    }

    @Override // javax.sound.midi.Transmitter, java.lang.AutoCloseable
    public void close() {
        this.transmitter.close();
    }

    @Override // javax.sound.midi.MidiDeviceTransmitter
    public MidiDevice getMidiDevice() {
        return this.device;
    }

    public Transmitter getTransmitter() {
        return this.transmitter;
    }
}

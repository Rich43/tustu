package com.sun.media.sound;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDeviceReceiver;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/* loaded from: rt.jar:com/sun/media/sound/MidiDeviceReceiverEnvelope.class */
public final class MidiDeviceReceiverEnvelope implements MidiDeviceReceiver {
    private final MidiDevice device;
    private final Receiver receiver;

    public MidiDeviceReceiverEnvelope(MidiDevice midiDevice, Receiver receiver) {
        if (midiDevice == null || receiver == null) {
            throw new NullPointerException();
        }
        this.device = midiDevice;
        this.receiver = receiver;
    }

    @Override // javax.sound.midi.Receiver, java.lang.AutoCloseable
    public void close() {
        this.receiver.close();
    }

    @Override // javax.sound.midi.Receiver
    public void send(MidiMessage midiMessage, long j2) {
        this.receiver.send(midiMessage, j2);
    }

    @Override // javax.sound.midi.MidiDeviceReceiver
    public MidiDevice getMidiDevice() {
        return this.device;
    }

    public Receiver getReceiver() {
        return this.receiver;
    }
}

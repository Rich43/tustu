package com.sun.media.sound;

import java.util.TreeMap;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDeviceReceiver;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

/* loaded from: rt.jar:com/sun/media/sound/SoftReceiver.class */
public final class SoftReceiver implements MidiDeviceReceiver {
    boolean open = true;
    private final Object control_mutex;
    private final SoftSynthesizer synth;
    TreeMap<Long, Object> midimessages;
    SoftMainMixer mainmixer;

    public SoftReceiver(SoftSynthesizer softSynthesizer) {
        this.control_mutex = softSynthesizer.control_mutex;
        this.synth = softSynthesizer;
        this.mainmixer = softSynthesizer.getMainMixer();
        if (this.mainmixer != null) {
            this.midimessages = this.mainmixer.midimessages;
        }
    }

    @Override // javax.sound.midi.MidiDeviceReceiver
    public MidiDevice getMidiDevice() {
        return this.synth;
    }

    @Override // javax.sound.midi.Receiver
    public void send(MidiMessage midiMessage, long j2) {
        synchronized (this.control_mutex) {
            if (!this.open) {
                throw new IllegalStateException("Receiver is not open");
            }
        }
        if (j2 != -1) {
            synchronized (this.control_mutex) {
                this.mainmixer.activity();
                while (this.midimessages.get(Long.valueOf(j2)) != null) {
                    j2++;
                }
                if ((midiMessage instanceof ShortMessage) && ((ShortMessage) midiMessage).getChannel() > 15) {
                    this.midimessages.put(Long.valueOf(j2), midiMessage.clone());
                } else {
                    this.midimessages.put(Long.valueOf(j2), midiMessage.getMessage());
                }
            }
            return;
        }
        this.mainmixer.processMessage(midiMessage);
    }

    @Override // javax.sound.midi.Receiver, java.lang.AutoCloseable
    public void close() {
        synchronized (this.control_mutex) {
            this.open = false;
        }
        this.synth.removeReceiver(this);
    }
}

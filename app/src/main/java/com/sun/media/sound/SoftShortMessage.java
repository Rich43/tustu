package com.sun.media.sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

/* loaded from: rt.jar:com/sun/media/sound/SoftShortMessage.class */
public final class SoftShortMessage extends ShortMessage {
    int channel = 0;

    @Override // javax.sound.midi.ShortMessage
    public int getChannel() {
        return this.channel;
    }

    @Override // javax.sound.midi.ShortMessage
    public void setMessage(int i2, int i3, int i4, int i5) throws InvalidMidiDataException {
        this.channel = i3;
        super.setMessage(i2, i3 & 15, i4, i5);
    }

    @Override // javax.sound.midi.ShortMessage, javax.sound.midi.MidiMessage
    public Object clone() {
        SoftShortMessage softShortMessage = new SoftShortMessage();
        try {
            softShortMessage.setMessage(getCommand(), getChannel(), getData1(), getData2());
            return softShortMessage;
        } catch (InvalidMidiDataException e2) {
            throw new IllegalArgumentException(e2);
        }
    }
}

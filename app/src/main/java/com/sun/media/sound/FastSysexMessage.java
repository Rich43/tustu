package com.sun.media.sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

/* loaded from: rt.jar:com/sun/media/sound/FastSysexMessage.class */
final class FastSysexMessage extends SysexMessage {
    FastSysexMessage(byte[] bArr) throws InvalidMidiDataException {
        super(bArr);
        if (bArr.length == 0 || ((bArr[0] & 255) != 240 && (bArr[0] & 255) != 247)) {
            super.setMessage(bArr, bArr.length);
        }
    }

    byte[] getReadOnlyMessage() {
        return this.data;
    }

    @Override // javax.sound.midi.SysexMessage, javax.sound.midi.MidiMessage
    public void setMessage(byte[] bArr, int i2) throws InvalidMidiDataException {
        if (bArr.length == 0 || ((bArr[0] & 255) != 240 && (bArr[0] & 255) != 247)) {
            super.setMessage(bArr, bArr.length);
        }
        this.length = i2;
        this.data = new byte[this.length];
        System.arraycopy(bArr, 0, this.data, 0, i2);
    }
}

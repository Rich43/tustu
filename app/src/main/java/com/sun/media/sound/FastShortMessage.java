package com.sun.media.sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/media/sound/FastShortMessage.class */
final class FastShortMessage extends ShortMessage {
    private int packedMsg;

    FastShortMessage(int i2) throws InvalidMidiDataException {
        this.packedMsg = i2;
        getDataLength(i2 & 255);
    }

    FastShortMessage(ShortMessage shortMessage) {
        this.packedMsg = shortMessage.getStatus() | (shortMessage.getData1() << 8) | (shortMessage.getData2() << 16);
    }

    int getPackedMsg() {
        return this.packedMsg;
    }

    @Override // javax.sound.midi.MidiMessage
    public byte[] getMessage() {
        int dataLength = 0;
        try {
            dataLength = getDataLength(this.packedMsg & 255) + 1;
        } catch (InvalidMidiDataException e2) {
        }
        byte[] bArr = new byte[dataLength];
        if (dataLength > 0) {
            bArr[0] = (byte) (this.packedMsg & 255);
            if (dataLength > 1) {
                bArr[1] = (byte) ((this.packedMsg & NormalizerImpl.CC_MASK) >> 8);
                if (dataLength > 2) {
                    bArr[2] = (byte) ((this.packedMsg & 16711680) >> 16);
                }
            }
        }
        return bArr;
    }

    @Override // javax.sound.midi.MidiMessage
    public int getLength() {
        try {
            return getDataLength(this.packedMsg & 255) + 1;
        } catch (InvalidMidiDataException e2) {
            return 0;
        }
    }

    @Override // javax.sound.midi.ShortMessage
    public void setMessage(int i2) throws InvalidMidiDataException {
        if (getDataLength(i2) != 0) {
            super.setMessage(i2);
        }
        this.packedMsg = (this.packedMsg & 16776960) | (i2 & 255);
    }

    @Override // javax.sound.midi.ShortMessage
    public void setMessage(int i2, int i3, int i4) throws InvalidMidiDataException {
        getDataLength(i2);
        this.packedMsg = (i2 & 255) | ((i3 & 255) << 8) | ((i4 & 255) << 16);
    }

    @Override // javax.sound.midi.ShortMessage
    public void setMessage(int i2, int i3, int i4, int i5) throws InvalidMidiDataException {
        getDataLength(i2);
        this.packedMsg = (i2 & 240) | (i3 & 15) | ((i4 & 255) << 8) | ((i5 & 255) << 16);
    }

    @Override // javax.sound.midi.ShortMessage
    public int getChannel() {
        return this.packedMsg & 15;
    }

    @Override // javax.sound.midi.ShortMessage
    public int getCommand() {
        return this.packedMsg & 240;
    }

    @Override // javax.sound.midi.ShortMessage
    public int getData1() {
        return (this.packedMsg & NormalizerImpl.CC_MASK) >> 8;
    }

    @Override // javax.sound.midi.ShortMessage
    public int getData2() {
        return (this.packedMsg & 16711680) >> 16;
    }

    @Override // javax.sound.midi.MidiMessage
    public int getStatus() {
        return this.packedMsg & 255;
    }

    @Override // javax.sound.midi.ShortMessage, javax.sound.midi.MidiMessage
    public Object clone() {
        try {
            return new FastShortMessage(this.packedMsg);
        } catch (InvalidMidiDataException e2) {
            return null;
        }
    }
}

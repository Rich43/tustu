package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/SysexMessage.class */
public class SysexMessage extends MidiMessage {
    public static final int SYSTEM_EXCLUSIVE = 240;
    public static final int SPECIAL_SYSTEM_EXCLUSIVE = 247;

    public SysexMessage() {
        this(new byte[2]);
        this.data[0] = -16;
        this.data[1] = -9;
    }

    public SysexMessage(byte[] bArr, int i2) throws InvalidMidiDataException {
        super(null);
        setMessage(bArr, i2);
    }

    public SysexMessage(int i2, byte[] bArr, int i3) throws InvalidMidiDataException {
        super(null);
        setMessage(i2, bArr, i3);
    }

    protected SysexMessage(byte[] bArr) {
        super(bArr);
    }

    @Override // javax.sound.midi.MidiMessage
    public void setMessage(byte[] bArr, int i2) throws InvalidMidiDataException {
        int i3 = bArr[0] & 255;
        if (i3 != 240 && i3 != 247) {
            throw new InvalidMidiDataException("Invalid status byte for sysex message: 0x" + Integer.toHexString(i3));
        }
        super.setMessage(bArr, i2);
    }

    public void setMessage(int i2, byte[] bArr, int i3) throws InvalidMidiDataException {
        if (i2 != 240 && i2 != 247) {
            throw new InvalidMidiDataException("Invalid status byte for sysex message: 0x" + Integer.toHexString(i2));
        }
        if (i3 < 0 || i3 > bArr.length) {
            throw new IndexOutOfBoundsException("length out of bounds: " + i3);
        }
        this.length = i3 + 1;
        if (this.data == null || this.data.length < this.length) {
            this.data = new byte[this.length];
        }
        this.data[0] = (byte) (i2 & 255);
        if (i3 > 0) {
            System.arraycopy(bArr, 0, this.data, 1, i3);
        }
    }

    public byte[] getData() {
        byte[] bArr = new byte[this.length - 1];
        System.arraycopy(this.data, 1, bArr, 0, this.length - 1);
        return bArr;
    }

    @Override // javax.sound.midi.MidiMessage
    public Object clone() {
        byte[] bArr = new byte[this.length];
        System.arraycopy(this.data, 0, bArr, 0, bArr.length);
        return new SysexMessage(bArr);
    }
}

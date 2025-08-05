package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/MidiMessage.class */
public abstract class MidiMessage implements Cloneable {
    protected byte[] data;
    protected int length;

    public abstract Object clone();

    protected MidiMessage(byte[] bArr) {
        this.length = 0;
        this.data = bArr;
        if (bArr != null) {
            this.length = bArr.length;
        }
    }

    protected void setMessage(byte[] bArr, int i2) throws InvalidMidiDataException {
        if (i2 < 0 || (i2 > 0 && i2 > bArr.length)) {
            throw new IndexOutOfBoundsException("length out of bounds: " + i2);
        }
        this.length = i2;
        if (this.data == null || this.data.length < this.length) {
            this.data = new byte[this.length];
        }
        System.arraycopy(bArr, 0, this.data, 0, i2);
    }

    public byte[] getMessage() {
        byte[] bArr = new byte[this.length];
        System.arraycopy(this.data, 0, bArr, 0, this.length);
        return bArr;
    }

    public int getStatus() {
        if (this.length > 0) {
            return this.data[0] & 255;
        }
        return 0;
    }

    public int getLength() {
        return this.length;
    }
}

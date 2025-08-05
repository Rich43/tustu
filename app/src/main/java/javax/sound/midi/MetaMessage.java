package javax.sound.midi;

/* loaded from: rt.jar:javax/sound/midi/MetaMessage.class */
public class MetaMessage extends MidiMessage {
    public static final int META = 255;
    private int dataLength;
    private static final long mask = 127;

    public MetaMessage() {
        this(new byte[]{-1, 0});
    }

    public MetaMessage(int i2, byte[] bArr, int i3) throws InvalidMidiDataException {
        super(null);
        this.dataLength = 0;
        setMessage(i2, bArr, i3);
    }

    protected MetaMessage(byte[] bArr) {
        super(bArr);
        this.dataLength = 0;
        if (bArr.length >= 3) {
            this.dataLength = bArr.length - 3;
            for (int i2 = 2; i2 < bArr.length && (bArr[i2] & 128) != 0; i2++) {
                this.dataLength--;
            }
        }
    }

    public void setMessage(int i2, byte[] bArr, int i3) throws InvalidMidiDataException {
        if (i2 >= 128 || i2 < 0) {
            throw new InvalidMidiDataException("Invalid meta event with type " + i2);
        }
        if ((i3 > 0 && i3 > bArr.length) || i3 < 0) {
            throw new InvalidMidiDataException("length out of bounds: " + i3);
        }
        this.length = 2 + getVarIntLength(i3) + i3;
        this.dataLength = i3;
        this.data = new byte[this.length];
        this.data[0] = -1;
        this.data[1] = (byte) i2;
        writeVarInt(this.data, 2, i3);
        if (i3 > 0) {
            System.arraycopy(bArr, 0, this.data, this.length - this.dataLength, this.dataLength);
        }
    }

    public int getType() {
        if (this.length >= 2) {
            return this.data[1] & 255;
        }
        return 0;
    }

    public byte[] getData() {
        byte[] bArr = new byte[this.dataLength];
        System.arraycopy(this.data, this.length - this.dataLength, bArr, 0, this.dataLength);
        return bArr;
    }

    @Override // javax.sound.midi.MidiMessage
    public Object clone() {
        byte[] bArr = new byte[this.length];
        System.arraycopy(this.data, 0, bArr, 0, bArr.length);
        return new MetaMessage(bArr);
    }

    private int getVarIntLength(long j2) {
        int i2 = 0;
        do {
            j2 >>= 7;
            i2++;
        } while (j2 > 0);
        return i2;
    }

    private void writeVarInt(byte[] bArr, int i2, long j2) {
        int i3 = 63;
        while (i3 > 0 && (j2 & (mask << i3)) == 0) {
            i3 -= 7;
        }
        while (i3 > 0) {
            int i4 = i2;
            i2++;
            bArr[i4] = (byte) (((j2 & (mask << i3)) >> i3) | 128);
            i3 -= 7;
        }
        bArr[i2] = (byte) (j2 & mask);
    }
}

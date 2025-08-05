package javax.sound.midi;

import org.apache.commons.net.telnet.TelnetCommand;

/* loaded from: rt.jar:javax/sound/midi/ShortMessage.class */
public class ShortMessage extends MidiMessage {
    public static final int MIDI_TIME_CODE = 241;
    public static final int SONG_POSITION_POINTER = 242;
    public static final int SONG_SELECT = 243;
    public static final int TUNE_REQUEST = 246;
    public static final int END_OF_EXCLUSIVE = 247;
    public static final int TIMING_CLOCK = 248;
    public static final int START = 250;
    public static final int CONTINUE = 251;
    public static final int STOP = 252;
    public static final int ACTIVE_SENSING = 254;
    public static final int SYSTEM_RESET = 255;
    public static final int NOTE_OFF = 128;
    public static final int NOTE_ON = 144;
    public static final int POLY_PRESSURE = 160;
    public static final int CONTROL_CHANGE = 176;
    public static final int PROGRAM_CHANGE = 192;
    public static final int CHANNEL_PRESSURE = 208;
    public static final int PITCH_BEND = 224;

    public ShortMessage() {
        this(new byte[3]);
        this.data[0] = -112;
        this.data[1] = 64;
        this.data[2] = Byte.MAX_VALUE;
        this.length = 3;
    }

    public ShortMessage(int i2) throws InvalidMidiDataException {
        super(null);
        setMessage(i2);
    }

    public ShortMessage(int i2, int i3, int i4) throws InvalidMidiDataException {
        super(null);
        setMessage(i2, i3, i4);
    }

    public ShortMessage(int i2, int i3, int i4, int i5) throws InvalidMidiDataException {
        super(null);
        setMessage(i2, i3, i4, i5);
    }

    protected ShortMessage(byte[] bArr) {
        super(bArr);
    }

    public void setMessage(int i2) throws InvalidMidiDataException {
        int dataLength = getDataLength(i2);
        if (dataLength != 0) {
            throw new InvalidMidiDataException("Status byte; " + i2 + " requires " + dataLength + " data bytes");
        }
        setMessage(i2, 0, 0);
    }

    public void setMessage(int i2, int i3, int i4) throws InvalidMidiDataException {
        int dataLength = getDataLength(i2);
        if (dataLength > 0) {
            if (i3 < 0 || i3 > 127) {
                throw new InvalidMidiDataException("data1 out of range: " + i3);
            }
            if (dataLength > 1 && (i4 < 0 || i4 > 127)) {
                throw new InvalidMidiDataException("data2 out of range: " + i4);
            }
        }
        this.length = dataLength + 1;
        if (this.data == null || this.data.length < this.length) {
            this.data = new byte[3];
        }
        this.data[0] = (byte) (i2 & 255);
        if (this.length > 1) {
            this.data[1] = (byte) (i3 & 255);
            if (this.length > 2) {
                this.data[2] = (byte) (i4 & 255);
            }
        }
    }

    public void setMessage(int i2, int i3, int i4, int i5) throws InvalidMidiDataException {
        if (i2 >= 240 || i2 < 128) {
            throw new InvalidMidiDataException("command out of range: 0x" + Integer.toHexString(i2));
        }
        if ((i3 & (-16)) != 0) {
            throw new InvalidMidiDataException("channel out of range: " + i3);
        }
        setMessage((i2 & 240) | (i3 & 15), i4, i5);
    }

    public int getChannel() {
        return getStatus() & 15;
    }

    public int getCommand() {
        return getStatus() & 240;
    }

    public int getData1() {
        if (this.length > 1) {
            return this.data[1] & 255;
        }
        return 0;
    }

    public int getData2() {
        if (this.length > 2) {
            return this.data[2] & 255;
        }
        return 0;
    }

    @Override // javax.sound.midi.MidiMessage
    public Object clone() {
        byte[] bArr = new byte[this.length];
        System.arraycopy(this.data, 0, bArr, 0, bArr.length);
        return new ShortMessage(bArr);
    }

    protected final int getDataLength(int i2) throws InvalidMidiDataException {
        switch (i2) {
            case 241:
            case 243:
                return 1;
            case 242:
                return 2;
            case 244:
            case 245:
            default:
                switch (i2 & 240) {
                    case 128:
                    case 144:
                    case 160:
                    case 176:
                    case 224:
                        return 2;
                    case 192:
                    case 208:
                        return 1;
                    default:
                        throw new InvalidMidiDataException("Invalid status byte: " + i2);
                }
            case 246:
            case 247:
            case 248:
            case TelnetCommand.GA /* 249 */:
            case 250:
            case 251:
            case 252:
            case 253:
            case 254:
            case 255:
                return 0;
        }
    }
}

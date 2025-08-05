package com.sun.media.sound;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

/* compiled from: StandardMidiFileReader.java */
/* loaded from: rt.jar:com/sun/media/sound/SMFParser.class */
final class SMFParser {
    private static final int MTrk_MAGIC = 1297379947;
    private static final boolean STRICT_PARSER = false;
    private static final boolean DEBUG = false;
    int tracks;
    DataInputStream stream;
    private int trackLength = 0;
    private byte[] trackData = null;
    private int pos = 0;

    SMFParser() {
    }

    private int readUnsigned() throws IOException {
        byte[] bArr = this.trackData;
        int i2 = this.pos;
        this.pos = i2 + 1;
        return bArr[i2] & 255;
    }

    private void read(byte[] bArr) throws IOException {
        System.arraycopy(this.trackData, this.pos, bArr, 0, bArr.length);
        this.pos += bArr.length;
    }

    private long readVarInt() throws IOException {
        byte[] bArr;
        int i2;
        long j2 = 0;
        do {
            bArr = this.trackData;
            i2 = this.pos;
            this.pos = i2 + 1;
            j2 = (j2 << 7) + (r0 & 127);
        } while ((bArr[i2] & 255 & 128) != 0);
        return j2;
    }

    private int readIntFromStream() throws IOException {
        try {
            return this.stream.readInt();
        } catch (EOFException e2) {
            throw new EOFException("invalid MIDI file");
        }
    }

    boolean nextTrack() throws InvalidMidiDataException, IOException {
        this.trackLength = 0;
        while (this.stream.skipBytes(this.trackLength) == this.trackLength) {
            int intFromStream = readIntFromStream();
            this.trackLength = readIntFromStream();
            if (intFromStream == MTrk_MAGIC) {
                if (this.trackLength < 0) {
                    return false;
                }
                try {
                    this.trackData = new byte[this.trackLength];
                    try {
                        this.stream.readFully(this.trackData);
                        this.pos = 0;
                        return true;
                    } catch (EOFException e2) {
                        return false;
                    }
                } catch (OutOfMemoryError e3) {
                    throw new IOException("Track length too big", e3);
                }
            }
        }
        return false;
    }

    private boolean trackFinished() {
        return this.pos >= this.trackLength;
    }

    void readTrack(Track track) throws InvalidMidiDataException, IOException {
        MidiMessage fastShortMessage;
        long varInt = 0;
        int i2 = 0;
        boolean z2 = false;
        while (!trackFinished() && !z2) {
            try {
                int unsigned = -1;
                varInt += readVarInt();
                int unsigned2 = readUnsigned();
                if (unsigned2 >= 128) {
                    i2 = unsigned2;
                } else {
                    unsigned = unsigned2;
                }
                switch (i2 & 240) {
                    case 128:
                    case 144:
                    case 160:
                    case 176:
                    case 224:
                        if (unsigned == -1) {
                            unsigned = readUnsigned();
                        }
                        fastShortMessage = new FastShortMessage(i2 | (unsigned << 8) | (readUnsigned() << 16));
                        continue;
                        track.add(new MidiEvent(fastShortMessage, varInt));
                    case 192:
                    case 208:
                        if (unsigned == -1) {
                            unsigned = readUnsigned();
                        }
                        fastShortMessage = new FastShortMessage(i2 | (unsigned << 8));
                        continue;
                        track.add(new MidiEvent(fastShortMessage, varInt));
                    case 240:
                        switch (i2) {
                            case 240:
                            case 247:
                                int varInt2 = (int) readVarInt();
                                if (varInt2 < 0 || varInt2 > this.trackLength - this.pos) {
                                    throw new InvalidMidiDataException("Message length is out of bounds: " + varInt2);
                                }
                                byte[] bArr = new byte[varInt2];
                                read(bArr);
                                SysexMessage sysexMessage = new SysexMessage();
                                sysexMessage.setMessage(i2, bArr, varInt2);
                                fastShortMessage = sysexMessage;
                                continue;
                                track.add(new MidiEvent(fastShortMessage, varInt));
                                break;
                            case 255:
                                int unsigned3 = readUnsigned();
                                int varInt3 = (int) readVarInt();
                                if (varInt3 < 0 || varInt3 > this.trackLength - this.pos) {
                                    throw new InvalidMidiDataException("Message length is out of bounds: " + varInt3);
                                }
                                try {
                                    byte[] bArr2 = new byte[varInt3];
                                    read(bArr2);
                                    MetaMessage metaMessage = new MetaMessage();
                                    metaMessage.setMessage(unsigned3, bArr2, varInt3);
                                    fastShortMessage = metaMessage;
                                    if (unsigned3 == 47) {
                                        z2 = true;
                                    }
                                    track.add(new MidiEvent(fastShortMessage, varInt));
                                } catch (OutOfMemoryError e2) {
                                    throw new IOException("Meta length too big", e2);
                                }
                                break;
                            default:
                                throw new InvalidMidiDataException("Invalid status byte: " + i2);
                        }
                    default:
                        throw new InvalidMidiDataException("Invalid status byte: " + i2);
                }
            } catch (ArrayIndexOutOfBoundsException e3) {
                throw new EOFException("invalid MIDI file");
            }
            throw new EOFException("invalid MIDI file");
        }
    }
}

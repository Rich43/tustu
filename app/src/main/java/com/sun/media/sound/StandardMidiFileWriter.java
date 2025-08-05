package com.sun.media.sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.SequenceInputStream;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;
import javax.sound.midi.spi.MidiFileWriter;

/* loaded from: rt.jar:com/sun/media/sound/StandardMidiFileWriter.class */
public final class StandardMidiFileWriter extends MidiFileWriter {
    private static final int MThd_MAGIC = 1297377380;
    private static final int MTrk_MAGIC = 1297379947;
    private static final int ONE_BYTE = 1;
    private static final int TWO_BYTE = 2;
    private static final int SYSEX = 3;
    private static final int META = 4;
    private static final int ERROR = 5;
    private static final int IGNORE = 6;
    private static final int MIDI_TYPE_0 = 0;
    private static final int MIDI_TYPE_1 = 1;
    private static final int bufferSize = 16384;
    private DataOutputStream tddos;
    private static final int[] types = {0, 1};
    private static final long mask = 127;

    @Override // javax.sound.midi.spi.MidiFileWriter
    public int[] getMidiFileTypes() {
        int[] iArr = new int[types.length];
        System.arraycopy(types, 0, iArr, 0, types.length);
        return iArr;
    }

    @Override // javax.sound.midi.spi.MidiFileWriter
    public int[] getMidiFileTypes(Sequence sequence) {
        int[] iArr;
        if (sequence.getTracks().length == 1) {
            iArr = new int[]{0, 1};
        } else {
            iArr = new int[]{1};
        }
        return iArr;
    }

    @Override // javax.sound.midi.spi.MidiFileWriter
    public boolean isFileTypeSupported(int i2) {
        for (int i3 = 0; i3 < types.length; i3++) {
            if (i2 == types[i3]) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.sound.midi.spi.MidiFileWriter
    public int write(Sequence sequence, int i2, OutputStream outputStream) throws IOException {
        long j2 = 0;
        if (!isFileTypeSupported(i2, sequence)) {
            throw new IllegalArgumentException("Could not write MIDI file");
        }
        InputStream fileStream = getFileStream(i2, sequence);
        if (fileStream == null) {
            throw new IllegalArgumentException("Could not write MIDI file");
        }
        byte[] bArr = new byte[16384];
        while (true) {
            int i3 = fileStream.read(bArr);
            if (i3 >= 0) {
                outputStream.write(bArr, 0, i3);
                j2 += i3;
            } else {
                return (int) j2;
            }
        }
    }

    @Override // javax.sound.midi.spi.MidiFileWriter
    public int write(Sequence sequence, int i2, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        int iWrite = write(sequence, i2, fileOutputStream);
        fileOutputStream.close();
        return iWrite;
    }

    private InputStream getFileStream(int i2, Sequence sequence) throws IOException {
        InputStream sequenceInputStream;
        int resolution;
        Track[] tracks = sequence.getTracks();
        if (i2 == 0) {
            if (tracks.length != 1) {
                return null;
            }
        } else if (i2 == 1) {
            if (tracks.length < 1) {
                return null;
            }
        } else if (tracks.length == 1) {
            i2 = 0;
        } else if (tracks.length > 1) {
            i2 = 1;
        } else {
            return null;
        }
        InputStream[] inputStreamArr = new InputStream[tracks.length];
        int i3 = 0;
        for (Track track : tracks) {
            try {
                inputStreamArr[i3] = writeTrack(track, i2);
                i3++;
            } catch (InvalidMidiDataException e2) {
            }
        }
        if (i3 == 1) {
            sequenceInputStream = inputStreamArr[0];
        } else if (i3 > 1) {
            sequenceInputStream = inputStreamArr[0];
            for (int i4 = 1; i4 < tracks.length; i4++) {
                if (inputStreamArr[i4] != null) {
                    sequenceInputStream = new SequenceInputStream(sequenceInputStream, inputStreamArr[i4]);
                }
            }
        } else {
            throw new IllegalArgumentException("invalid MIDI data in sequence");
        }
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(pipedOutputStream);
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        dataOutputStream.writeInt(MThd_MAGIC);
        dataOutputStream.writeInt(14 - 8);
        if (i2 == 0) {
            dataOutputStream.writeShort(0);
        } else {
            dataOutputStream.writeShort(1);
        }
        dataOutputStream.writeShort((short) i3);
        float divisionType = sequence.getDivisionType();
        if (divisionType == 0.0f) {
            resolution = sequence.getResolution();
        } else if (divisionType == 24.0f) {
            resolution = (-6144) + (sequence.getResolution() & 255);
        } else if (divisionType == 25.0f) {
            resolution = (-6400) + (sequence.getResolution() & 255);
        } else if (divisionType == 29.97f) {
            resolution = (-7424) + (sequence.getResolution() & 255);
        } else if (divisionType == 30.0f) {
            resolution = (-7680) + (sequence.getResolution() & 255);
        } else {
            return null;
        }
        dataOutputStream.writeShort(resolution);
        SequenceInputStream sequenceInputStream2 = new SequenceInputStream(pipedInputStream, sequenceInputStream);
        dataOutputStream.close();
        int i5 = 0 + 14;
        return sequenceInputStream2;
    }

    private int getType(int i2) {
        if ((i2 & 240) == 240) {
            switch (i2) {
                case 240:
                case 247:
                    return 3;
                case 255:
                    return 4;
                default:
                    return 6;
            }
        }
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
                return 5;
        }
    }

    private int writeVarInt(long j2) throws IOException {
        int i2 = 1;
        int i3 = 63;
        while (i3 > 0 && (j2 & (mask << i3)) == 0) {
            i3 -= 7;
        }
        while (i3 > 0) {
            this.tddos.writeByte((int) (((j2 & (mask << i3)) >> i3) | 128));
            i3 -= 7;
            i2++;
        }
        this.tddos.writeByte((int) (j2 & mask));
        return i2;
    }

    private InputStream writeTrack(Track track, int i2) throws InvalidMidiDataException, IOException, ArrayIndexOutOfBoundsException {
        int length = 0;
        int size = track.size();
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(pipedOutputStream);
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.tddos = new DataOutputStream(byteArrayOutputStream);
        long tick = 0;
        int i3 = -1;
        for (int i4 = 0; i4 < size; i4++) {
            MidiEvent midiEvent = track.get(i4);
            midiEvent.getTick();
            long tick2 = midiEvent.getTick() - tick;
            tick = midiEvent.getTick();
            int status = midiEvent.getMessage().getStatus();
            switch (getType(status)) {
                case 1:
                    int data1 = ((ShortMessage) midiEvent.getMessage()).getData1();
                    int iWriteVarInt = length + writeVarInt(tick2);
                    if (status != i3) {
                        i3 = status;
                        this.tddos.writeByte(status);
                        iWriteVarInt++;
                    }
                    this.tddos.writeByte(data1);
                    length = iWriteVarInt + 1;
                    break;
                case 2:
                    ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
                    int data12 = shortMessage.getData1();
                    int data2 = shortMessage.getData2();
                    int iWriteVarInt2 = length + writeVarInt(tick2);
                    if (status != i3) {
                        i3 = status;
                        this.tddos.writeByte(status);
                        iWriteVarInt2++;
                    }
                    this.tddos.writeByte(data12);
                    this.tddos.writeByte(data2);
                    length = iWriteVarInt2 + 1 + 1;
                    break;
                case 3:
                    SysexMessage sysexMessage = (SysexMessage) midiEvent.getMessage();
                    sysexMessage.getLength();
                    byte[] message = sysexMessage.getMessage();
                    int iWriteVarInt3 = length + writeVarInt(tick2);
                    i3 = status;
                    this.tddos.writeByte(message[0]);
                    int iWriteVarInt4 = iWriteVarInt3 + 1 + writeVarInt(message.length - 1);
                    this.tddos.write(message, 1, message.length - 1);
                    length = iWriteVarInt4 + (message.length - 1);
                    break;
                case 4:
                    MetaMessage metaMessage = (MetaMessage) midiEvent.getMessage();
                    metaMessage.getLength();
                    byte[] message2 = metaMessage.getMessage();
                    int iWriteVarInt5 = length + writeVarInt(tick2);
                    i3 = status;
                    this.tddos.write(message2, 0, message2.length);
                    length = iWriteVarInt5 + message2.length;
                    break;
                case 5:
                case 6:
                    break;
                default:
                    throw new InvalidMidiDataException("internal file writer error");
            }
        }
        dataOutputStream.writeInt(MTrk_MAGIC);
        dataOutputStream.writeInt(length);
        int i5 = length + 8;
        SequenceInputStream sequenceInputStream = new SequenceInputStream(pipedInputStream, new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        dataOutputStream.close();
        this.tddos.close();
        return sequenceInputStream;
    }
}

package com.sun.media.sound;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.Sequence;
import javax.sound.midi.spi.MidiFileReader;

/* loaded from: rt.jar:com/sun/media/sound/StandardMidiFileReader.class */
public final class StandardMidiFileReader extends MidiFileReader {
    private static final int MThd_MAGIC = 1297377380;
    private static final int bisBufferSize = 1024;

    @Override // javax.sound.midi.spi.MidiFileReader
    public MidiFileFormat getMidiFileFormat(InputStream inputStream) throws InvalidMidiDataException, IOException {
        return getMidiFileFormatFromStream(inputStream, -1, null);
    }

    private MidiFileFormat getMidiFileFormatFromStream(InputStream inputStream, int i2, SMFParser sMFParser) throws InvalidMidiDataException, IOException {
        DataInputStream dataInputStream;
        float f2;
        int i3;
        if (inputStream instanceof DataInputStream) {
            dataInputStream = (DataInputStream) inputStream;
        } else {
            dataInputStream = new DataInputStream(inputStream);
        }
        if (sMFParser == null) {
            dataInputStream.mark(16);
        } else {
            sMFParser.stream = dataInputStream;
        }
        try {
            if (dataInputStream.readInt() != MThd_MAGIC) {
                throw new InvalidMidiDataException("not a valid MIDI file");
            }
            int i4 = dataInputStream.readInt() - 6;
            short s2 = dataInputStream.readShort();
            short s3 = dataInputStream.readShort();
            int i5 = dataInputStream.readShort();
            if (i5 > 0) {
                f2 = 0.0f;
                i3 = i5;
            } else {
                int i6 = (-1) * (i5 >> 8);
                switch (i6) {
                    case 24:
                        f2 = 24.0f;
                        break;
                    case 25:
                        f2 = 25.0f;
                        break;
                    case 26:
                    case 27:
                    case 28:
                    default:
                        throw new InvalidMidiDataException("Unknown frame code: " + i6);
                    case 29:
                        f2 = 29.97f;
                        break;
                    case 30:
                        f2 = 30.0f;
                        break;
                }
                i3 = (i5 & 255) == true ? 1 : 0;
            }
            if (sMFParser != null) {
                dataInputStream.skip(i4);
                sMFParser.tracks = s3;
            }
            return new MidiFileFormat(s2, f2, i3, i2, -1);
        } finally {
            if (sMFParser == null) {
                dataInputStream.reset();
            }
        }
    }

    @Override // javax.sound.midi.spi.MidiFileReader
    public MidiFileFormat getMidiFileFormat(URL url) throws InvalidMidiDataException, IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream(), 1024);
        try {
            MidiFileFormat midiFileFormat = getMidiFileFormat(bufferedInputStream);
            bufferedInputStream.close();
            return midiFileFormat;
        } catch (Throwable th) {
            bufferedInputStream.close();
            throw th;
        }
    }

    @Override // javax.sound.midi.spi.MidiFileReader
    public MidiFileFormat getMidiFileFormat(File file) throws InvalidMidiDataException, IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 1024);
        long length = file.length();
        if (length > 2147483647L) {
            length = -1;
        }
        try {
            MidiFileFormat midiFileFormatFromStream = getMidiFileFormatFromStream(bufferedInputStream, (int) length, null);
            bufferedInputStream.close();
            return midiFileFormatFromStream;
        } catch (Throwable th) {
            bufferedInputStream.close();
            throw th;
        }
    }

    @Override // javax.sound.midi.spi.MidiFileReader
    public Sequence getSequence(InputStream inputStream) throws InvalidMidiDataException, IOException {
        SMFParser sMFParser = new SMFParser();
        MidiFileFormat midiFileFormatFromStream = getMidiFileFormatFromStream(inputStream, -1, sMFParser);
        if (midiFileFormatFromStream.getType() != 0 && midiFileFormatFromStream.getType() != 1) {
            throw new InvalidMidiDataException("Invalid or unsupported file type: " + midiFileFormatFromStream.getType());
        }
        Sequence sequence = new Sequence(midiFileFormatFromStream.getDivisionType(), midiFileFormatFromStream.getResolution());
        for (int i2 = 0; i2 < sMFParser.tracks && sMFParser.nextTrack(); i2++) {
            sMFParser.readTrack(sequence.createTrack());
        }
        return sequence;
    }

    @Override // javax.sound.midi.spi.MidiFileReader
    public Sequence getSequence(URL url) throws InvalidMidiDataException, IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream(), 1024);
        try {
            Sequence sequence = getSequence(bufferedInputStream);
            bufferedInputStream.close();
            return sequence;
        } catch (Throwable th) {
            bufferedInputStream.close();
            throw th;
        }
    }

    @Override // javax.sound.midi.spi.MidiFileReader
    public Sequence getSequence(File file) throws InvalidMidiDataException, IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 1024);
        try {
            Sequence sequence = getSequence(bufferedInputStream);
            bufferedInputStream.close();
            return sequence;
        } catch (Throwable th) {
            bufferedInputStream.close();
            throw th;
        }
    }
}

package com.sun.media.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;

/* loaded from: rt.jar:com/sun/media/sound/WaveExtensibleFileReader.class */
public final class WaveExtensibleFileReader extends AudioFileReader {
    private static final String[] channelnames = {"FL", "FR", "FC", "LF", "BL", "BR", "FLC", "FLR", "BC", "SL", "SR", "TC", "TFL", "TFC", "TFR", "TBL", "TBC", "TBR"};
    private static final String[] allchannelnames = {"w1", "w2", "w3", "w4", "w5", "w6", "w7", "w8", "w9", "w10", "w11", "w12", "w13", "w14", "w15", "w16", "w17", "w18", "w19", "w20", "w21", "w22", "w23", "w24", "w25", "w26", "w27", "w28", "w29", "w30", "w31", "w32", "w33", "w34", "w35", "w36", "w37", "w38", "w39", "w40", "w41", "w42", "w43", "w44", "w45", "w46", "w47", "w48", "w49", "w50", "w51", "w52", "w53", "w54", "w55", "w56", "w57", "w58", "w59", "w60", "w61", "w62", "w63", "w64"};
    private static final GUID SUBTYPE_PCM = new GUID(1, 0, 16, 128, 0, 0, 170, 0, 56, 155, 113);
    private static final GUID SUBTYPE_IEEE_FLOAT = new GUID(3, 0, 16, 128, 0, 0, 170, 0, 56, 155, 113);

    /* loaded from: rt.jar:com/sun/media/sound/WaveExtensibleFileReader$GUID.class */
    private static class GUID {
        long i1;
        int s1;
        int s2;
        int x1;
        int x2;
        int x3;
        int x4;
        int x5;
        int x6;
        int x7;
        int x8;

        private GUID() {
        }

        GUID(long j2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
            this.i1 = j2;
            this.s1 = i2;
            this.s2 = i3;
            this.x1 = i4;
            this.x2 = i5;
            this.x3 = i6;
            this.x4 = i7;
            this.x5 = i8;
            this.x6 = i9;
            this.x7 = i10;
            this.x8 = i11;
        }

        public static GUID read(RIFFReader rIFFReader) throws IOException {
            GUID guid = new GUID();
            guid.i1 = rIFFReader.readUnsignedInt();
            guid.s1 = rIFFReader.readUnsignedShort();
            guid.s2 = rIFFReader.readUnsignedShort();
            guid.x1 = rIFFReader.readUnsignedByte();
            guid.x2 = rIFFReader.readUnsignedByte();
            guid.x3 = rIFFReader.readUnsignedByte();
            guid.x4 = rIFFReader.readUnsignedByte();
            guid.x5 = rIFFReader.readUnsignedByte();
            guid.x6 = rIFFReader.readUnsignedByte();
            guid.x7 = rIFFReader.readUnsignedByte();
            guid.x8 = rIFFReader.readUnsignedByte();
            return guid;
        }

        public int hashCode() {
            return (int) this.i1;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof GUID)) {
                return false;
            }
            GUID guid = (GUID) obj;
            if (this.i1 != guid.i1 || this.s1 != guid.s1 || this.s2 != guid.s2 || this.x1 != guid.x1 || this.x2 != guid.x2 || this.x3 != guid.x3 || this.x4 != guid.x4 || this.x5 != guid.x5 || this.x6 != guid.x6 || this.x7 != guid.x7 || this.x8 != guid.x8) {
                return false;
            }
            return true;
        }
    }

    private String decodeChannelMask(long j2) {
        StringBuffer stringBuffer = new StringBuffer();
        long j3 = 1;
        for (int i2 = 0; i2 < allchannelnames.length; i2++) {
            if ((j2 & j3) != 0) {
                if (i2 < channelnames.length) {
                    stringBuffer.append(channelnames[i2] + " ");
                } else {
                    stringBuffer.append(allchannelnames[i2] + " ");
                }
            }
            j3 *= 2;
        }
        if (stringBuffer.length() == 0) {
            return null;
        }
        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        inputStream.mark(200);
        try {
            return internal_getAudioFileFormat(inputStream);
        } finally {
            inputStream.reset();
        }
    }

    private AudioFileFormat internal_getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        AudioFormat audioFormat;
        RIFFReader rIFFReader = new RIFFReader(inputStream);
        if (!rIFFReader.getFormat().equals("RIFF")) {
            throw new UnsupportedAudioFileException();
        }
        if (!rIFFReader.getType().equals("WAVE")) {
            throw new UnsupportedAudioFileException();
        }
        boolean z2 = false;
        boolean z3 = false;
        int unsignedShort = 1;
        long unsignedInt = 1;
        int unsignedShort2 = 1;
        int unsignedShort3 = 1;
        int unsignedShort4 = 1;
        long unsignedInt2 = 0;
        GUID guid = null;
        while (true) {
            if (!rIFFReader.hasNextChunk()) {
                break;
            }
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            if (rIFFReaderNextChunk.getFormat().equals("fmt ")) {
                z2 = true;
                if (rIFFReaderNextChunk.readUnsignedShort() != 65534) {
                    throw new UnsupportedAudioFileException();
                }
                unsignedShort = rIFFReaderNextChunk.readUnsignedShort();
                unsignedInt = rIFFReaderNextChunk.readUnsignedInt();
                rIFFReaderNextChunk.readUnsignedInt();
                unsignedShort2 = rIFFReaderNextChunk.readUnsignedShort();
                unsignedShort3 = rIFFReaderNextChunk.readUnsignedShort();
                if (rIFFReaderNextChunk.readUnsignedShort() != 22) {
                    throw new UnsupportedAudioFileException();
                }
                unsignedShort4 = rIFFReaderNextChunk.readUnsignedShort();
                if (unsignedShort4 > unsignedShort3) {
                    throw new UnsupportedAudioFileException();
                }
                unsignedInt2 = rIFFReaderNextChunk.readUnsignedInt();
                guid = GUID.read(rIFFReaderNextChunk);
            }
            if (rIFFReaderNextChunk.getFormat().equals("data")) {
                z3 = true;
                break;
            }
        }
        if (!z2) {
            throw new UnsupportedAudioFileException();
        }
        if (!z3) {
            throw new UnsupportedAudioFileException();
        }
        HashMap map = new HashMap();
        String strDecodeChannelMask = decodeChannelMask(unsignedInt2);
        if (strDecodeChannelMask != null) {
            map.put("channelOrder", strDecodeChannelMask);
        }
        if (unsignedInt2 != 0) {
            map.put("channelMask", Long.valueOf(unsignedInt2));
        }
        map.put("validBitsPerSample", Integer.valueOf(unsignedShort4));
        if (guid.equals(SUBTYPE_PCM)) {
            if (unsignedShort3 == 8) {
                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, unsignedInt, unsignedShort3, unsignedShort, unsignedShort2, unsignedInt, false, map);
            } else {
                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, unsignedInt, unsignedShort3, unsignedShort, unsignedShort2, unsignedInt, false, map);
            }
        } else if (guid.equals(SUBTYPE_IEEE_FLOAT)) {
            audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, unsignedInt, unsignedShort3, unsignedShort, unsignedShort2, unsignedInt, false, map);
        } else {
            throw new UnsupportedAudioFileException();
        }
        return new AudioFileFormat(AudioFileFormat.Type.WAVE, audioFormat, -1);
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat audioFileFormat = getAudioFileFormat(inputStream);
        RIFFReader rIFFReader = new RIFFReader(inputStream);
        if (!rIFFReader.getFormat().equals("RIFF")) {
            throw new UnsupportedAudioFileException();
        }
        if (!rIFFReader.getType().equals("WAVE")) {
            throw new UnsupportedAudioFileException();
        }
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            if (rIFFReaderNextChunk.getFormat().equals("data")) {
                return new AudioInputStream(rIFFReaderNextChunk, audioFileFormat.getFormat(), rIFFReaderNextChunk.getSize());
            }
        }
        throw new UnsupportedAudioFileException();
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream inputStreamOpenStream = url.openStream();
        try {
            AudioFileFormat audioFileFormat = getAudioFileFormat(new BufferedInputStream(inputStreamOpenStream));
            inputStreamOpenStream.close();
            return audioFileFormat;
        } catch (Throwable th) {
            inputStreamOpenStream.close();
            throw th;
        }
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            AudioFileFormat audioFileFormat = getAudioFileFormat(new BufferedInputStream(fileInputStream));
            fileInputStream.close();
            return audioFileFormat;
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        return getAudioInputStream(new BufferedInputStream(url.openStream()));
    }

    @Override // javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        return getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
    }
}

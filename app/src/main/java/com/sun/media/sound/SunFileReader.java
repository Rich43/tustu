package com.sun.media.sound;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/media/sound/SunFileReader.class */
abstract class SunFileReader extends AudioFileReader {
    protected static final int bisBufferSize = 4096;

    @Override // javax.sound.sampled.spi.AudioFileReader
    public abstract AudioFileFormat getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException;

    @Override // javax.sound.sampled.spi.AudioFileReader
    public abstract AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException;

    @Override // javax.sound.sampled.spi.AudioFileReader
    public abstract AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException;

    @Override // javax.sound.sampled.spi.AudioFileReader
    public abstract AudioInputStream getAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException;

    @Override // javax.sound.sampled.spi.AudioFileReader
    public abstract AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException;

    @Override // javax.sound.sampled.spi.AudioFileReader
    public abstract AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException;

    SunFileReader() {
    }

    final int rllong(DataInputStream dataInputStream) throws IOException {
        int i2 = dataInputStream.readInt();
        int i3 = (i2 & 255) << 24;
        int i4 = (i2 & NormalizerImpl.CC_MASK) << 8;
        int i5 = (i2 & 16711680) >> 8;
        return i3 | i4 | i5 | ((i2 & (-16777216)) >>> 24);
    }

    final int big2little(int i2) {
        return ((i2 & 255) << 24) | ((i2 & NormalizerImpl.CC_MASK) << 8) | ((i2 & 16711680) >> 8) | ((i2 & (-16777216)) >>> 24);
    }

    final short rlshort(DataInputStream dataInputStream) throws IOException {
        short s2 = dataInputStream.readShort();
        return (short) (((short) ((s2 & 255) << 8)) | ((short) ((s2 & 65280) >>> 8)));
    }

    final short big2littleShort(short s2) {
        return (short) (((short) ((s2 & 255) << 8)) | ((short) ((s2 & 65280) >>> 8)));
    }

    static final int calculatePCMFrameSize(int i2, int i3) {
        return ((i2 + 7) / 8) * i3;
    }
}

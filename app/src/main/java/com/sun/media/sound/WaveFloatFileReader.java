package com.sun.media.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;

/* loaded from: rt.jar:com/sun/media/sound/WaveFloatFileReader.class */
public final class WaveFloatFileReader extends AudioFileReader {
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
        while (true) {
            if (!rIFFReader.hasNextChunk()) {
                break;
            }
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            if (rIFFReaderNextChunk.getFormat().equals("fmt ")) {
                z2 = true;
                if (rIFFReaderNextChunk.readUnsignedShort() != 3) {
                    throw new UnsupportedAudioFileException();
                }
                unsignedShort = rIFFReaderNextChunk.readUnsignedShort();
                unsignedInt = rIFFReaderNextChunk.readUnsignedInt();
                rIFFReaderNextChunk.readUnsignedInt();
                unsignedShort2 = rIFFReaderNextChunk.readUnsignedShort();
                if (unsignedShort2 == 0) {
                    throw new UnsupportedAudioFileException("Can not process audio format with 0 frame size");
                }
                unsignedShort3 = rIFFReaderNextChunk.readUnsignedShort();
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
        return new AudioFileFormat(AudioFileFormat.Type.WAVE, new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, unsignedInt, unsignedShort3, unsignedShort, unsignedShort2, unsignedInt, false), -1);
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

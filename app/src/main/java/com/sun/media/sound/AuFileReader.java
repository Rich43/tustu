package com.sun.media.sound;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: rt.jar:com/sun/media/sound/AuFileReader.class */
public final class AuFileReader extends SunFileReader {
    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        AudioFormat.Encoding encoding;
        int i2;
        int i3;
        boolean z2 = false;
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        dataInputStream.mark(28);
        int i4 = dataInputStream.readInt();
        if (i4 != 779316836 || i4 == 779314176 || i4 == 1684960046 || i4 == 6583086) {
            dataInputStream.reset();
            throw new UnsupportedAudioFileException("not an AU file");
        }
        if (i4 == 779316836 || i4 == 779314176) {
            z2 = true;
        }
        int i5 = z2 ? dataInputStream.readInt() : rllong(dataInputStream);
        int i6 = 0 + 4;
        int i7 = z2 ? dataInputStream.readInt() : rllong(dataInputStream);
        int i8 = i6 + 4;
        int i9 = z2 ? dataInputStream.readInt() : rllong(dataInputStream);
        int i10 = i8 + 4;
        int i11 = z2 ? dataInputStream.readInt() : rllong(dataInputStream);
        int i12 = i10 + 4;
        int i13 = z2 ? dataInputStream.readInt() : rllong(dataInputStream);
        int i14 = i12 + 4;
        if (i13 <= 0) {
            dataInputStream.reset();
            throw new UnsupportedAudioFileException("Invalid number of channels");
        }
        switch (i9) {
            case 1:
                encoding = AudioFormat.Encoding.ULAW;
                i2 = 8;
                break;
            case 2:
                encoding = AudioFormat.Encoding.PCM_SIGNED;
                i2 = 8;
                break;
            case 3:
                encoding = AudioFormat.Encoding.PCM_SIGNED;
                i2 = 16;
                break;
            case 4:
                encoding = AudioFormat.Encoding.PCM_SIGNED;
                i2 = 24;
                break;
            case 5:
                encoding = AudioFormat.Encoding.PCM_SIGNED;
                i2 = 32;
                break;
            case 27:
                encoding = AudioFormat.Encoding.ALAW;
                i2 = 8;
                break;
            default:
                dataInputStream.reset();
                throw new UnsupportedAudioFileException("not a valid AU file");
        }
        int iCalculatePCMFrameSize = calculatePCMFrameSize(i2, i13);
        if (i7 < 0) {
            i3 = -1;
        } else {
            i3 = i7 / iCalculatePCMFrameSize;
        }
        AuFileFormat auFileFormat = new AuFileFormat(AudioFileFormat.Type.AU, i7 + i5, new AudioFormat(encoding, i11, i2, i13, iCalculatePCMFrameSize, i11, z2), i3);
        dataInputStream.reset();
        return auFileFormat;
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream inputStreamOpenStream = url.openStream();
        try {
            AudioFileFormat audioFileFormat = getAudioFileFormat(new BufferedInputStream(inputStreamOpenStream, 4096));
            inputStreamOpenStream.close();
            return audioFileFormat;
        } catch (Throwable th) {
            inputStreamOpenStream.close();
            throw th;
        }
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            AudioFileFormat audioFileFormat = getAudioFileFormat(new BufferedInputStream(fileInputStream, 4096));
            fileInputStream.close();
            return audioFileFormat;
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        AudioFormat format = getAudioFileFormat(inputStream).getFormat();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        dataInputStream.readInt();
        dataInputStream.skipBytes((format.isBigEndian() ? dataInputStream.readInt() : rllong(dataInputStream)) - 8);
        return new AudioInputStream(dataInputStream, format, r0.getFrameLength());
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream inputStreamOpenStream = url.openStream();
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = getAudioInputStream(new BufferedInputStream(inputStreamOpenStream, 4096));
            if (audioInputStream == null) {
                inputStreamOpenStream.close();
            }
            return audioInputStream;
        } catch (Throwable th) {
            if (audioInputStream == null) {
                inputStreamOpenStream.close();
            }
            throw th;
        }
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = getAudioInputStream(new BufferedInputStream(fileInputStream, 4096));
            if (audioInputStream == null) {
                fileInputStream.close();
            }
            return audioInputStream;
        } catch (Throwable th) {
            if (audioInputStream == null) {
                fileInputStream.close();
            }
            throw th;
        }
    }
}

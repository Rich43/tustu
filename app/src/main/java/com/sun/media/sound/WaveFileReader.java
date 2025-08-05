package com.sun.media.sound;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: rt.jar:com/sun/media/sound/WaveFileReader.class */
public final class WaveFileReader extends SunFileReader {
    private static final int MAX_READ_LENGTH = 12;

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat fmt = getFMT(inputStream, true);
        inputStream.reset();
        return fmt;
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream inputStreamOpenStream = url.openStream();
        try {
            AudioFileFormat fmt = getFMT(inputStreamOpenStream, false);
            inputStreamOpenStream.close();
            return fmt;
        } catch (Throwable th) {
            inputStreamOpenStream.close();
            throw th;
        }
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            AudioFileFormat fmt = getFMT(fileInputStream, false);
            fileInputStream.close();
            return fmt;
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        return new AudioInputStream(inputStream, getFMT(inputStream, true).getFormat(), r0.getFrameLength());
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream inputStreamOpenStream = url.openStream();
        AudioFileFormat fmt = null;
        try {
            fmt = getFMT(inputStreamOpenStream, false);
            if (fmt == null) {
                inputStreamOpenStream.close();
            }
            return new AudioInputStream(inputStreamOpenStream, fmt.getFormat(), fmt.getFrameLength());
        } catch (Throwable th) {
            if (fmt == null) {
                inputStreamOpenStream.close();
            }
            throw th;
        }
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        AudioFileFormat fmt = null;
        try {
            fmt = getFMT(fileInputStream, false);
            if (fmt == null) {
                fileInputStream.close();
            }
            return new AudioInputStream(fileInputStream, fmt.getFormat(), fmt.getFrameLength());
        } catch (Throwable th) {
            if (fmt == null) {
                fileInputStream.close();
            }
            throw th;
        }
    }

    private AudioFileFormat getFMT(InputStream inputStream, boolean z2) throws UnsupportedAudioFileException, IOException {
        int i2;
        int i3;
        AudioFormat.Encoding encoding;
        int iSkipBytes = 0;
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        if (z2) {
            dataInputStream.mark(12);
        }
        int i4 = dataInputStream.readInt();
        int iRllong = rllong(dataInputStream);
        int i5 = dataInputStream.readInt();
        if (iRllong <= 0) {
            i2 = -1;
        } else {
            i2 = iRllong + 8;
        }
        if (i4 != 1380533830 || i5 != 1463899717) {
            if (z2) {
                dataInputStream.reset();
            }
            throw new UnsupportedAudioFileException("not a WAVE file");
        }
        while (true) {
            try {
                i3 = iSkipBytes + 4;
                if (dataInputStream.readInt() == 1718449184) {
                    break;
                }
                int iRllong2 = rllong(dataInputStream);
                int i6 = i3 + 4;
                if (iRllong2 % 2 > 0) {
                    iRllong2++;
                }
                iSkipBytes = i6 + dataInputStream.skipBytes(iRllong2);
            } catch (EOFException e2) {
                throw new UnsupportedAudioFileException("Not a valid WAV file");
            }
        }
        int iRllong3 = rllong(dataInputStream);
        int i7 = i3 + 4;
        int i8 = i7 + iRllong3;
        short sRlshort = rlshort(dataInputStream);
        int i9 = i7 + 2;
        if (sRlshort == 1) {
            encoding = AudioFormat.Encoding.PCM_SIGNED;
        } else if (sRlshort == 6) {
            encoding = AudioFormat.Encoding.ALAW;
        } else if (sRlshort == 7) {
            encoding = AudioFormat.Encoding.ULAW;
        } else {
            throw new UnsupportedAudioFileException("Not a supported WAV file");
        }
        short sRlshort2 = rlshort(dataInputStream);
        int i10 = i9 + 2;
        if (sRlshort2 <= 0) {
            throw new UnsupportedAudioFileException("Invalid number of channels");
        }
        long jRllong = rllong(dataInputStream);
        rllong(dataInputStream);
        rlshort(dataInputStream);
        short sRlshort3 = rlshort(dataInputStream);
        int i11 = i10 + 4 + 4 + 2 + 2;
        if (sRlshort3 <= 0) {
            throw new UnsupportedAudioFileException("Invalid bitsPerSample");
        }
        if (sRlshort3 == 8 && encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            encoding = AudioFormat.Encoding.PCM_UNSIGNED;
        }
        if (iRllong3 % 2 != 0) {
            int i12 = iRllong3 + 1;
        }
        if (i8 > i11) {
            int iSkipBytes2 = i11 + dataInputStream.skipBytes(i8 - i11);
        }
        int iSkipBytes3 = 0;
        while (true) {
            try {
                int i13 = iSkipBytes3 + 4;
                if (dataInputStream.readInt() != 1684108385) {
                    int iRllong4 = rllong(dataInputStream);
                    int i14 = i13 + 4;
                    if (iRllong4 % 2 > 0) {
                        iRllong4++;
                    }
                    iSkipBytes3 = i14 + dataInputStream.skipBytes(iRllong4);
                } else {
                    int iRllong5 = rllong(dataInputStream);
                    int i15 = i13 + 4;
                    AudioFormat audioFormat = new AudioFormat(encoding, jRllong, sRlshort3, sRlshort2, calculatePCMFrameSize(sRlshort3, sRlshort2), jRllong, false);
                    return new WaveFileFormat(AudioFileFormat.Type.WAVE, i2, audioFormat, iRllong5 / audioFormat.getFrameSize());
                }
            } catch (EOFException e3) {
                throw new UnsupportedAudioFileException("Not a valid WAV file");
            }
        }
    }
}

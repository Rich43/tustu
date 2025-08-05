package com.sun.media.sound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: rt.jar:com/sun/media/sound/AiffFileReader.class */
public final class AiffFileReader extends SunFileReader {
    private static final int MAX_READ_LENGTH = 8;

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat comm = getCOMM(inputStream, true);
        inputStream.reset();
        return comm;
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream inputStreamOpenStream = url.openStream();
        try {
            AudioFileFormat comm = getCOMM(inputStreamOpenStream, false);
            inputStreamOpenStream.close();
            return comm;
        } catch (Throwable th) {
            inputStreamOpenStream.close();
            throw th;
        }
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            AudioFileFormat comm = getCOMM(fileInputStream, false);
            fileInputStream.close();
            return comm;
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        return new AudioInputStream(inputStream, getCOMM(inputStream, true).getFormat(), r0.getFrameLength());
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        InputStream inputStreamOpenStream = url.openStream();
        AudioFileFormat comm = null;
        try {
            comm = getCOMM(inputStreamOpenStream, false);
            if (comm == null) {
                inputStreamOpenStream.close();
            }
            return new AudioInputStream(inputStreamOpenStream, comm.getFormat(), comm.getFrameLength());
        } catch (Throwable th) {
            if (comm == null) {
                inputStreamOpenStream.close();
            }
            throw th;
        }
    }

    @Override // com.sun.media.sound.SunFileReader, javax.sound.sampled.spi.AudioFileReader
    public AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        AudioFileFormat comm = null;
        try {
            comm = getCOMM(fileInputStream, false);
            if (comm == null) {
                fileInputStream.close();
            }
            return new AudioInputStream(fileInputStream, comm.getFormat(), comm.getFrameLength());
        } catch (Throwable th) {
            if (comm == null) {
                fileInputStream.close();
            }
            throw th;
        }
    }

    private AudioFileFormat getCOMM(InputStream inputStream, boolean z2) throws UnsupportedAudioFileException, IOException {
        int i2;
        int i3;
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        if (z2) {
            dataInputStream.mark(8);
        }
        int i4 = 0;
        AudioFormat audioFormat = null;
        if (dataInputStream.readInt() != 1179603533) {
            if (z2) {
                dataInputStream.reset();
            }
            throw new UnsupportedAudioFileException("not an AIFF file");
        }
        int i5 = dataInputStream.readInt();
        int i6 = dataInputStream.readInt();
        int iSkipBytes = 0 + 12;
        if (i5 <= 0) {
            i5 = -1;
            i2 = -1;
        } else {
            i2 = i5 + 8;
        }
        boolean z3 = false;
        if (i6 == 1095321155) {
            z3 = true;
        }
        boolean z4 = false;
        while (!z4) {
            int i7 = dataInputStream.readInt();
            int i8 = dataInputStream.readInt();
            int i9 = iSkipBytes + 8;
            int i10 = 0;
            switch (i7) {
                case 1129270605:
                    if ((!z3 && i8 < 18) || (z3 && i8 < 22)) {
                        throw new UnsupportedAudioFileException("Invalid AIFF/COMM chunksize");
                    }
                    int unsignedShort = dataInputStream.readUnsignedShort();
                    if (unsignedShort <= 0) {
                        throw new UnsupportedAudioFileException("Invalid number of channels");
                    }
                    dataInputStream.readInt();
                    int unsignedShort2 = dataInputStream.readUnsignedShort();
                    if (unsignedShort2 < 1 || unsignedShort2 > 32) {
                        throw new UnsupportedAudioFileException("Invalid AIFF/COMM sampleSize");
                    }
                    float f2 = (float) read_ieee_extended(dataInputStream);
                    i10 = 0 + 18;
                    AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
                    if (z3) {
                        i10 += 4;
                        switch (dataInputStream.readInt()) {
                            case 1313820229:
                                encoding = AudioFormat.Encoding.PCM_SIGNED;
                                break;
                            case 1970037111:
                                encoding = AudioFormat.Encoding.ULAW;
                                unsignedShort2 = 8;
                                break;
                            default:
                                throw new UnsupportedAudioFileException("Invalid AIFF encoding");
                        }
                    }
                    audioFormat = new AudioFormat(encoding, f2, unsignedShort2, unsignedShort, calculatePCMFrameSize(unsignedShort2, unsignedShort), f2, true);
                    break;
                    break;
                case 1397968452:
                    dataInputStream.readInt();
                    dataInputStream.readInt();
                    i10 = 0 + 8;
                    if (i8 < i5) {
                        i4 = i8 - i10;
                    } else {
                        i4 = i5 - (i9 + i10);
                    }
                    z4 = true;
                    break;
            }
            iSkipBytes = i9 + i10;
            if (!z4 && (i3 = i8 - i10) > 0) {
                iSkipBytes += dataInputStream.skipBytes(i3);
            }
        }
        if (audioFormat == null) {
            throw new UnsupportedAudioFileException("missing COMM chunk");
        }
        return new AiffFileFormat(z3 ? AudioFileFormat.Type.AIFC : AudioFileFormat.Type.AIFF, i2, audioFormat, i4 / audioFormat.getFrameSize());
    }

    private void write_ieee_extended(DataOutputStream dataOutputStream, double d2) throws IOException {
        int i2 = 16398;
        double d3 = d2;
        while (d3 < 44000.0d) {
            d3 *= 2.0d;
            i2--;
        }
        dataOutputStream.writeShort(i2);
        dataOutputStream.writeInt(((int) d3) << 16);
        dataOutputStream.writeInt(0);
    }

    private double read_ieee_extended(DataInputStream dataInputStream) throws IOException {
        double dPow;
        int unsignedShort = dataInputStream.readUnsignedShort();
        long unsignedShort2 = (dataInputStream.readUnsignedShort() << 16) | dataInputStream.readUnsignedShort();
        long unsignedShort3 = (dataInputStream.readUnsignedShort() << 16) | dataInputStream.readUnsignedShort();
        if (unsignedShort == 0 && unsignedShort2 == 0 && unsignedShort3 == 0) {
            dPow = 0.0d;
        } else if (unsignedShort == 32767) {
            dPow = 3.4028234663852886E38d;
        } else {
            dPow = (unsignedShort2 * Math.pow(2.0d, (unsignedShort - 16383) - 31)) + (unsignedShort3 * Math.pow(2.0d, r13 - 32));
        }
        return dPow;
    }
}

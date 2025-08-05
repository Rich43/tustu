package com.sun.media.sound;

import com.sun.media.sound.SunFileWriter;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/media/sound/AiffFileWriter.class */
public final class AiffFileWriter extends SunFileWriter {
    private static final int DOUBLE_MANTISSA_LENGTH = 52;
    private static final int DOUBLE_EXPONENT_LENGTH = 11;
    private static final long DOUBLE_SIGN_MASK = Long.MIN_VALUE;
    private static final long DOUBLE_EXPONENT_MASK = 9218868437227405312L;
    private static final long DOUBLE_MANTISSA_MASK = 4503599627370495L;
    private static final int DOUBLE_EXPONENT_OFFSET = 1023;
    private static final int EXTENDED_EXPONENT_OFFSET = 16383;
    private static final int EXTENDED_MANTISSA_LENGTH = 63;
    private static final int EXTENDED_EXPONENT_LENGTH = 15;
    private static final long EXTENDED_INTEGER_MASK = Long.MIN_VALUE;

    public AiffFileWriter() {
        super(new AudioFileFormat.Type[]{AudioFileFormat.Type.AIFF});
    }

    @Override // com.sun.media.sound.SunFileWriter, javax.sound.sampled.spi.AudioFileWriter
    public AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream audioInputStream) {
        AudioFileFormat.Type[] typeArr = new AudioFileFormat.Type[this.types.length];
        System.arraycopy(this.types, 0, typeArr, 0, this.types.length);
        AudioFormat.Encoding encoding = audioInputStream.getFormat().getEncoding();
        if (AudioFormat.Encoding.ALAW.equals(encoding) || AudioFormat.Encoding.ULAW.equals(encoding) || AudioFormat.Encoding.PCM_SIGNED.equals(encoding) || AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding)) {
            return typeArr;
        }
        return new AudioFileFormat.Type[0];
    }

    @Override // com.sun.media.sound.SunFileWriter, javax.sound.sampled.spi.AudioFileWriter
    public int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, OutputStream outputStream) throws IOException {
        AiffFileFormat aiffFileFormat = (AiffFileFormat) getAudioFileFormat(type, audioInputStream);
        if (audioInputStream.getFrameLength() == -1) {
            throw new IOException("stream length not specified");
        }
        return writeAiffFile(audioInputStream, aiffFileFormat, outputStream);
    }

    @Override // com.sun.media.sound.SunFileWriter, javax.sound.sampled.spi.AudioFileWriter
    public int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, File file) throws IOException {
        AiffFileFormat aiffFileFormat = (AiffFileFormat) getAudioFileFormat(type, audioInputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), 4096);
        int iWriteAiffFile = writeAiffFile(audioInputStream, aiffFileFormat, bufferedOutputStream);
        bufferedOutputStream.close();
        if (aiffFileFormat.getByteLength() == -1) {
            int channels = aiffFileFormat.getFormat().getChannels() * aiffFileFormat.getFormat().getSampleSizeInBits();
            int headerSize = (iWriteAiffFile - aiffFileFormat.getHeaderSize()) + 16;
            int i2 = (int) (((headerSize - 16) * 8) / channels);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
            randomAccessFile.skipBytes(4);
            randomAccessFile.writeInt(iWriteAiffFile - 8);
            randomAccessFile.skipBytes(4 + aiffFileFormat.getFverChunkSize() + 4 + 4 + 2);
            randomAccessFile.writeInt(i2);
            randomAccessFile.skipBytes(16);
            randomAccessFile.writeInt(headerSize - 8);
            randomAccessFile.close();
        }
        return iWriteAiffFile;
    }

    private AudioFileFormat getAudioFileFormat(AudioFileFormat.Type type, AudioInputStream audioInputStream) {
        AudioFormat.Encoding encoding;
        int sampleSizeInBits;
        int frameLength;
        AudioFormat.Encoding encoding2 = AudioFormat.Encoding.PCM_SIGNED;
        AudioFormat format = audioInputStream.getFormat();
        AudioFormat.Encoding encoding3 = format.getEncoding();
        boolean z2 = false;
        if (!this.types[0].equals(type)) {
            throw new IllegalArgumentException("File type " + ((Object) type) + " not supported.");
        }
        if (AudioFormat.Encoding.ALAW.equals(encoding3) || AudioFormat.Encoding.ULAW.equals(encoding3)) {
            if (format.getSampleSizeInBits() == 8) {
                encoding = AudioFormat.Encoding.PCM_SIGNED;
                sampleSizeInBits = 16;
                z2 = true;
            } else {
                throw new IllegalArgumentException("Encoding " + ((Object) encoding3) + " supported only for 8-bit data.");
            }
        } else if (format.getSampleSizeInBits() == 8) {
            encoding = AudioFormat.Encoding.PCM_UNSIGNED;
            sampleSizeInBits = 8;
        } else {
            encoding = AudioFormat.Encoding.PCM_SIGNED;
            sampleSizeInBits = format.getSampleSizeInBits();
        }
        AudioFormat audioFormat = new AudioFormat(encoding, format.getSampleRate(), sampleSizeInBits, format.getChannels(), format.getFrameSize(), format.getFrameRate(), true);
        if (audioInputStream.getFrameLength() != -1) {
            if (z2) {
                frameLength = (((int) audioInputStream.getFrameLength()) * format.getFrameSize() * 2) + 54;
            } else {
                frameLength = (((int) audioInputStream.getFrameLength()) * format.getFrameSize()) + 54;
            }
        } else {
            frameLength = -1;
        }
        return new AiffFileFormat(AudioFileFormat.Type.AIFF, frameLength, audioFormat, (int) audioInputStream.getFrameLength());
    }

    private int writeAiffFile(InputStream inputStream, AiffFileFormat aiffFileFormat, OutputStream outputStream) throws IOException {
        int i2 = 0;
        InputStream fileStream = getFileStream(aiffFileFormat, inputStream);
        byte[] bArr = new byte[4096];
        int byteLength = aiffFileFormat.getByteLength();
        while (true) {
            int i3 = fileStream.read(bArr);
            if (i3 < 0) {
                break;
            }
            if (byteLength > 0) {
                if (i3 < byteLength) {
                    outputStream.write(bArr, 0, i3);
                    i2 += i3;
                    byteLength -= i3;
                } else {
                    outputStream.write(bArr, 0, byteLength);
                    i2 += byteLength;
                    break;
                }
            } else {
                outputStream.write(bArr, 0, i3);
                i2 += i3;
            }
        }
        return i2;
    }

    private InputStream getFileStream(AiffFileFormat aiffFileFormat, InputStream inputStream) throws IOException {
        AudioFormat format = aiffFileFormat.getFormat();
        int headerSize = aiffFileFormat.getHeaderSize();
        aiffFileFormat.getFverChunkSize();
        int commChunkSize = aiffFileFormat.getCommChunkSize();
        int i2 = -1;
        int i3 = -1;
        aiffFileFormat.getSsndChunkOffset();
        short channels = (short) format.getChannels();
        short sampleSizeInBits = (short) format.getSampleSizeInBits();
        int i4 = channels * sampleSizeInBits;
        int frameLength = aiffFileFormat.getFrameLength();
        if (frameLength != -1) {
            long j2 = (frameLength * i4) / 8;
            i3 = ((int) j2) + 16;
            i2 = ((int) j2) + headerSize;
        }
        float sampleRate = format.getSampleRate();
        InputStream audioInputStream = inputStream;
        if (inputStream instanceof AudioInputStream) {
            AudioFormat format2 = ((AudioInputStream) inputStream).getFormat();
            AudioFormat.Encoding encoding = format2.getEncoding();
            if (AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding) || (AudioFormat.Encoding.PCM_SIGNED.equals(encoding) && !format2.isBigEndian())) {
                audioInputStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format2.getSampleRate(), format2.getSampleSizeInBits(), format2.getChannels(), format2.getFrameSize(), format2.getFrameRate(), true), (AudioInputStream) inputStream);
            } else if (AudioFormat.Encoding.ULAW.equals(encoding) || AudioFormat.Encoding.ALAW.equals(encoding)) {
                if (format2.getSampleSizeInBits() != 8) {
                    throw new IllegalArgumentException("unsupported encoding");
                }
                audioInputStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format2.getSampleRate(), format2.getSampleSizeInBits() * 2, format2.getChannels(), format2.getFrameSize() * 2, format2.getFrameRate(), true), (AudioInputStream) inputStream);
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(1179603533);
        dataOutputStream.writeInt(i2 - 8);
        dataOutputStream.writeInt(1095321158);
        dataOutputStream.writeInt(1129270605);
        dataOutputStream.writeInt(commChunkSize - 8);
        dataOutputStream.writeShort(channels);
        dataOutputStream.writeInt(frameLength);
        dataOutputStream.writeShort(sampleSizeInBits);
        write_ieee_extended(dataOutputStream, sampleRate);
        dataOutputStream.writeInt(1397968452);
        dataOutputStream.writeInt(i3 - 8);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
        dataOutputStream.close();
        return new SequenceInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), new SunFileWriter.NoCloseInputStream(audioInputStream));
    }

    private void write_ieee_extended(DataOutputStream dataOutputStream, float f2) throws IOException {
        long jDoubleToLongBits = Double.doubleToLongBits(f2);
        long j2 = (jDoubleToLongBits & Long.MIN_VALUE) >> 63;
        long j3 = (jDoubleToLongBits & 9218868437227405312L) >> 52;
        long j4 = jDoubleToLongBits & 4503599627370495L;
        long j5 = (j3 - 1023) + 16383;
        dataOutputStream.writeShort((short) ((j2 << 15) | j5));
        dataOutputStream.writeLong(Long.MIN_VALUE | (j4 << 11));
    }
}

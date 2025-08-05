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

/* loaded from: rt.jar:com/sun/media/sound/AuFileWriter.class */
public final class AuFileWriter extends SunFileWriter {
    public static final int UNKNOWN_SIZE = -1;

    public AuFileWriter() {
        super(new AudioFileFormat.Type[]{AudioFileFormat.Type.AU});
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
        return writeAuFile(audioInputStream, (AuFileFormat) getAudioFileFormat(type, audioInputStream), outputStream);
    }

    @Override // com.sun.media.sound.SunFileWriter, javax.sound.sampled.spi.AudioFileWriter
    public int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, File file) throws IOException {
        AuFileFormat auFileFormat = (AuFileFormat) getAudioFileFormat(type, audioInputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), 4096);
        int iWriteAuFile = writeAuFile(audioInputStream, auFileFormat, bufferedOutputStream);
        bufferedOutputStream.close();
        if (auFileFormat.getByteLength() == -1) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
            if (randomAccessFile.length() <= 2147483647L) {
                randomAccessFile.skipBytes(8);
                randomAccessFile.writeInt(iWriteAuFile - 24);
            }
            randomAccessFile.close();
        }
        return iWriteAuFile;
    }

    private AudioFileFormat getAudioFileFormat(AudioFileFormat.Type type, AudioInputStream audioInputStream) {
        AudioFormat.Encoding encoding;
        int sampleSizeInBits;
        int frameLength;
        AudioFormat.Encoding encoding2 = AudioFormat.Encoding.PCM_SIGNED;
        AudioFormat format = audioInputStream.getFormat();
        AudioFormat.Encoding encoding3 = format.getEncoding();
        if (!this.types[0].equals(type)) {
            throw new IllegalArgumentException("File type " + ((Object) type) + " not supported.");
        }
        if (AudioFormat.Encoding.ALAW.equals(encoding3) || AudioFormat.Encoding.ULAW.equals(encoding3)) {
            encoding = encoding3;
            sampleSizeInBits = format.getSampleSizeInBits();
        } else if (format.getSampleSizeInBits() == 8) {
            encoding = AudioFormat.Encoding.PCM_SIGNED;
            sampleSizeInBits = 8;
        } else {
            encoding = AudioFormat.Encoding.PCM_SIGNED;
            sampleSizeInBits = format.getSampleSizeInBits();
        }
        AudioFormat audioFormat = new AudioFormat(encoding, format.getSampleRate(), sampleSizeInBits, format.getChannels(), format.getFrameSize(), format.getFrameRate(), true);
        if (audioInputStream.getFrameLength() != -1) {
            frameLength = (((int) audioInputStream.getFrameLength()) * format.getFrameSize()) + 24;
        } else {
            frameLength = -1;
        }
        return new AuFileFormat(AudioFileFormat.Type.AU, frameLength, audioFormat, (int) audioInputStream.getFrameLength());
    }

    private InputStream getFileStream(AuFileFormat auFileFormat, InputStream inputStream) throws IOException {
        AudioFormat format = auFileFormat.getFormat();
        long frameLength = auFileFormat.getFrameLength();
        long frameSize = frameLength == -1 ? -1L : frameLength * format.getFrameSize();
        if (frameSize > 2147483647L) {
            frameSize = -1;
        }
        int auType = auFileFormat.getAuType();
        int sampleRate = (int) format.getSampleRate();
        int channels = format.getChannels();
        InputStream audioInputStream = inputStream;
        if (inputStream instanceof AudioInputStream) {
            AudioFormat format2 = ((AudioInputStream) inputStream).getFormat();
            AudioFormat.Encoding encoding = format2.getEncoding();
            if (AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding) || (AudioFormat.Encoding.PCM_SIGNED.equals(encoding) && true != format2.isBigEndian())) {
                audioInputStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format2.getSampleRate(), format2.getSampleSizeInBits(), format2.getChannels(), format2.getFrameSize(), format2.getFrameRate(), true), (AudioInputStream) inputStream);
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        if (1 != 0) {
            dataOutputStream.writeInt(779316836);
            dataOutputStream.writeInt(24);
            dataOutputStream.writeInt((int) frameSize);
            dataOutputStream.writeInt(auType);
            dataOutputStream.writeInt(sampleRate);
            dataOutputStream.writeInt(channels);
        } else {
            dataOutputStream.writeInt(1684960046);
            dataOutputStream.writeInt(big2little(24));
            dataOutputStream.writeInt(big2little((int) frameSize));
            dataOutputStream.writeInt(big2little(auType));
            dataOutputStream.writeInt(big2little(sampleRate));
            dataOutputStream.writeInt(big2little(channels));
        }
        dataOutputStream.close();
        return new SequenceInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), new SunFileWriter.NoCloseInputStream(audioInputStream));
    }

    private int writeAuFile(InputStream inputStream, AuFileFormat auFileFormat, OutputStream outputStream) throws IOException {
        int i2 = 0;
        InputStream fileStream = getFileStream(auFileFormat, inputStream);
        byte[] bArr = new byte[4096];
        int byteLength = auFileFormat.getByteLength();
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
}

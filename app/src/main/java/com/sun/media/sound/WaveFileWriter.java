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

/* loaded from: rt.jar:com/sun/media/sound/WaveFileWriter.class */
public final class WaveFileWriter extends SunFileWriter {
    static final int RIFF_MAGIC = 1380533830;
    static final int WAVE_MAGIC = 1463899717;
    static final int FMT_MAGIC = 1718449184;
    static final int DATA_MAGIC = 1684108385;
    static final int WAVE_FORMAT_UNKNOWN = 0;
    static final int WAVE_FORMAT_PCM = 1;
    static final int WAVE_FORMAT_ADPCM = 2;
    static final int WAVE_FORMAT_ALAW = 6;
    static final int WAVE_FORMAT_MULAW = 7;
    static final int WAVE_FORMAT_OKI_ADPCM = 16;
    static final int WAVE_FORMAT_DIGISTD = 21;
    static final int WAVE_FORMAT_DIGIFIX = 22;
    static final int WAVE_IBM_FORMAT_MULAW = 257;
    static final int WAVE_IBM_FORMAT_ALAW = 258;
    static final int WAVE_IBM_FORMAT_ADPCM = 259;
    static final int WAVE_FORMAT_DVI_ADPCM = 17;
    static final int WAVE_FORMAT_SX7383 = 7175;

    public WaveFileWriter() {
        super(new AudioFileFormat.Type[]{AudioFileFormat.Type.WAVE});
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
        WaveFileFormat waveFileFormat = (WaveFileFormat) getAudioFileFormat(type, audioInputStream);
        if (audioInputStream.getFrameLength() == -1) {
            throw new IOException("stream length not specified");
        }
        return writeWaveFile(audioInputStream, waveFileFormat, outputStream);
    }

    @Override // com.sun.media.sound.SunFileWriter, javax.sound.sampled.spi.AudioFileWriter
    public int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, File file) throws IOException {
        WaveFileFormat waveFileFormat = (WaveFileFormat) getAudioFileFormat(type, audioInputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), 4096);
        int iWriteWaveFile = writeWaveFile(audioInputStream, waveFileFormat, bufferedOutputStream);
        bufferedOutputStream.close();
        if (waveFileFormat.getByteLength() == -1) {
            int headerSize = iWriteWaveFile - waveFileFormat.getHeaderSize();
            int headerSize2 = (headerSize + waveFileFormat.getHeaderSize()) - 8;
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
            randomAccessFile.skipBytes(4);
            randomAccessFile.writeInt(big2little(headerSize2));
            randomAccessFile.skipBytes(12 + WaveFileFormat.getFmtChunkSize(waveFileFormat.getWaveType()) + 4);
            randomAccessFile.writeInt(big2little(headerSize));
            randomAccessFile.close();
        }
        return iWriteWaveFile;
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
        int i2 = 1;
        if (AudioFormat.Encoding.ALAW.equals(encoding3) || AudioFormat.Encoding.ULAW.equals(encoding3)) {
            encoding = encoding3;
            sampleSizeInBits = format.getSampleSizeInBits();
            i2 = encoding3.equals(AudioFormat.Encoding.ALAW) ? 6 : 7;
        } else if (format.getSampleSizeInBits() == 8) {
            encoding = AudioFormat.Encoding.PCM_UNSIGNED;
            sampleSizeInBits = 8;
        } else {
            encoding = AudioFormat.Encoding.PCM_SIGNED;
            sampleSizeInBits = format.getSampleSizeInBits();
        }
        AudioFormat audioFormat = new AudioFormat(encoding, format.getSampleRate(), sampleSizeInBits, format.getChannels(), format.getFrameSize(), format.getFrameRate(), false);
        if (audioInputStream.getFrameLength() != -1) {
            frameLength = (((int) audioInputStream.getFrameLength()) * format.getFrameSize()) + WaveFileFormat.getHeaderSize(i2);
        } else {
            frameLength = -1;
        }
        return new WaveFileFormat(AudioFileFormat.Type.WAVE, frameLength, audioFormat, (int) audioInputStream.getFrameLength());
    }

    private int writeWaveFile(InputStream inputStream, WaveFileFormat waveFileFormat, OutputStream outputStream) throws IOException {
        int i2 = 0;
        InputStream fileStream = getFileStream(waveFileFormat, inputStream);
        byte[] bArr = new byte[4096];
        int byteLength = waveFileFormat.getByteLength();
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

    private InputStream getFileStream(WaveFileFormat waveFileFormat, InputStream inputStream) throws IOException {
        AudioFormat format = waveFileFormat.getFormat();
        int headerSize = waveFileFormat.getHeaderSize();
        int fmtChunkSize = WaveFileFormat.getFmtChunkSize(waveFileFormat.getWaveType());
        short waveType = (short) waveFileFormat.getWaveType();
        short channels = (short) format.getChannels();
        short sampleSizeInBits = (short) format.getSampleSizeInBits();
        int sampleRate = (int) format.getSampleRate();
        int frameSize = format.getFrameSize();
        int i2 = ((channels * sampleSizeInBits) * sampleRate) / 8;
        short s2 = (short) ((sampleSizeInBits / 8) * channels);
        int frameLength = waveFileFormat.getFrameLength() * frameSize;
        waveFileFormat.getByteLength();
        int i3 = (frameLength + headerSize) - 8;
        InputStream audioInputStream = inputStream;
        if (inputStream instanceof AudioInputStream) {
            AudioFormat format2 = ((AudioInputStream) inputStream).getFormat();
            AudioFormat.Encoding encoding = format2.getEncoding();
            if (AudioFormat.Encoding.PCM_SIGNED.equals(encoding) && sampleSizeInBits == 8) {
                waveType = 1;
                audioInputStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, format2.getSampleRate(), format2.getSampleSizeInBits(), format2.getChannels(), format2.getFrameSize(), format2.getFrameRate(), false), (AudioInputStream) inputStream);
            }
            if (((AudioFormat.Encoding.PCM_SIGNED.equals(encoding) && format2.isBigEndian()) || ((AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding) && !format2.isBigEndian()) || (AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding) && format2.isBigEndian()))) && sampleSizeInBits != 8) {
                waveType = 1;
                audioInputStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format2.getSampleRate(), format2.getSampleSizeInBits(), format2.getChannels(), format2.getFrameSize(), format2.getFrameRate(), false), (AudioInputStream) inputStream);
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(RIFF_MAGIC);
        dataOutputStream.writeInt(big2little(i3));
        dataOutputStream.writeInt(WAVE_MAGIC);
        dataOutputStream.writeInt(FMT_MAGIC);
        dataOutputStream.writeInt(big2little(fmtChunkSize));
        dataOutputStream.writeShort(big2littleShort(waveType));
        dataOutputStream.writeShort(big2littleShort(channels));
        dataOutputStream.writeInt(big2little(sampleRate));
        dataOutputStream.writeInt(big2little(i2));
        dataOutputStream.writeShort(big2littleShort(s2));
        dataOutputStream.writeShort(big2littleShort(sampleSizeInBits));
        if (waveType != 1) {
            dataOutputStream.writeShort(0);
        }
        dataOutputStream.writeInt(DATA_MAGIC);
        dataOutputStream.writeInt(big2little(frameLength));
        dataOutputStream.close();
        return new SequenceInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), new SunFileWriter.NoCloseInputStream(audioInputStream));
    }
}

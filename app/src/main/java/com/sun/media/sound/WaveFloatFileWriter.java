package com.sun.media.sound;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.spi.AudioFileWriter;

/* loaded from: rt.jar:com/sun/media/sound/WaveFloatFileWriter.class */
public final class WaveFloatFileWriter extends AudioFileWriter {
    @Override // javax.sound.sampled.spi.AudioFileWriter
    public AudioFileFormat.Type[] getAudioFileTypes() {
        return new AudioFileFormat.Type[]{AudioFileFormat.Type.WAVE};
    }

    @Override // javax.sound.sampled.spi.AudioFileWriter
    public AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream audioInputStream) {
        if (!audioInputStream.getFormat().getEncoding().equals(AudioFormat.Encoding.PCM_FLOAT)) {
            return new AudioFileFormat.Type[0];
        }
        return new AudioFileFormat.Type[]{AudioFileFormat.Type.WAVE};
    }

    private void checkFormat(AudioFileFormat.Type type, AudioInputStream audioInputStream) {
        if (!AudioFileFormat.Type.WAVE.equals(type)) {
            throw new IllegalArgumentException("File type " + ((Object) type) + " not supported.");
        }
        if (!audioInputStream.getFormat().getEncoding().equals(AudioFormat.Encoding.PCM_FLOAT)) {
            throw new IllegalArgumentException("File format " + ((Object) audioInputStream.getFormat()) + " not supported.");
        }
    }

    public void write(AudioInputStream audioInputStream, RIFFWriter rIFFWriter) throws IOException {
        RIFFWriter rIFFWriterWriteChunk = rIFFWriter.writeChunk("fmt ");
        AudioFormat format = audioInputStream.getFormat();
        rIFFWriterWriteChunk.writeUnsignedShort(3);
        rIFFWriterWriteChunk.writeUnsignedShort(format.getChannels());
        rIFFWriterWriteChunk.writeUnsignedInt((int) format.getSampleRate());
        rIFFWriterWriteChunk.writeUnsignedInt(((int) format.getFrameRate()) * format.getFrameSize());
        rIFFWriterWriteChunk.writeUnsignedShort(format.getFrameSize());
        rIFFWriterWriteChunk.writeUnsignedShort(format.getSampleSizeInBits());
        rIFFWriterWriteChunk.close();
        RIFFWriter rIFFWriterWriteChunk2 = rIFFWriter.writeChunk("data");
        byte[] bArr = new byte[1024];
        while (true) {
            int i2 = audioInputStream.read(bArr, 0, bArr.length);
            if (i2 != -1) {
                rIFFWriterWriteChunk2.write(bArr, 0, i2);
            } else {
                rIFFWriterWriteChunk2.close();
                return;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/WaveFloatFileWriter$NoCloseOutputStream.class */
    private static class NoCloseOutputStream extends OutputStream {
        final OutputStream out;

        NoCloseOutputStream(OutputStream outputStream) {
            this.out = outputStream;
        }

        @Override // java.io.OutputStream
        public void write(int i2) throws IOException {
            this.out.write(i2);
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            this.out.flush();
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            this.out.write(bArr, i2, i3);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr) throws IOException {
            this.out.write(bArr);
        }
    }

    private AudioInputStream toLittleEndian(AudioInputStream audioInputStream) {
        AudioFormat format = audioInputStream.getFormat();
        return AudioSystem.getAudioInputStream(new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), false), audioInputStream);
    }

    @Override // javax.sound.sampled.spi.AudioFileWriter
    public int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, OutputStream outputStream) throws IOException {
        checkFormat(type, audioInputStream);
        if (audioInputStream.getFormat().isBigEndian()) {
            audioInputStream = toLittleEndian(audioInputStream);
        }
        RIFFWriter rIFFWriter = new RIFFWriter(new NoCloseOutputStream(outputStream), "WAVE");
        write(audioInputStream, rIFFWriter);
        int filePointer = (int) rIFFWriter.getFilePointer();
        rIFFWriter.close();
        return filePointer;
    }

    @Override // javax.sound.sampled.spi.AudioFileWriter
    public int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, File file) throws IOException {
        checkFormat(type, audioInputStream);
        if (audioInputStream.getFormat().isBigEndian()) {
            audioInputStream = toLittleEndian(audioInputStream);
        }
        RIFFWriter rIFFWriter = new RIFFWriter(file, "WAVE");
        write(audioInputStream, rIFFWriter);
        int filePointer = (int) rIFFWriter.getFilePointer();
        rIFFWriter.close();
        return filePointer;
    }
}

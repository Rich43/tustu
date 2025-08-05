package com.sun.media.sound;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.spi.FormatConversionProvider;
import sun.security.x509.IssuingDistributionPointExtension;

/* loaded from: rt.jar:com/sun/media/sound/AudioFloatFormatConverter.class */
public final class AudioFloatFormatConverter extends FormatConversionProvider {
    private final AudioFormat.Encoding[] formats = {AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED, AudioFormat.Encoding.PCM_FLOAT};

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatFormatConverter$AudioFloatFormatConverterInputStream.class */
    private static class AudioFloatFormatConverterInputStream extends InputStream {
        private final AudioFloatConverter converter;
        private final AudioFloatInputStream stream;
        private float[] readfloatbuffer;
        private final int fsize;

        AudioFloatFormatConverterInputStream(AudioFormat audioFormat, AudioFloatInputStream audioFloatInputStream) {
            this.stream = audioFloatInputStream;
            this.converter = AudioFloatConverter.getConverter(audioFormat);
            this.fsize = (audioFormat.getSampleSizeInBits() + 7) / 8;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            byte[] bArr = new byte[1];
            int i2 = read(bArr);
            if (i2 < 0) {
                return i2;
            }
            return bArr[0] & 255;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            int i4 = i3 / this.fsize;
            if (this.readfloatbuffer == null || this.readfloatbuffer.length < i4) {
                this.readfloatbuffer = new float[i4];
            }
            int i5 = this.stream.read(this.readfloatbuffer, 0, i4);
            if (i5 < 0) {
                return i5;
            }
            this.converter.toByteArray(this.readfloatbuffer, 0, i5, bArr, i2);
            return i5 * this.fsize;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            int iAvailable = this.stream.available();
            if (iAvailable < 0) {
                return iAvailable;
            }
            return iAvailable * this.fsize;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.stream.close();
        }

        @Override // java.io.InputStream
        public synchronized void mark(int i2) {
            this.stream.mark(i2 * this.fsize);
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return this.stream.markSupported();
        }

        @Override // java.io.InputStream
        public synchronized void reset() throws IOException {
            this.stream.reset();
        }

        @Override // java.io.InputStream
        public long skip(long j2) throws IOException {
            long jSkip = this.stream.skip(j2 / this.fsize);
            if (jSkip < 0) {
                return jSkip;
            }
            return jSkip * this.fsize;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatFormatConverter$AudioFloatInputStreamChannelMixer.class */
    private static class AudioFloatInputStreamChannelMixer extends AudioFloatInputStream {
        private final int targetChannels;
        private final int sourceChannels;
        private final AudioFloatInputStream ais;
        private final AudioFormat targetFormat;
        private float[] conversion_buffer;

        AudioFloatInputStreamChannelMixer(AudioFloatInputStream audioFloatInputStream, int i2) {
            this.sourceChannels = audioFloatInputStream.getFormat().getChannels();
            this.targetChannels = i2;
            this.ais = audioFloatInputStream;
            AudioFormat format = audioFloatInputStream.getFormat();
            this.targetFormat = new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(), i2, (format.getFrameSize() / this.sourceChannels) * i2, format.getFrameRate(), format.isBigEndian());
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public int available() throws IOException {
            return (this.ais.available() / this.sourceChannels) * this.targetChannels;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void close() throws IOException {
            this.ais.close();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public AudioFormat getFormat() {
            return this.targetFormat;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long getFrameLength() {
            return this.ais.getFrameLength();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void mark(int i2) {
            this.ais.mark((i2 / this.targetChannels) * this.sourceChannels);
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public boolean markSupported() {
            return this.ais.markSupported();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public int read(float[] fArr, int i2, int i3) throws IOException {
            int i4 = (i3 / this.targetChannels) * this.sourceChannels;
            if (this.conversion_buffer == null || this.conversion_buffer.length < i4) {
                this.conversion_buffer = new float[i4];
            }
            int i5 = this.ais.read(this.conversion_buffer, 0, i4);
            if (i5 < 0) {
                return i5;
            }
            if (this.sourceChannels == 1) {
                int i6 = this.targetChannels;
                for (int i7 = 0; i7 < this.targetChannels; i7++) {
                    int i8 = 0;
                    int i9 = i2;
                    int i10 = i7;
                    while (true) {
                        int i11 = i9 + i10;
                        if (i8 < i4) {
                            fArr[i11] = this.conversion_buffer[i8];
                            i8++;
                            i9 = i11;
                            i10 = i6;
                        }
                    }
                }
            } else if (this.targetChannels == 1) {
                int i12 = this.sourceChannels;
                int i13 = 0;
                int i14 = i2;
                while (i13 < i4) {
                    fArr[i14] = this.conversion_buffer[i13];
                    i13 += i12;
                    i14++;
                }
                for (int i15 = 1; i15 < this.sourceChannels; i15++) {
                    int i16 = i15;
                    int i17 = i2;
                    while (i16 < i4) {
                        int i18 = i17;
                        fArr[i18] = fArr[i18] + this.conversion_buffer[i16];
                        i16 += i12;
                        i17++;
                    }
                }
                float f2 = 1.0f / this.sourceChannels;
                int i19 = 0;
                int i20 = i2;
                while (i19 < i4) {
                    int i21 = i20;
                    fArr[i21] = fArr[i21] * f2;
                    i19 += i12;
                    i20++;
                }
            } else {
                int iMin = Math.min(this.sourceChannels, this.targetChannels);
                int i22 = i2 + i3;
                int i23 = this.targetChannels;
                int i24 = this.sourceChannels;
                for (int i25 = 0; i25 < iMin; i25++) {
                    int i26 = i2 + i25;
                    int i27 = i25;
                    while (true) {
                        int i28 = i27;
                        if (i26 < i22) {
                            fArr[i26] = this.conversion_buffer[i28];
                            i26 += i23;
                            i27 = i28 + i24;
                        }
                    }
                }
                for (int i29 = iMin; i29 < this.targetChannels; i29++) {
                    int i30 = i2;
                    int i31 = i29;
                    while (true) {
                        int i32 = i30 + i31;
                        if (i32 < i22) {
                            fArr[i32] = 0.0f;
                            i30 = i32;
                            i31 = i23;
                        }
                    }
                }
            }
            return (i5 / this.sourceChannels) * this.targetChannels;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void reset() throws IOException {
            this.ais.reset();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long skip(long j2) throws IOException {
            long jSkip = this.ais.skip((j2 / this.targetChannels) * this.sourceChannels);
            if (jSkip < 0) {
                return jSkip;
            }
            return (jSkip / this.sourceChannels) * this.targetChannels;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatFormatConverter$AudioFloatInputStreamResampler.class */
    private static class AudioFloatInputStreamResampler extends AudioFloatInputStream {
        private final AudioFloatInputStream ais;
        private final AudioFormat targetFormat;
        private float[] skipbuffer;
        private SoftAbstractResampler resampler;
        private final float[] ibuffer2;
        private final float[][] ibuffer;
        private float ibuffer_index;
        private int ibuffer_len;
        private final int nrofchannels;
        private float[][] cbuffer;
        private final int pad;
        private final int pad2;
        private final float[] pitch = new float[1];
        private final int buffer_len = 512;
        private final float[] ix = new float[1];
        private final int[] ox = new int[1];
        private float[][] mark_ibuffer = (float[][]) null;
        private float mark_ibuffer_index = 0.0f;
        private int mark_ibuffer_len = 0;

        AudioFloatInputStreamResampler(AudioFloatInputStream audioFloatInputStream, AudioFormat audioFormat) {
            this.ibuffer_index = 0.0f;
            this.ibuffer_len = 0;
            this.ais = audioFloatInputStream;
            AudioFormat format = audioFloatInputStream.getFormat();
            this.targetFormat = new AudioFormat(format.getEncoding(), audioFormat.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), audioFormat.getSampleRate(), format.isBigEndian());
            this.nrofchannels = this.targetFormat.getChannels();
            Object property = audioFormat.getProperty("interpolation");
            if (property != null && (property instanceof String)) {
                String str = (String) property;
                if (str.equalsIgnoreCase(IssuingDistributionPointExtension.POINT)) {
                    this.resampler = new SoftPointResampler();
                }
                if (str.equalsIgnoreCase("linear")) {
                    this.resampler = new SoftLinearResampler2();
                }
                if (str.equalsIgnoreCase("linear1")) {
                    this.resampler = new SoftLinearResampler();
                }
                if (str.equalsIgnoreCase("linear2")) {
                    this.resampler = new SoftLinearResampler2();
                }
                if (str.equalsIgnoreCase("cubic")) {
                    this.resampler = new SoftCubicResampler();
                }
                if (str.equalsIgnoreCase("lanczos")) {
                    this.resampler = new SoftLanczosResampler();
                }
                if (str.equalsIgnoreCase("sinc")) {
                    this.resampler = new SoftSincResampler();
                }
            }
            if (this.resampler == null) {
                this.resampler = new SoftLinearResampler2();
            }
            this.pitch[0] = format.getSampleRate() / audioFormat.getSampleRate();
            this.pad = this.resampler.getPadding();
            this.pad2 = this.pad * 2;
            this.ibuffer = new float[this.nrofchannels][512 + this.pad2];
            this.ibuffer2 = new float[this.nrofchannels * 512];
            this.ibuffer_index = 512 + this.pad;
            this.ibuffer_len = 512;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public int available() throws IOException {
            return 0;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void close() throws IOException {
            this.ais.close();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public AudioFormat getFormat() {
            return this.targetFormat;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long getFrameLength() {
            return -1L;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void mark(int i2) {
            this.ais.mark((int) (i2 * this.pitch[0]));
            this.mark_ibuffer_index = this.ibuffer_index;
            this.mark_ibuffer_len = this.ibuffer_len;
            if (this.mark_ibuffer == null) {
                this.mark_ibuffer = new float[this.ibuffer.length][this.ibuffer[0].length];
            }
            for (int i3 = 0; i3 < this.ibuffer.length; i3++) {
                float[] fArr = this.ibuffer[i3];
                float[] fArr2 = this.mark_ibuffer[i3];
                for (int i4 = 0; i4 < fArr2.length; i4++) {
                    fArr2[i4] = fArr[i4];
                }
            }
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public boolean markSupported() {
            return this.ais.markSupported();
        }

        private void readNextBuffer() throws IOException {
            int i2;
            if (this.ibuffer_len == -1) {
                return;
            }
            for (int i3 = 0; i3 < this.nrofchannels; i3++) {
                float[] fArr = this.ibuffer[i3];
                int i4 = this.ibuffer_len + this.pad2;
                int i5 = this.ibuffer_len;
                int i6 = 0;
                while (i5 < i4) {
                    fArr[i6] = fArr[i5];
                    i5++;
                    i6++;
                }
            }
            this.ibuffer_index -= this.ibuffer_len;
            this.ibuffer_len = this.ais.read(this.ibuffer2);
            if (this.ibuffer_len >= 0) {
                while (this.ibuffer_len < this.ibuffer2.length && (i2 = this.ais.read(this.ibuffer2, this.ibuffer_len, this.ibuffer2.length - this.ibuffer_len)) != -1) {
                    this.ibuffer_len += i2;
                }
                Arrays.fill(this.ibuffer2, this.ibuffer_len, this.ibuffer2.length, 0.0f);
                this.ibuffer_len /= this.nrofchannels;
            } else {
                Arrays.fill(this.ibuffer2, 0, this.ibuffer2.length, 0.0f);
            }
            int length = this.ibuffer2.length;
            for (int i7 = 0; i7 < this.nrofchannels; i7++) {
                float[] fArr2 = this.ibuffer[i7];
                int i8 = i7;
                int i9 = this.pad2;
                while (i8 < length) {
                    fArr2[i9] = this.ibuffer2[i8];
                    i8 += this.nrofchannels;
                    i9++;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:29:0x0097 A[PHI: r18
  0x0097: PHI (r18v3 int) = (r18v2 int), (r18v4 int) binds: [B:25:0x0080, B:27:0x0091] A[DONT_GENERATE, DONT_INLINE]] */
        @Override // com.sun.media.sound.AudioFloatInputStream
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int read(float[] r12, int r13, int r14) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 358
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.AudioFloatFormatConverter.AudioFloatInputStreamResampler.read(float[], int, int):int");
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void reset() throws IOException {
            this.ais.reset();
            if (this.mark_ibuffer == null) {
                return;
            }
            this.ibuffer_index = this.mark_ibuffer_index;
            this.ibuffer_len = this.mark_ibuffer_len;
            for (int i2 = 0; i2 < this.ibuffer.length; i2++) {
                float[] fArr = this.mark_ibuffer[i2];
                float[] fArr2 = this.ibuffer[i2];
                for (int i3 = 0; i3 < fArr2.length; i3++) {
                    fArr2[i3] = fArr[i3];
                }
            }
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long skip(long j2) throws IOException {
            long j3;
            if (j2 < 0) {
                return 0L;
            }
            if (this.skipbuffer == null) {
                this.skipbuffer = new float[1024 * this.targetFormat.getFrameSize()];
            }
            float[] fArr = this.skipbuffer;
            long j4 = j2;
            while (true) {
                j3 = j4;
                if (j3 <= 0) {
                    break;
                }
                int i2 = read(fArr, 0, (int) Math.min(j3, this.skipbuffer.length));
                if (i2 >= 0) {
                    j4 = j3 - i2;
                } else if (j3 == j2) {
                    return i2;
                }
            }
            return j2 - j3;
        }
    }

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public AudioInputStream getAudioInputStream(AudioFormat.Encoding encoding, AudioInputStream audioInputStream) {
        if (audioInputStream.getFormat().getEncoding().equals(encoding)) {
            return audioInputStream;
        }
        AudioFormat format = audioInputStream.getFormat();
        int channels = format.getChannels();
        float sampleRate = format.getSampleRate();
        int sampleSizeInBits = format.getSampleSizeInBits();
        boolean zIsBigEndian = format.isBigEndian();
        if (encoding.equals(AudioFormat.Encoding.PCM_FLOAT)) {
            sampleSizeInBits = 32;
        }
        return getAudioInputStream(new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, (channels * sampleSizeInBits) / 8, sampleRate, zIsBigEndian), audioInputStream);
    }

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public AudioInputStream getAudioInputStream(AudioFormat audioFormat, AudioInputStream audioInputStream) {
        if (!isConversionSupported(audioFormat, audioInputStream.getFormat())) {
            throw new IllegalArgumentException("Unsupported conversion: " + audioInputStream.getFormat().toString() + " to " + audioFormat.toString());
        }
        return getAudioInputStream(audioFormat, AudioFloatInputStream.getInputStream(audioInputStream));
    }

    public AudioInputStream getAudioInputStream(AudioFormat audioFormat, AudioFloatInputStream audioFloatInputStream) {
        if (!isConversionSupported(audioFormat, audioFloatInputStream.getFormat())) {
            throw new IllegalArgumentException("Unsupported conversion: " + audioFloatInputStream.getFormat().toString() + " to " + audioFormat.toString());
        }
        if (audioFormat.getChannels() != audioFloatInputStream.getFormat().getChannels()) {
            audioFloatInputStream = new AudioFloatInputStreamChannelMixer(audioFloatInputStream, audioFormat.getChannels());
        }
        if (Math.abs(audioFormat.getSampleRate() - audioFloatInputStream.getFormat().getSampleRate()) > 1.0E-6d) {
            audioFloatInputStream = new AudioFloatInputStreamResampler(audioFloatInputStream, audioFormat);
        }
        return new AudioInputStream(new AudioFloatFormatConverterInputStream(audioFormat, audioFloatInputStream), audioFormat, audioFloatInputStream.getFrameLength());
    }

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public AudioFormat.Encoding[] getSourceEncodings() {
        return new AudioFormat.Encoding[]{AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED, AudioFormat.Encoding.PCM_FLOAT};
    }

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public AudioFormat.Encoding[] getTargetEncodings() {
        return new AudioFormat.Encoding[]{AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED, AudioFormat.Encoding.PCM_FLOAT};
    }

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public AudioFormat.Encoding[] getTargetEncodings(AudioFormat audioFormat) {
        if (AudioFloatConverter.getConverter(audioFormat) == null) {
            return new AudioFormat.Encoding[0];
        }
        return new AudioFormat.Encoding[]{AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED, AudioFormat.Encoding.PCM_FLOAT};
    }

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public AudioFormat[] getTargetFormats(AudioFormat.Encoding encoding, AudioFormat audioFormat) {
        if (AudioFloatConverter.getConverter(audioFormat) == null) {
            return new AudioFormat[0];
        }
        int channels = audioFormat.getChannels();
        ArrayList arrayList = new ArrayList();
        if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0f, 8, channels, channels, -1.0f, false));
        }
        if (encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, -1.0f, 8, channels, channels, -1.0f, false));
        }
        for (int i2 = 16; i2 < 32; i2 += 8) {
            if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
                arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0f, i2, channels, (channels * i2) / 8, -1.0f, false));
                arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0f, i2, channels, (channels * i2) / 8, -1.0f, true));
            }
            if (encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, -1.0f, i2, channels, (channels * i2) / 8, -1.0f, true));
                arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, -1.0f, i2, channels, (channels * i2) / 8, -1.0f, false));
            }
        }
        if (encoding.equals(AudioFormat.Encoding.PCM_FLOAT)) {
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0f, 32, channels, channels * 4, -1.0f, false));
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0f, 32, channels, channels * 4, -1.0f, true));
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0f, 64, channels, channels * 8, -1.0f, false));
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0f, 64, channels, channels * 8, -1.0f, true));
        }
        return (AudioFormat[]) arrayList.toArray(new AudioFormat[arrayList.size()]);
    }

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public boolean isConversionSupported(AudioFormat audioFormat, AudioFormat audioFormat2) {
        if (AudioFloatConverter.getConverter(audioFormat2) == null || AudioFloatConverter.getConverter(audioFormat) == null || audioFormat2.getChannels() <= 0 || audioFormat.getChannels() <= 0) {
            return false;
        }
        return true;
    }

    @Override // javax.sound.sampled.spi.FormatConversionProvider
    public boolean isConversionSupported(AudioFormat.Encoding encoding, AudioFormat audioFormat) {
        if (AudioFloatConverter.getConverter(audioFormat) == null) {
            return false;
        }
        for (int i2 = 0; i2 < this.formats.length; i2++) {
            if (encoding.equals(this.formats[i2])) {
                return true;
            }
        }
        return false;
    }
}

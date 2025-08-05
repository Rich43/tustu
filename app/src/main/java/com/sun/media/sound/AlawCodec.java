package com.sun.media.sound;

import java.io.IOException;
import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/media/sound/AlawCodec.class */
public final class AlawCodec extends SunCodec {
    private static final byte[] ALAW_TABH = new byte[256];
    private static final byte[] ALAW_TABL = new byte[256];
    private static final AudioFormat.Encoding[] alawEncodings = {AudioFormat.Encoding.ALAW, AudioFormat.Encoding.PCM_SIGNED};
    private static final short[] seg_end = {255, 511, 1023, 2047, 4095, 8191, 16383, Short.MAX_VALUE};

    static {
        for (int i2 = 0; i2 < 256; i2++) {
            int i3 = i2 ^ 85;
            int i4 = (i3 & 15) << 4;
            int i5 = (i3 & 112) >> 4;
            int i6 = i4 + 8;
            if (i5 >= 1) {
                i6 += 256;
            }
            if (i5 > 1) {
                i6 <<= i5 - 1;
            }
            if ((i3 & 128) == 0) {
                i6 = -i6;
            }
            ALAW_TABL[i2] = (byte) i6;
            ALAW_TABH[i2] = (byte) (i6 >> 8);
        }
    }

    public AlawCodec() {
        super(alawEncodings, alawEncodings);
    }

    @Override // com.sun.media.sound.SunCodec, javax.sound.sampled.spi.FormatConversionProvider
    public AudioFormat.Encoding[] getTargetEncodings(AudioFormat audioFormat) {
        if (audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
            if (audioFormat.getSampleSizeInBits() == 16) {
                return new AudioFormat.Encoding[]{AudioFormat.Encoding.ALAW};
            }
            return new AudioFormat.Encoding[0];
        }
        if (audioFormat.getEncoding().equals(AudioFormat.Encoding.ALAW)) {
            if (audioFormat.getSampleSizeInBits() == 8) {
                return new AudioFormat.Encoding[]{AudioFormat.Encoding.PCM_SIGNED};
            }
            return new AudioFormat.Encoding[0];
        }
        return new AudioFormat.Encoding[0];
    }

    @Override // com.sun.media.sound.SunCodec, javax.sound.sampled.spi.FormatConversionProvider
    public AudioFormat[] getTargetFormats(AudioFormat.Encoding encoding, AudioFormat audioFormat) {
        if ((encoding.equals(AudioFormat.Encoding.PCM_SIGNED) && audioFormat.getEncoding().equals(AudioFormat.Encoding.ALAW)) || (encoding.equals(AudioFormat.Encoding.ALAW) && audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED))) {
            return getOutputFormats(audioFormat);
        }
        return new AudioFormat[0];
    }

    @Override // com.sun.media.sound.SunCodec, javax.sound.sampled.spi.FormatConversionProvider
    public AudioInputStream getAudioInputStream(AudioFormat.Encoding encoding, AudioInputStream audioInputStream) {
        AudioFormat audioFormat;
        AudioFormat format = audioInputStream.getFormat();
        AudioFormat.Encoding encoding2 = format.getEncoding();
        if (encoding2.equals(encoding)) {
            return audioInputStream;
        }
        if (!isConversionSupported(encoding, audioInputStream.getFormat())) {
            throw new IllegalArgumentException("Unsupported conversion: " + audioInputStream.getFormat().toString() + " to " + encoding.toString());
        }
        if (encoding2.equals(AudioFormat.Encoding.ALAW) && encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            audioFormat = new AudioFormat(encoding, format.getSampleRate(), 16, format.getChannels(), 2 * format.getChannels(), format.getSampleRate(), format.isBigEndian());
        } else if (encoding2.equals(AudioFormat.Encoding.PCM_SIGNED) && encoding.equals(AudioFormat.Encoding.ALAW)) {
            audioFormat = new AudioFormat(encoding, format.getSampleRate(), 8, format.getChannels(), format.getChannels(), format.getSampleRate(), false);
        } else {
            throw new IllegalArgumentException("Unsupported conversion: " + audioInputStream.getFormat().toString() + " to " + encoding.toString());
        }
        return getAudioInputStream(audioFormat, audioInputStream);
    }

    @Override // com.sun.media.sound.SunCodec, javax.sound.sampled.spi.FormatConversionProvider
    public AudioInputStream getAudioInputStream(AudioFormat audioFormat, AudioInputStream audioInputStream) {
        return getConvertedStream(audioFormat, audioInputStream);
    }

    private AudioInputStream getConvertedStream(AudioFormat audioFormat, AudioInputStream audioInputStream) {
        AudioInputStream alawCodecStream;
        if (audioInputStream.getFormat().matches(audioFormat)) {
            alawCodecStream = audioInputStream;
        } else {
            alawCodecStream = new AlawCodecStream(audioInputStream, audioFormat);
        }
        return alawCodecStream;
    }

    private AudioFormat[] getOutputFormats(AudioFormat audioFormat) {
        Vector vector = new Vector();
        if (AudioFormat.Encoding.PCM_SIGNED.equals(audioFormat.getEncoding())) {
            vector.addElement(new AudioFormat(AudioFormat.Encoding.ALAW, audioFormat.getSampleRate(), 8, audioFormat.getChannels(), audioFormat.getChannels(), audioFormat.getSampleRate(), false));
        }
        if (AudioFormat.Encoding.ALAW.equals(audioFormat.getEncoding())) {
            vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false));
            vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), true));
        }
        AudioFormat[] audioFormatArr = new AudioFormat[vector.size()];
        for (int i2 = 0; i2 < audioFormatArr.length; i2++) {
            audioFormatArr[i2] = (AudioFormat) vector.elementAt(i2);
        }
        return audioFormatArr;
    }

    /* loaded from: rt.jar:com/sun/media/sound/AlawCodec$AlawCodecStream.class */
    final class AlawCodecStream extends AudioInputStream {
        private static final int tempBufferSize = 64;
        private byte[] tempBuffer;
        boolean encode;
        AudioFormat encodeFormat;
        AudioFormat decodeFormat;
        byte[] tabByte1;
        byte[] tabByte2;
        int highByte;
        int lowByte;

        AlawCodecStream(AudioInputStream audioInputStream, AudioFormat audioFormat) {
            boolean zIsBigEndian;
            super(audioInputStream, audioFormat, -1L);
            this.tempBuffer = null;
            this.encode = false;
            this.tabByte1 = null;
            this.tabByte2 = null;
            this.highByte = 0;
            this.lowByte = 1;
            AudioFormat format = audioInputStream.getFormat();
            if (!AlawCodec.this.isConversionSupported(audioFormat, format)) {
                throw new IllegalArgumentException("Unsupported conversion: " + format.toString() + " to " + audioFormat.toString());
            }
            if (AudioFormat.Encoding.ALAW.equals(format.getEncoding())) {
                this.encode = false;
                this.encodeFormat = format;
                this.decodeFormat = audioFormat;
                zIsBigEndian = audioFormat.isBigEndian();
            } else {
                this.encode = true;
                this.encodeFormat = audioFormat;
                this.decodeFormat = format;
                zIsBigEndian = format.isBigEndian();
                this.tempBuffer = new byte[64];
            }
            if (zIsBigEndian) {
                this.tabByte1 = AlawCodec.ALAW_TABH;
                this.tabByte2 = AlawCodec.ALAW_TABL;
                this.highByte = 0;
                this.lowByte = 1;
            } else {
                this.tabByte1 = AlawCodec.ALAW_TABL;
                this.tabByte2 = AlawCodec.ALAW_TABH;
                this.highByte = 1;
                this.lowByte = 0;
            }
            if (audioInputStream instanceof AudioInputStream) {
                this.frameLength = audioInputStream.getFrameLength();
            }
            this.framePos = 0L;
            this.frameSize = format.getFrameSize();
            if (this.frameSize == -1) {
                this.frameSize = 1;
            }
        }

        private short search(short s2, short[] sArr, short s3) {
            short s4 = 0;
            while (true) {
                short s5 = s4;
                if (s5 < s3) {
                    if (s2 <= sArr[s5]) {
                        return s5;
                    }
                    s4 = (short) (s5 + 1);
                } else {
                    return s3;
                }
            }
        }

        @Override // javax.sound.sampled.AudioInputStream, java.io.InputStream
        public int read() throws IOException {
            byte[] bArr = new byte[1];
            return read(bArr, 0, bArr.length);
        }

        @Override // javax.sound.sampled.AudioInputStream, java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        @Override // javax.sound.sampled.AudioInputStream, java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            int i4;
            byte b2;
            byte b3;
            byte b4;
            if (i3 % this.frameSize != 0) {
                i3 -= i3 % this.frameSize;
            }
            if (this.encode) {
                int i5 = i2;
                int i6 = i3 * 2;
                int i7 = i6 > 64 ? 64 : i6;
                while (true) {
                    i4 = super.read(this.tempBuffer, 0, i7);
                    if (i4 <= 0) {
                        break;
                    }
                    for (int i8 = 0; i8 < i4; i8 += 2) {
                        short s2 = (short) (((short) ((this.tempBuffer[i8 + this.highByte] << 8) & NormalizerImpl.CC_MASK)) | ((short) (this.tempBuffer[i8 + this.lowByte] & 255)));
                        if (s2 >= 0) {
                            b2 = 213;
                        } else {
                            b2 = 85;
                            s2 = (short) ((-s2) - 8);
                        }
                        short sSearch = search(s2, AlawCodec.seg_end, (short) 8);
                        if (sSearch >= 8) {
                            b4 = Byte.MAX_VALUE;
                        } else {
                            byte b5 = (byte) (sSearch << 4);
                            if (sSearch < 2) {
                                b3 = (byte) (b5 | ((byte) ((s2 >> 4) & 15)));
                            } else {
                                b3 = (byte) (b5 | ((byte) ((s2 >> (sSearch + 3)) & 15)));
                            }
                            b4 = b3;
                        }
                        bArr[i5] = (byte) (b4 ^ b2);
                        i5++;
                    }
                    i6 -= i4;
                    i7 = i6 > 64 ? 64 : i6;
                }
                if (i5 == i2 && i4 < 0) {
                    return i4;
                }
                return i5 - i2;
            }
            int i9 = i2 + (i3 / 2);
            int i10 = super.read(bArr, i9, i3 / 2);
            int i11 = i2;
            while (i11 < i2 + (i10 * 2)) {
                bArr[i11] = this.tabByte1[bArr[i9] & 255];
                bArr[i11 + 1] = this.tabByte2[bArr[i9] & 255];
                i9++;
                i11 += 2;
            }
            if (i10 < 0) {
                return i10;
            }
            return i11 - i2;
        }
    }
}

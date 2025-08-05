package com.sun.media.sound;

import java.io.IOException;
import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/media/sound/UlawCodec.class */
public final class UlawCodec extends SunCodec {
    private static final byte[] ULAW_TABH = new byte[256];
    private static final byte[] ULAW_TABL = new byte[256];
    private static final AudioFormat.Encoding[] ulawEncodings = {AudioFormat.Encoding.ULAW, AudioFormat.Encoding.PCM_SIGNED};
    private static final short[] seg_end = {255, 511, 1023, 2047, 4095, 8191, 16383, Short.MAX_VALUE};

    static {
        int i2;
        int i3;
        for (int i4 = 0; i4 < 256; i4++) {
            int i5 = (i4 ^ (-1)) & 255;
            int i6 = (((i5 & 15) << 3) + 132) << ((i5 & 112) >> 4);
            if ((i5 & 128) != 0) {
                i2 = 132;
                i3 = i6;
            } else {
                i2 = i6;
                i3 = 132;
            }
            int i7 = i2 - i3;
            ULAW_TABL[i4] = (byte) (i7 & 255);
            ULAW_TABH[i4] = (byte) ((i7 >> 8) & 255);
        }
    }

    public UlawCodec() {
        super(ulawEncodings, ulawEncodings);
    }

    @Override // com.sun.media.sound.SunCodec, javax.sound.sampled.spi.FormatConversionProvider
    public AudioFormat.Encoding[] getTargetEncodings(AudioFormat audioFormat) {
        if (AudioFormat.Encoding.PCM_SIGNED.equals(audioFormat.getEncoding())) {
            if (audioFormat.getSampleSizeInBits() == 16) {
                return new AudioFormat.Encoding[]{AudioFormat.Encoding.ULAW};
            }
            return new AudioFormat.Encoding[0];
        }
        if (AudioFormat.Encoding.ULAW.equals(audioFormat.getEncoding())) {
            if (audioFormat.getSampleSizeInBits() == 8) {
                return new AudioFormat.Encoding[]{AudioFormat.Encoding.PCM_SIGNED};
            }
            return new AudioFormat.Encoding[0];
        }
        return new AudioFormat.Encoding[0];
    }

    @Override // com.sun.media.sound.SunCodec, javax.sound.sampled.spi.FormatConversionProvider
    public AudioFormat[] getTargetFormats(AudioFormat.Encoding encoding, AudioFormat audioFormat) {
        if ((AudioFormat.Encoding.PCM_SIGNED.equals(encoding) && AudioFormat.Encoding.ULAW.equals(audioFormat.getEncoding())) || (AudioFormat.Encoding.ULAW.equals(encoding) && AudioFormat.Encoding.PCM_SIGNED.equals(audioFormat.getEncoding()))) {
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
        if (AudioFormat.Encoding.ULAW.equals(encoding2) && AudioFormat.Encoding.PCM_SIGNED.equals(encoding)) {
            audioFormat = new AudioFormat(encoding, format.getSampleRate(), 16, format.getChannels(), 2 * format.getChannels(), format.getSampleRate(), format.isBigEndian());
        } else if (AudioFormat.Encoding.PCM_SIGNED.equals(encoding2) && AudioFormat.Encoding.ULAW.equals(encoding)) {
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
        AudioInputStream ulawCodecStream;
        if (audioInputStream.getFormat().matches(audioFormat)) {
            ulawCodecStream = audioInputStream;
        } else {
            ulawCodecStream = new UlawCodecStream(audioInputStream, audioFormat);
        }
        return ulawCodecStream;
    }

    private AudioFormat[] getOutputFormats(AudioFormat audioFormat) {
        Vector vector = new Vector();
        if (audioFormat.getSampleSizeInBits() == 16 && AudioFormat.Encoding.PCM_SIGNED.equals(audioFormat.getEncoding())) {
            vector.addElement(new AudioFormat(AudioFormat.Encoding.ULAW, audioFormat.getSampleRate(), 8, audioFormat.getChannels(), audioFormat.getChannels(), audioFormat.getSampleRate(), false));
        }
        if (AudioFormat.Encoding.ULAW.equals(audioFormat.getEncoding())) {
            vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false));
            vector.addElement(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), true));
        }
        AudioFormat[] audioFormatArr = new AudioFormat[vector.size()];
        for (int i2 = 0; i2 < audioFormatArr.length; i2++) {
            audioFormatArr[i2] = (AudioFormat) vector.elementAt(i2);
        }
        return audioFormatArr;
    }

    /* loaded from: rt.jar:com/sun/media/sound/UlawCodec$UlawCodecStream.class */
    class UlawCodecStream extends AudioInputStream {
        private static final int tempBufferSize = 64;
        private byte[] tempBuffer;
        boolean encode;
        AudioFormat encodeFormat;
        AudioFormat decodeFormat;
        byte[] tabByte1;
        byte[] tabByte2;
        int highByte;
        int lowByte;

        UlawCodecStream(AudioInputStream audioInputStream, AudioFormat audioFormat) {
            boolean zIsBigEndian;
            super(audioInputStream, audioFormat, -1L);
            this.tempBuffer = null;
            this.encode = false;
            this.tabByte1 = null;
            this.tabByte2 = null;
            this.highByte = 0;
            this.lowByte = 1;
            AudioFormat format = audioInputStream.getFormat();
            if (!UlawCodec.this.isConversionSupported(audioFormat, format)) {
                throw new IllegalArgumentException("Unsupported conversion: " + format.toString() + " to " + audioFormat.toString());
            }
            if (AudioFormat.Encoding.ULAW.equals(format.getEncoding())) {
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
                this.tabByte1 = UlawCodec.ULAW_TABH;
                this.tabByte2 = UlawCodec.ULAW_TABL;
                this.highByte = 0;
                this.lowByte = 1;
            } else {
                this.tabByte1 = UlawCodec.ULAW_TABL;
                this.tabByte2 = UlawCodec.ULAW_TABH;
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
            if (read(bArr, 0, bArr.length) == 1) {
                return bArr[1] & 255;
            }
            return -1;
        }

        @Override // javax.sound.sampled.AudioInputStream, java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        @Override // javax.sound.sampled.AudioInputStream, java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            int i4;
            short s2;
            byte b2;
            byte b3;
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
                        short s3 = (short) (((short) ((this.tempBuffer[i8 + this.highByte] << 8) & NormalizerImpl.CC_MASK)) | ((short) (this.tempBuffer[i8 + this.lowByte] & 255)));
                        if (s3 < 0) {
                            s2 = (short) (132 - s3);
                            b2 = Byte.MAX_VALUE;
                        } else {
                            s2 = (short) (s3 + 132);
                            b2 = 255;
                        }
                        short sSearch = search(s2, UlawCodec.seg_end, (short) 8);
                        if (sSearch >= 8) {
                            b3 = Byte.MAX_VALUE;
                        } else {
                            b3 = (byte) ((sSearch << 4) | ((s2 >> (sSearch + 3)) & 15));
                        }
                        bArr[i5] = (byte) (b3 ^ b2);
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
            if (i10 < 0) {
                return i10;
            }
            int i11 = i2;
            while (i11 < i2 + (i10 * 2)) {
                bArr[i11] = this.tabByte1[bArr[i9] & 255];
                bArr[i11 + 1] = this.tabByte2[bArr[i9] & 255];
                i9++;
                i11 += 2;
            }
            return i11 - i2;
        }
    }
}
